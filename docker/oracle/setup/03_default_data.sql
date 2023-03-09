alter session set container = XEPDB1;

-- Populate Style Choices
insert into calendar_owner.occurrence_style_choice values (1, 'Tentative Plan', 'tentative', 1);
insert into calendar_owner.occurrence_style_choice values (2, 'Major Event', 'major', 2);
insert into calendar_owner.occurrence_style_choice values (3, 'Incomplete Task', 'incomplete', 3);
insert into calendar_owner.occurrence_style_choice values (4, 'Lock Up', 'lock', 4);
insert into calendar_owner.occurrence_style_choice values (5, 'Utility Interruption', 'utility', 5);
insert into calendar_owner.occurrence_style_choice values (6, 'Radcon Group', 'radcon', 6);
insert into calendar_owner.occurrence_style_choice values (7, 'Special Support', 'support', 7);
insert into calendar_owner.occurrence_style_choice values (8, 'Subset', 'subset', 8);
insert into calendar_owner.occurrence_style_choice values (9, 'New/Changed', 'changed', 9);

