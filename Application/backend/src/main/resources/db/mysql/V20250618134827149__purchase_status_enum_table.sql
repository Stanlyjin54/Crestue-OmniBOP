drop table if exists `purchase_status_enum`;
create table `purchase_status_enum` (
	purchase_status_enum_id	INT	 not null auto_increment,
	status_name	VARCHAR(512)	not null,
	primary key (purchase_status_enum_id)
);insert into	purchase_status_enum	(purchase_status_enum_id,status_name)	values	("1","待采购");
insert into	purchase_status_enum	(purchase_status_enum_id,status_name)	values	("2","已下单");
insert into	purchase_status_enum	(purchase_status_enum_id,status_name)	values	("3","已到货");
insert into	purchase_status_enum	(purchase_status_enum_id,status_name)	values	("4","已完成");
