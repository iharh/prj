create or replace procedure ddd ( 
	subsql character varying default ''
)  as 

is_debug boolean default true;

begin
    dbms_output.enable(1000000);
    dbms_output.put_line('Debug mode.');
    dbms_output.put_line('subsql: ' || subsql);
    
    if (subsql is null or subsql= '') then
       dbms_output.put_line('default chosen');
	  else
       dbms_output.put_line('non default chosen');
	  end if;
end;
