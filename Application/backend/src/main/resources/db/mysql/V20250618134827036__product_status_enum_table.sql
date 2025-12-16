drop table if exists `product_status_enum`;
create table `product_status_enum` (
	product_status_enum_id	INT	 not null auto_increment,
	status_name	VARCHAR(512)	not null,
	primary key (product_status_enum_id)
);insert into	product_status_enum	(product_status_enum_id,status_name)	values	("1","正常");
insert into	product_status_enum	(product_status_enum_id,status_name)	values	("2","临期");
insert into	product_status_enum	(product_status_enum_id,status_name)	values	("3","停售");
