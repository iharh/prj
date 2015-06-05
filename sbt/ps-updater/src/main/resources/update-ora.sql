create or replace
procedure reprocess_build_staging ( 
	stg_schema_name character varying,
    stg_table_name character varying default 'STG',
	subsql character varying default ''
)  as 

is_debug boolean default false;

-- variable for dynamically created sql queries
dynamic_sql character varying(32000);
stg_sp_call character varying(32000);
create_sql character varying(31500);
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

begin
    v_START_DATE := sysdate;
    
    cCRLF:= chr(13)||chr(10);
        
    if is_debug then
        DBMS_OUTPUT.ENABLE(1000000);
        dbms_output.put_line('Debug mode.');
        dbms_output.put_line('Staging table name: ' || stg_table_name);
    end if;

    create_sql := 'CREATE TABLE ' || stg_table_name || ' (
    CBRP_DOCUMENT_ID numeric(20,0),
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
	if get_text_column_type(user, 'p_document', 'cbrp_session_date_id') is null then
		create_sql := create_sql ||','||cCRLF|| 'CBRP_SESSION_DATE TIMESTAMP(6)';
		insert_sql_1 := insert_sql_1 ||','||cCRLF|| 'cbrp_session_date';
		insert_sql_2 := insert_sql_2 ||','||cCRLF|| '(select ss.session_date from p_session ss where ss.session_id = d.session_id)';
        column_list := column_list || 'CBRP_SESSION_DATE|';
	end if;
	
    for i in (select attribute_type, attribute_name, dimension_name, is_keep_date from p_attribute) loop
        -- skip CBRP_DOCUMENT_ID
		if upper(i.attribute_name) not in ('CBRP_DOCUMENT_ID', 'CBRP_CMP_SOURCE', 'CBRP_NATURAL_ID', 'CBRP_PROCESSED_FLAG', 'CB_BC_INDUSTRY', 'CB_BC_COMPANY', 'CB_BC_BRAND', 'CB_BC_PRODUCT') 
		      and i.attribute_type not in ('SATSCORE','RANGE','DIMENSIONAL') then   -- ignore derived attributes     
		
            -- skip attributes that were defined but never appeared in the data
            if i.attribute_type = 'TEXT' then
                if i.dimension_name is null then
                    test_col_name   := i.attribute_name;
                else
                    test_col_name := i.attribute_name || '_id';
                end if;
            elsif i.attribute_type = 'NUMBER' then
                test_col_name      := i.attribute_name;
            elsif i.attribute_type = 'DATE' then
                test_col_name      := i.attribute_name || '_id';
            end if;
            
            if get_text_column_type(user, 'p_document', test_col_name) is not null then
    
                insert_sql_1 := insert_sql_1 ||','||cCRLF|| i.ATTRIBUTE_NAME;
                column_list := column_list || i.ATTRIBUTE_NAME || '|';
            
                IF I.ATTRIBUTE_TYPE = 'TEXT' THEN 
                    if  i.DIMENSION_NAME is null or trim(i.DIMENSION_NAME) = '' then
                    -- have to use original datatype as it could be CLOB equivalent for the longer text fields
                        create_sql := create_sql ||','||cCRLF|| i.ATTRIBUTE_NAME || ' ' || coalesce(get_text_column_type(user, 'p_document', i.ATTRIBUTE_NAME), '');
                        insert_sql_2 := insert_sql_2 ||','||cCRLF|| i.ATTRIBUTE_NAME;
                    else  
                        create_sql := create_sql ||','||cCRLF|| i.ATTRIBUTE_NAME || ' character varying(1000)'; -- only 1000 is used in dimension table
                        insert_sql_2 := insert_sql_2 ||','||cCRLF|| '(select '||i.ATTRIBUTE_NAME||'_value from '||i.DIMENSION_NAME||' s where s.'||i.ATTRIBUTE_NAME||'_id=d.'||i.ATTRIBUTE_NAME||'_id)';
                    end if;    
                ELSIF i.ATTRIBUTE_TYPE = 'NUMBER' THEN 
                    create_sql := create_sql ||','||cCRLF|| i.ATTRIBUTE_NAME || ' NUMBER';
                    insert_sql_2 := insert_sql_2 ||','||cCRLF|| i.ATTRIBUTE_NAME;
                ELSIF i.ATTRIBUTE_TYPE = 'DATE' THEN 
                    create_sql := create_sql ||','||cCRLF|| i.ATTRIBUTE_NAME || ' TIMESTAMP(6)'; 
                    if i.is_keep_date = 1 then
                        insert_sql_2 := insert_sql_2 ||','||cCRLF|| i.ATTRIBUTE_NAME;
                    else
                        insert_sql_2 := insert_sql_2 ||','||cCRLF|| '(select TIME_VALUE from PD_TIME s where s.time_id=d.'||i.ATTRIBUTE_NAME||'_id)';
                    end if;
                END IF;
            end if;
        end if;
    end loop;

    for i in (SELECT VERBATIM_TYPE_ID, VERBATIM_TYPE_VALUE FROM PD_VERBATIM_TYPE) loop
	    col_name := substr(regexp_replace(i.VERBATIM_TYPE_VALUE, '\W', '_'),1,28);
        
        col_name_candidate := col_name;
        while instr(upper(column_list),upper('|' || col_name_candidate || '|')) > 0 loop        
            col_name_candidate := col_name || DBMS_RANDOM.STRING('X', 2);
        end loop;
        
         	for j in (select keyword from ${system_user_name}.SYS_WORDS) loop
 	 			IF j.keyword = upper(col_name_candidate) THEN
 	 		  	col_name_candidate := col_name || DBMS_RANDOM.STRING('X', 2);
 	 		  	end if;
 	 		end loop;
        
        col_name := col_name_candidate;
        column_list := column_list || col_name || '|';
        
        create_sql := create_sql ||','||cCRLF ||col_name || ' CLOB';
        insert_sql_1 := insert_sql_1 ||','||cCRLF||col_name;
        insert_sql_2 := insert_sql_2 ||','||cCRLF|| '(select EXTRACTED_ORIGINAL from P_VERBATIM v where v.document_id=d.document_id and v.verbatim_type_id='||i.VERBATIM_TYPE_ID||')';
    end loop;
    
    create_sql := create_sql || ')';
    insert_sql_1 := insert_sql_1 || ') ';
    insert_sql_2 := insert_sql_2 || cCRLF || ' from p_document d ';
    
    
    if is_debug then
        dbms_output.put_line(create_sql);
        dbms_output.put_line(insert_sql_1);
        dbms_output.put_line(insert_sql_2);
		dbms_output.put_line(subsql);
    end if;
    
    IF (NOT IS_DEBUG) THEN 
      EXECUTE IMMEDIATE create_sql;
        if (subsql is null or subsql= '') then
		EXECUTE IMMEDIATE insert_sql_1||insert_sql_2;
	  else
	    EXECUTE IMMEDIATE insert_sql_1||insert_sql_2||'where d.document_id in ('||subsql||')';
	  end if;
      EXECUTE IMMEDIATE 'grant all on ' || stg_table_name || ' to ' || stg_schema_name;
    END IF;
   
    if is_debug then
        dbms_output.put_line('Processing is done');
        dbms_output.put_line('it started at: ' || to_char(v_START_DATE, 'DD-MON-YYYY HH24:MI:SS') || ' and finished at: ' || to_char(SYSDATE, 'DD-MON-YYYY HH24:MI:SS'));
    end if;
end;
