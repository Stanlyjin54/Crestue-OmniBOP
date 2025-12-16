drop table if exists `rfm_level_enum`;
create table `rfm_level_enum` (
	rfm_level_enum_id	INT	 not null auto_increment,
	level_name	VARCHAR(512)	not null,
	primary key (rfm_level_enum_id)
);insert into	rfm_level_enum	(level_name,rfm_level_enum_id)	values	("高价值","1");
insert into	rfm_level_enum	(level_name,rfm_level_enum_id)	values	("潜力","2");
insert into	rfm_level_enum	(level_name,rfm_level_enum_id)	values	("沉睡","3");
