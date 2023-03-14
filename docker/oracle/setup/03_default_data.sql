alter session set container = XEPDB1;

-- Populate Calendars
INSERT INTO CALENDAR_OWNER.CALENDAR (CALENDAR_ID, NAME, DESCRIPTION, ORDER_ID) VALUES (1, 'CEBAF', 'Primary CEBAF Calendar', 1);
INSERT INTO CALENDAR_OWNER.CALENDAR (CALENDAR_ID, NAME, DESCRIPTION, ORDER_ID) VALUES (2, 'LERF', 'FEL Facility', 2);
INSERT INTO CALENDAR_OWNER.CALENDAR (CALENDAR_ID, NAME, DESCRIPTION, ORDER_ID) VALUES (3, 'ALTERNATE', 'Alternate CEBAF Universe', 3);
INSERT INTO CALENDAR_OWNER.CALENDAR (CALENDAR_ID, NAME, DESCRIPTION, ORDER_ID) VALUES (4, 'ALTERNATE #2', 'Alternate CEBAF Universe #2', 4);

-- Populate Style Choices
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (1, 'Tentative Plan', 'tentative', 1);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (2, 'Major Event', 'major', 2);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (3, 'Incomplete Task', 'incomplete', 3);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (4, 'Lock Up', 'lock', 4);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (5, 'Utility Interruption', 'utility', 5);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (6, 'Radcon Group', 'radcon', 6);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (7, 'Special Support', 'support', 7);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (8, 'Subset', 'subset', 8);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (9, 'New/Changed', 'changed', 9);
INSERT INTO CALENDAR_OWNER.OCCURRENCE_STYLE_CHOICE (OCCURRENCE_STYLE_CHOICE_ID, NAME, CSS_CLASS_NAME, ORDER_ID) VALUES (10, 'LERF', 'lerf', 10);