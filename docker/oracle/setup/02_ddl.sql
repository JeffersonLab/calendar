alter session set container = XEPDB1;

--DROP SEQUENCE CALENDAR_OWNER.HIBERNATE_SEQUENCE;
--DROP SEQUENCE CALENDAR_OWNER.EVENT_ID;
--DROP SEQUENCE CALENDAR_OWNER.OCCURRENCE_ID;
--DROP SEQUENCE CALENDAR_OWNER.OCCURRENCE_STYLE_ID;

--DROP TABLE CALENDAR_OWNER.CALENDAR CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.EVENT CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.OCCURRENCE CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.OCCURRENCE_STYLE CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.CALENDAR_REVISION_INFO CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.EVENT_AUD CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.OCCURRENCE_AUD CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.OCCURRENCE_STYLE_AUD CASCADE CONSTRAINTS PURGE;
--DROP TABLE CALENDAR_OWNER.ATLIS_TASK CASCADE CONSTRAINTS PURGE;

CREATE SEQUENCE CALENDAR_OWNER.HIBERNATE_SEQUENCE
    START WITH 1
    NOCYCLE
    NOCACHE
    ORDER;

CREATE SEQUENCE CALENDAR_OWNER.EVENT_ID
    START WITH 1
    NOCYCLE
    NOCACHE
    ORDER;

CREATE SEQUENCE CALENDAR_OWNER.OCCURRENCE_ID
    START WITH 1
    NOCYCLE
    NOCACHE
    ORDER;

CREATE SEQUENCE CALENDAR_OWNER.OCCURRENCE_STYLE_ID
    START WITH 1
    NOCYCLE
    NOCACHE
    ORDER;

CREATE TABLE CALENDAR_OWNER.CALENDAR
(
    CALENDAR_ID          INTEGER NOT NULL ,
    NAME                 VARCHAR2(64 CHAR) NOT NULL ,
    ORDER_ID             INTEGER NOT NULL ,
    DESCRIPTION          VARCHAR2(256 CHAR) NULL ,
    CONSTRAINT CALENDAR_PK PRIMARY KEY (CALENDAR_ID)
);

CREATE TABLE CALENDAR_OWNER.EVENT
(
    EVENT_ID             INTEGER NOT NULL ,
    CALENDAR_ID          INTEGER NOT NULL ,
    TASK_ID              INTEGER NULL ,
    CONSTRAINT EVENT_PK PRIMARY KEY (EVENT_ID),
    CONSTRAINT EVENT_FK2 FOREIGN KEY (CALENDAR_ID) REFERENCES CALENDAR_OWNER.CALENDAR (CALENDAR_ID) ON DELETE CASCADE
);

CREATE TABLE CALENDAR_OWNER.OCCURRENCE
(
    OCCURRENCE_ID        INTEGER NOT NULL ,
    YEAR_MONTH_DAY       TIMESTAMP(0) NOT NULL CONSTRAINT OCCURRENCE_CK1 CHECK (EXTRACT(HOUR FROM YEAR_MONTH_DAY) = 0 AND EXTRACT(MINUTE FROM YEAR_MONTH_DAY) = 0 AND EXTRACT(SECOND FROM YEAR_MONTH_DAY) = 0),
    SHIFT                VARCHAR2(5 CHAR) NOT NULL CONSTRAINT OCCURRENCE_CK2 CHECK (SHIFT IN ('OWL', 'DAY', 'SWING')),
    TITLE                VARCHAR2(128 CHAR) NOT NULL ,
    DESCRIPTION          VARCHAR2(512 CHAR) NULL ,
    LIAISON              VARCHAR2(64 CHAR) NULL ,
    DISPLAY              VARCHAR2(20 CHAR) NOT NULL CONSTRAINT OCCURRENCE_CK3 CHECK (DISPLAY IN ('SHOW', 'MORE', 'HIDE')),
    REMARK               VARCHAR2(512 CHAR) NULL ,
    EVENT_ID             INTEGER NOT NULL ,
    ORDER_ID             INTEGER NOT NULL CONSTRAINT OCCURRENCE_CK4 CHECK (ORDER_ID >= 1),
    CONSTRAINT OCCURRENCE_PK PRIMARY KEY (OCCURRENCE_ID),
    CONSTRAINT OCCURRENCE_FK1 FOREIGN KEY (EVENT_ID) REFERENCES CALENDAR_OWNER.EVENT (EVENT_ID) ON DELETE CASCADE
);

CREATE TABLE CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE
(
    OCCURRENCE_STYLE_CHOICE_ID INTEGER NOT NULL ,
    NAME                 VARCHAR2(20 CHAR) NOT NULL ,
    CSS_CLASS_NAME       VARCHAR2(20 CHAR) NOT NULL ,
    ORDER_ID             INTEGER NOT NULL ,
    CONSTRAINT OCCURRENCE_STYLE_CHOICE_PK PRIMARY KEY (OCCURRENCE_STYLE_CHOICE_ID),
    CONSTRAINT OCCURRENCE_STYLE_CHOICE_AK1 UNIQUE (NAME),
    CONSTRAINT OCCURRENCE_STYLE_CHOICE_AK2 UNIQUE (CSS_CLASS_NAME),
    CONSTRAINT OCCURRENCE_STYLE_CHOICE_AK3 UNIQUE (ORDER_ID)
);

CREATE TABLE CALENDAR_OWNER.OCCURRENCE_STYLE
(
    OCCURRENCE_STYLE_ID  INTEGER NOT NULL ,
    OCCURRENCE_ID        INTEGER NOT NULL ,
    OCCURRENCE_STYLE_CHOICE_ID INTEGER NOT NULL ,
    CONSTRAINT OCCURRENCE_STYLE_PK PRIMARY KEY (OCCURRENCE_STYLE_ID),
    CONSTRAINT OCCURRENCE_STYLE_AK1 UNIQUE (OCCURRENCE_ID,OCCURRENCE_STYLE_CHOICE_ID),
    CONSTRAINT OCCURRENCE_STYLE_FK1 FOREIGN KEY (OCCURRENCE_ID) REFERENCES CALENDAR_OWNER.OCCURRENCE (OCCURRENCE_ID) ON DELETE CASCADE,
    CONSTRAINT OCCURRENCE_STYLE_FK2 FOREIGN KEY (OCCURRENCE_STYLE_CHOICE_ID) REFERENCES CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID) ON DELETE SET NULL
);

CREATE TABLE CALENDAR_OWNER.CALENDAR_REVISION_INFO
(
    REV         NUMBER(10,0) NOT NULL,
    REVTSTMP    NUMBER(19,0) NOT NULL,
    USERNAME    VARCHAR2(64 CHAR),
    ADDRESS     VARCHAR2(64 CHAR),
    PRIMARY KEY (REV)
);

CREATE TABLE CALENDAR_OWNER.EVENT_AUD
(
    EVENT_ID             INTEGER NOT NULL ,
    REV                  NUMBER(10,0) NOT NULL ,
    REVTYPE              NUMBER(3,0) NOT NULL ,
    TASK_ID              INTEGER NULL ,
    CALENDAR_ID          INTEGER NOT NULL ,
    CONSTRAINT EVENT_AUD_PK PRIMARY KEY (EVENT_ID, REV) ,
    CONSTRAINT EVENT_AUD_FK1 FOREIGN KEY (REV) REFERENCES CALENDAR_OWNER.CALENDAR_REVISION_INFO (REV) ON DELETE CASCADE
);

CREATE TABLE CALENDAR_OWNER.OCCURRENCE_AUD
(
    OCCURRENCE_ID        INTEGER NOT NULL ,
    REV                  NUMBER(10,0) NOT NULL ,
    REVTYPE              NUMBER(3,0) NOT NULL ,
    YEAR_MONTH_DAY       TIMESTAMP(0) NOT NULL ,
    SHIFT                VARCHAR2(5 CHAR) NOT NULL ,
    TITLE                VARCHAR2(128 CHAR) NOT NULL ,
    DESCRIPTION          VARCHAR2(512 CHAR) NULL ,
    LIAISON              VARCHAR2(64 CHAR) NULL ,
    REMARK               VARCHAR2(512 CHAR) NULL ,
    DISPLAY              VARCHAR2(20 CHAR) NOT NULL ,
    EVENT_ID             INTEGER NOT NULL ,
    ORDER_ID             INTEGER NOT NULL ,
    CONSTRAINT OCCURRENCE_AUD_PK PRIMARY KEY (OCCURRENCE_ID, REV) ,
    CONSTRAINT OCCURRENCE_AUD_FK1 FOREIGN KEY (REV) REFERENCES CALENDAR_OWNER.CALENDAR_REVISION_INFO (REV) ON DELETE CASCADE
);

CREATE TABLE CALENDAR_OWNER.OCCURRENCE_STYLE_AUD
(
    OCCURRENCE_STYLE_ID        INTEGER NOT NULL ,
    REV                        NUMBER(10,0) NOT NULL ,
    REVTYPE                    NUMBER(3,0) NOT NULL ,
    OCCURRENCE_ID              INTEGER NOT NULL ,
    OCCURRENCE_STYLE_CHOICE_ID INTEGER NOT NULL ,
    CONSTRAINT OCCURRENCE_STYLE_AUD_PK PRIMARY KEY (OCCURRENCE_STYLE_ID, REV) ,
    CONSTRAINT OCCURRENCE_STYLE_AUD_FK1 FOREIGN KEY (REV) REFERENCES CALENDAR_OWNER.CALENDAR_REVISION_INFO (REV) ON DELETE CASCADE
);


-- Note: A view can be used instead of the ATLIS_TASK table.  See commented section below.
CREATE TABLE CALENDAR_OWNER.ATLIS_TASK (
    ATLIS_TASK_ID   NUMBER(10,0) NOT NULL ,
    TITLE           VARCHAR2(255 CHAR) NOT NULL ,
    DESCRIPTION     CLOB NOT NULL ,
    LIAISON         VARCHAR2(255 CHAR) NULL ,
    SCHEDULED_DATE  TIMESTAMP(6) NULL,
    CONSTRAINT ATLIS_TASK_PK PRIMARY KEY (ATLIS_TASK_ID)
);

--In lieu of a local ATLIS_TASK table a view can be used across schemas
--grant select on ATLIS7_OWNER.TASKS to CALENDAR_OWNER;
--grant select on ATLIS7_OWNER.NOTE_FIELDS to CALENDAR_OWNER;
--CREATE VIEW CALENDAR_OWNER.ATLIS_TASK (ATLIS_TASK_ID, TITLE, DESCRIPTION, LIAISON, SCHEDULED_DATE) AS
--SELECT ATLIS7_OWNER.TASKS.ID, TITLE, TEXT, CONTACT_INFO, SCHEDULED_AT
--FROM ATLIS7_OWNER.TASKS LEFT JOIN ATLIS7_OWNER.NOTE_FIELDS ON ATLIS7_OWNER.TASKS.ID = ATLIS7_OWNER.NOTE_FIELDS.TASK_ID
--WHERE NOTE_FIELD_TYPE_ID is null OR NOTE_FIELD_TYPE_ID = 100;