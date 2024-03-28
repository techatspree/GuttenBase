CREATE TABLE FOO_USER
(
    ID              bigint       NOT NULL,
    PERSONAL_NUMBER SMALLINT,
    USER_NAME       varchar(100) NOT NULL,
    NAME            varchar(100) NOT NULL,
    PASSWORD        varchar(255) NOT NULL,
    COMPANY_ID      bigint       NOT NULL
);

CREATE TABLE FOO_USER_SYSTEMS
(
    USER_ID   bigint       NOT NULL,
    USER_NAME varchar(100) NOT NULL,
    SYSTEMS   VARCHAR(100) NOT NULL
);

CREATE TABLE FOO_COMPANY
(
    ID       bigint PRIMARY KEY,
    SUPPLIER char(1),
    NAME     varchar(100) NOT NULL
);

ALTER TABLE FOO_USER
    ADD CONSTRAINT FK_FOO_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES FOO_COMPANY (ID);

ALTER TABLE FOO_USER
    ADD CONSTRAINT PK_ENTITY PRIMARY KEY (ID, USER_NAME);

ALTER TABLE FOO_USER_SYSTEMS
    ADD CONSTRAINT FK_FOO_USER_SYSTEMS FOREIGN KEY (USER_ID, USER_NAME) REFERENCES FOO_USER (ID, USER_NAME);

CREATE UNIQUE INDEX FOO_USER_IDX ON FOO_USER (ID);

CREATE INDEX COMPANY_IDX ON FOO_USER (COMPANY_ID);

CREATE UNIQUE INDEX USERNAME_IDX ON FOO_USER (USER_NAME);

CREATE UNIQUE INDEX COMPANY_NAME_IDX ON FOO_COMPANY (NAME);