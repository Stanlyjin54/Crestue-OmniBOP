drop table if exists `transfer_status_enum`;
create table `transfer_status_enum` (
	transfer_status_enum_id	INT	 not null auto_increment,
	status_name	VARCHAR(512)	not null,
	primary key (transfer_status_enum_id)
);insert into	transfer_status_enum	(transfer_status_enum_id,status_name)	values	("1","待确认");
insert into	transfer_status_enum	(transfer_status_enum_id,status_name)	values	("2","已确认");
insert into	transfer_status_enum	(transfer_status_enum_id,status_name)	values	("3","已完成");
