--;
--table name: srv_auth.grant_code;
--;
create table if not exists srv_auth.grant_code(id uuid not null );
alter table if exists srv_auth.grant_code add if not exists dt timestamp not null;
alter table if exists srv_auth.grant_code add if not exists scope_id uuid not null;
alter table if exists srv_auth.grant_code add if not exists user_id uuid not null;
alter table if exists srv_auth.grant_code add if not exists expires timestamp not null;
alter table if exists srv_auth.grant_code add if not exists enabled bool not null  default false;
--;
--table name: srv_auth.scope;
--;
create table if not exists srv_auth.scope(id uuid not null );
alter table if exists srv_auth.scope add if not exists dt timestamp not null;
alter table if exists srv_auth.scope add if not exists name varchar(50) not null  default '';
alter table if exists srv_auth.scope add if not exists enabled bool not null  default false;
--;
--table name: srv_auth.token;
--;
create table if not exists srv_auth.token(id uuid not null );
alter table if exists srv_auth.token add if not exists dt timestamp not null;
alter table if exists srv_auth.token add if not exists scope_id uuid not null;
alter table if exists srv_auth.token add if not exists user_id uuid not null;
alter table if exists srv_auth.token add if not exists grant_code_id uuid;
alter table if exists srv_auth.token add if not exists token_type varchar(255) not null  default '';
alter table if exists srv_auth.token add if not exists access_token varchar(1000) not null  default '';
alter table if exists srv_auth.token add if not exists access_token_md5 uuid;
alter table if exists srv_auth.token add if not exists refresh_token varchar(1000) not null  default '';
alter table if exists srv_auth.token add if not exists refresh_token_md5 uuid;
alter table if exists srv_auth.token add if not exists expires_in bigint not null  default 0;
alter table if exists srv_auth.token add if not exists enabled bool not null  default false;
--;
--table name: srv_auth.user;
--;
create table if not exists srv_auth.user(id uuid not null );
alter table if exists srv_auth.user add if not exists scope_id uuid not null;
alter table if exists srv_auth.user add if not exists dt timestamp not null;
alter table if exists srv_auth.user add if not exists name varchar(50) not null  default '';
alter table if exists srv_auth.user add if not exists username varchar(50) not null  default '';
alter table if exists srv_auth.user add if not exists password varchar(200) not null  default '';
alter table if exists srv_auth.user add if not exists password_crypt varchar(200) not null  default '';
alter table if exists srv_auth.user add if not exists document varchar(20) not null  default '';
alter table if exists srv_auth.user add if not exists email varchar(50) not null  default '';
alter table if exists srv_auth.user add if not exists phone_number varchar(20) not null  default '';
alter table if exists srv_auth.user add if not exists dt_birth date not null;
alter table if exists srv_auth.user add if not exists enabled bool not null  default false;
alter table if exists srv_auth.user add if not exists deleted bool not null  default false;