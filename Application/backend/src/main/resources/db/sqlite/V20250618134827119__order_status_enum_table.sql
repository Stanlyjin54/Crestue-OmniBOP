drop table if exists `order_status_enum`;
create table `order_status_enum` (
	order_status_enum_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	status_name	VARCHAR(512)	not null
);insert into	order_status_enum	(order_status_enum_id,status_name)	values	("1","待付款");
insert into	order_status_enum	(order_status_enum_id,status_name)	values	("2","已支付");
insert into	order_status_enum	(order_status_enum_id,status_name)	values	("3","待发货");
insert into	order_status_enum	(order_status_enum_id,status_name)	values	("4","已发货");
insert into	order_status_enum	(order_status_enum_id,status_name)	values	("5","待收货");
insert into	order_status_enum	(order_status_enum_id,status_name)	values	("6","已完成");
insert into	order_status_enum	(order_status_enum_id,status_name)	values	("7","已取消");
insert into	order_status_enum	(order_status_enum_id,status_name)	values	("8","已超时");
