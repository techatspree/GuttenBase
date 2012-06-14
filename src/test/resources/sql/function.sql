create or replace function drop_all_sequences() returns integer as '
declare
 rec record;
begin
 for rec in select relname as seqname
   from pg_class where relkind=''S''
 loop
   execute ''drop sequence '' || rec.seqname;
 end loop;
 return 1;
end;
' language 'plpgsql';