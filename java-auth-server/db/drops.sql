--;
--schema name: srv_auth;
--;
drop schema if exists srv_auth cascade;
--;
--table name: srv_auth.grant_code;
--;
drop table if exists srv_auth.grant_code cascade;
--;
--table name: srv_auth.scope;
--;
drop table if exists srv_auth.scope cascade;
--;
--table name: srv_auth.token;
--;
drop table if exists srv_auth.token cascade;
--;
--table name: srv_auth.user;
--;
drop table if exists srv_auth.user cascade;
