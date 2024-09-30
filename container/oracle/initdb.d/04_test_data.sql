alter session set container = XEPDB1;

-- Populate Test ATLIS Task
INSERT INTO CALENDAR_OWNER.ATLIS_TASK(ATLIS_TASK_ID, TITLE, DESCRIPTION, LIAISON, SCHEDULED_DATE) VALUES(1, 'Test Task', TO_CLOB('Test Description'), 'tester', sysdate);