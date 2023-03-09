-- delete all occurrences from calendar 1 between date
delete from 
--select * from 
occurrence where occurrence_id in (select occurrence_id from occurrence left join event using (event_id) 
where calendar_id = 1 and 
year_month_day between to_date('2018-04-07','yyyy-mm-dd') and to_date('2018-08-30','yyyy-mm-dd'));

-- delete any hanging events (events with no occurrences)
delete from
--select * from 
event where event_id in (select event_id from event left outer join occurrence using(event_id) where occurrence_id is null);

-- Move all events with incidents between date from calendar 2 to calendar 1
update event set calendar_id = 1 
--select * from event
where event_id in (select event_id from event left join occurrence using(event_id) where calendar_id = 2 and year_month_day between to_date('2018-04-07','yyyy-mm-dd') and to_date('2018-08-30','yyyy-mm-dd'));



