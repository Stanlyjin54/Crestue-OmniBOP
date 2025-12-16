drop table if exists `transfer_status_enum`;
create table `transfer_status_enum` (
	transfer_status_enum_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	status_name	VARCHAR(512)	not null
);insert into	transfer_status_enum	(transfer_status_enum_id,status_name)	values	("1","待确认");
insert into	transfer_status_enum	(transfer_status_enum_id,status_name)	values	("2","已确认");
insert into	transfer_status_enum	(transfer_status_enum_id,status_name)	values	("3","已完成");
