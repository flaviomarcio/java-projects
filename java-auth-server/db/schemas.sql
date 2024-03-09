--;
--schema name: srv_auth;
--;
create schema if not exists srv_auth;
set search_path to srv_auth;
--;
--default command: ;
--;
create extension if not exists "uuid-ossp";
