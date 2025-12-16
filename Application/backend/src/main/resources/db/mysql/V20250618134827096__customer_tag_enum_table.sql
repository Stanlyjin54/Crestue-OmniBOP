drop table if exists `customer_tag_enum`;
create table `customer_tag_enum` (
	customer_tag_enum_id	INT	 not null auto_increment,
	tag_name	VARCHAR(512)	not null,
	is_ai_generated	BOOL	,
	primary key (customer_tag_enum_id)
);insert into	customer_tag_enum	(customer_tag_enum_id,is_ai_generated,tag_name)	values	("1","false","高活跃");
insert into	customer_tag_enum	(customer_tag_enum_id,is_ai_generated,tag_name)	values	("2","true","待跟进");
