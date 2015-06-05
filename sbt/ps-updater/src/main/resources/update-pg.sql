create or replace function reprocess_build_staging ( 
	stg_table_name character varying default 'STG' ,
	subsql character varying default ''
)  returns void 
as \$\$
declare

is_debug boolean default false;

-- variable for dynamically created sql queries
dynamic_sql character varying(32000);
create_sql character varying(32000);
insert_sql_1 character varying(32000);
insert_sql_2 character varying(32000);

v_ATTR_DIMENSION_NAME character varying(200);
v_ATTR_TYPE character varying(50);
v_ATTR_NAME  character varying(200);
v_UPDATE_FLAG numeric;
v_VERBATIM_TYPE_ID numeric;
v_START_DATE timestamp;
cCRLF character varying(2);
col_name character varying(32);
col_name_candidate character varying(32);
test_col_name character varying(32);
column_list character varying(30000) := '|';
i	record;
j	record;

BEGIN
    v_START_DATE := now();
    
    cCRLF:= chr(13)||chr(10);
        
    if is_debug then
	raise notice 'Debug mode.';
	raise notice 'Staging table name: % ', stg_table_name;
    end if;

    create_sql := 'CREATE TABLE ' || stg_table_name || ' (
    CBRP_DOCUMENT_ID numeric,
    CBRP_CMP_SOURCE character varying(4000),
    CBRP_NATURAL_ID character varying(256),
    CBRP_PROCESSED_FLAG character varying(1)';
    insert_sql_1 := 'insert into '|| stg_table_name || '(
    CBRP_DOCUMENT_ID,
    CBRP_CMP_SOURCE,
    CBRP_NATURAL_ID,
    CBRP_PROCESSED_FLAG';
    insert_sql_2 := 'select d.document_id,
    (select source_value from pd_source s where s.source_id=d.source_id),
    d.natural_id,
    ''N''';
    column_list := column_list || 'CBRP_DOCUMENT_ID|CBRP_CMP_SOURCE|CBRP_NATURAL_ID|CBRP_PROCESSED_FLAG|';

	-- if original CB_ORIG_SESSION_DATE is not present in the p_document table, use date from the session table
	if get_column_type(current_schema::character varying, 'p_document', 'cbrp_session_date_id') is null then
		create_sql := create_sql ||','||cCRLF|| 'CBRP_SESSION_DATE TIMESTAMP(6)';
		insert_sql_1 := insert_sql_1 ||','||cCRLF|| 'cbrp_session_date';
		insert_sql_2 := insert_sql_2 ||','||cCRLF|| '(select ss.session_date from p_session ss where ss.session_id = d.session_id)';
		column_list := column_list || 'CBRP_SESSION_DATE|';
	end if;
	
    for i in select ATTRIBUTE_TYPE, ATTRIBUTE_NAME, DIMENSION_NAME, is_keep_date from P_ATTRIBUTE 
    loop
        -- skip CBRP_DOCUMENT_ID
		continue when upper(i.attribute_name) in ('CBRP_DOCUMENT_ID', 'CBRP_CMP_SOURCE', 'CBRP_NATURAL_ID', 'CBRP_PROCESSED_FLAG', 'CB_BC_INDUSTRY', 'CB_BC_COMPANY', 'CB_BC_BRAND', 'CB_BC_PRODUCT');
		continue when i.attribute_type in ('SATSCORE','RANGE','DIMENSIONAL'); -- ignore derived attributes
		-- skip attributes that were defined but never appeared in the data
		if i.attribute_type = 'TEXT' then
			if  i.dimension_name is null or trim(i.dimension_name) = '' then
				test_col_name := i.attribute_name;
			else 
				test_col_name := i.attribute_name || '_id';
			end if;
		elseif i.attribute_type = 'NUMBER' then
			test_col_name := i.attribute_name;
		elseif i.attribute_type = 'DATE' then
			test_col_name := i.attribute_name || '_id';
		end if;
		continue when get_column_type(current_schema::character varying, 'p_document', lower(test_col_name)) is null;

        insert_sql_1 := insert_sql_1 ||','||cCRLF|| i.ATTRIBUTE_NAME;
        column_list := column_list || i.ATTRIBUTE_NAME || '|';


        IF I.ATTRIBUTE_TYPE = 'TEXT' THEN 
			if  i.DIMENSION_NAME is null or trim(i.DIMENSION_NAME) = '' then
			-- have to use original datatype as it could be CLOB equivalent for the longer text fields
				if is_debug then
					raise notice 'attribute:%, type:%',  lower(i.ATTRIBUTE_NAME), coalesce(get_column_type(current_schema::character varying, 'p_document', lower(i.ATTRIBUTE_NAME)), 'x'); 
				end if;

				create_sql := create_sql ||','||cCRLF|| i.ATTRIBUTE_NAME || ' ' || coalesce(get_column_type(current_schema::character varying, 'p_document', lower(i.ATTRIBUTE_NAME)), '');
				insert_sql_2 := insert_sql_2 ||','||cCRLF|| i.ATTRIBUTE_NAME;
			else  
				create_sql := create_sql ||','||cCRLF|| i.ATTRIBUTE_NAME || ' character varying(1000)'; -- only 1000 is used in dimension table
				insert_sql_2 := insert_sql_2 ||','||cCRLF|| '(select '||i.ATTRIBUTE_NAME||'_value from '||i.DIMENSION_NAME||' s where s.'||i.ATTRIBUTE_NAME||'_id=d.'||i.ATTRIBUTE_NAME||'_id)';
			end if;    
        ELSIF i.ATTRIBUTE_TYPE = 'NUMBER' THEN 
            create_sql := create_sql ||','||cCRLF|| i.ATTRIBUTE_NAME || ' NUMERIC';
            insert_sql_2 := insert_sql_2 ||','||cCRLF|| i.ATTRIBUTE_NAME;
        ELSIF i.ATTRIBUTE_TYPE = 'DATE' THEN 
            create_sql := create_sql ||','||cCRLF|| i.ATTRIBUTE_NAME || ' TIMESTAMP(6)'; 
			if i.is_keep_date = 1 then
				insert_sql_2 := insert_sql_2 ||','||cCRLF|| i.ATTRIBUTE_NAME;
			else
				insert_sql_2 := insert_sql_2 ||','||cCRLF|| '(select TIME_VALUE from PD_TIME s where s.time_id=d.'||i.ATTRIBUTE_NAME||'_id)';
			end if;
        END IF;
    end loop;

    for i in (SELECT VERBATIM_TYPE_ID, VERBATIM_TYPE_VALUE FROM PD_VERBATIM_TYPE) loop
	    col_name := substr(regexp_replace(i.VERBATIM_TYPE_VALUE, E'\\W', '_', 'g'),1,28);

        col_name_candidate := col_name;
        while position(upper('|' || col_name_candidate || '|') in upper(column_list)) > 0 loop        
            col_name_candidate := col_name || round(random() * 100);
        end loop;
        
         for j in (select word from $system_user_name$.SYS_WORDS) loop
            IF j.word = lower(col_name_candidate) THEN 
              col_name_candidate := col_name || round(random() * 100);
            end if;
          end loop;
            
        col_name := col_name_candidate;
        column_list := column_list || col_name || '|';
        create_sql := create_sql ||','||cCRLF ||col_name || ' TEXT';
        insert_sql_1 := insert_sql_1 ||','||cCRLF||col_name;
        insert_sql_2 := insert_sql_2 ||','||cCRLF|| '(select EXTRACTED_ORIGINAL from P_VERBATIM v where v.document_id=d.document_id and v.verbatim_type_id='||i.VERBATIM_TYPE_ID||')';
    end loop;
    
    create_sql := create_sql || ')';
    insert_sql_1 := insert_sql_1 || ') ';
    insert_sql_2 := insert_sql_2 || cCRLF || ' from p_document d ';
    
    if is_debug then
        raise notice '%', create_sql;
        raise notice '%', insert_sql_1;
        raise notice '%', insert_sql_2;
		raise notice '%', subsql;
    end if;
    
    IF (not is_debug) THEN 
      EXECUTE create_sql; 
	  if subsql = '' then
		EXECUTE insert_sql_1||insert_sql_2;
	  else
		EXECUTE insert_sql_1||insert_sql_2 ||'where d.document_id in ('||subsql||')';
	  end if;
    END IF;
   
    if is_debug then
        raise notice 'Processing is done';
        raise notice 'started at: %', to_char(v_START_DATE, 'DD-MON-YYYY HH24:MI:SS') || ' and finished at: ' || to_char(now(), 'DD-MON-YYYY HH24:MI:SS');
    end if;
END;
\$\$ language 'plpgsql';
