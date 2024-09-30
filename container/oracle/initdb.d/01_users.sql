alter session set container = XEPDB1;

ALTER SYSTEM SET db_create_file_dest = '/opt/oracle/oradata';

create tablespace CALENDAR;

create user "CALENDAR_OWNER" profile "DEFAULT" identified by "password" default tablespace "CALENDAR" account unlock;

grant connect to CALENDAR_OWNER;
grant unlimited tablespace to CALENDAR_OWNER;

grant create view to CALENDAR_OWNER;
grant create sequence to CALENDAR_OWNER;
grant create table to CALENDAR_OWNER;
grant create procedure to CALENDAR_OWNER;
grant create type to CALENDAR_OWNER;