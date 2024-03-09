--;
--table name: eportal.account_check_point;
--;
CREATE TABLE EPORTAL.ACCOUNT_CHECK_POINT
(
    ID VARCHAR(36) NOT NULL
);
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT
    ADD DT TIMESTAMP;
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT
    ADD ACCOUNT_ID VARCHAR(36) NOT NULL;
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT
    ADD MOVEMENT_ID VARCHAR(36) NOT NULL;
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT
    ADD VALUE NUMERIC(15, 5) DEFAULT 0 NOT NULL;
--;
--table name: eportal.account_check_point_rev;
--;
CREATE TABLE EPORTAL.ACCOUNT_CHECK_POINT_REV
(
    ID VARCHAR(36) NOT NULL
);
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT_REV
    ADD DT TIMESTAMP;
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT_REV
    ADD ACCOUNT_ID VARCHAR(36);
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT_REV
    ADD MOVEMENT_ID VARCHAR(36);
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT_REV
    ADD VALUE NUMERIC(15, 5) DEFAULT 0;
--;
--table name: eportal.account_movement;
--;
CREATE TABLE EPORTAL.ACCOUNT_MOVEMENT
(
    ID VARCHAR(36) NOT NULL
);
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT
    ADD DT TIMESTAMP;
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT
    ADD ACCOUNT_ID VARCHAR(36);
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT
    ADD OPERATION NUMBER DEFAULT 0;
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT
    ADD REFERENCE_ID VARCHAR(36);
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT
    ADD VALUE NUMERIC(15, 5) DEFAULT 0;
--;
--table name: eportal.account_movement_state;
--;
CREATE TABLE EPORTAL.ACCOUNT_MOVEMENT_STATE
(
    ID VARCHAR(36) NOT NULL
);
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT_STATE
    ADD DT TIMESTAMP;
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT_STATE
    ADD ACCOUNT_MOVEMENT_STATE VARCHAR(36);
--;
--table name: eportal.account_target;
--;
CREATE TABLE EPORTAL.ACCOUNT_TARGET
(
    ID VARCHAR(36) NOT NULL
);
ALTER TABLE EPORTAL.ACCOUNT_TARGET
    ADD DT TIMESTAMP NOT NULL;
ALTER TABLE EPORTAL.ACCOUNT_TARGET
    ADD ACCOUNT_TARGET VARCHAR(36) NOT NULL;
ALTER TABLE EPORTAL.ACCOUNT_TARGET
    ADD ACCOUNT_TYPE NUMBER DEFAULT 0 NOT NULL;
ALTER TABLE EPORTAL.ACCOUNT_TARGET
    ADD ACCOUNT_KEY VARCHAR(36) NOT NULL;
ALTER TABLE EPORTAL.ACCOUNT_TARGET
    ADD ACCOUNT_ARGS VARCHAR(255) DEFAULT '' NOT NULL;
ALTER TABLE EPORTAL.ACCOUNT_TARGET
    ADD ENABLED NUMBER(1) DEFAULT 0 NOT NULL;
