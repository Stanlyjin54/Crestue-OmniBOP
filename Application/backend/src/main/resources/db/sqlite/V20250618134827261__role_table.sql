drop table if exists `role`;
create table `role` (
	role_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	role_name	VARCHAR(512)	not null,
	role_desc	VARCHAR(512)	,
	create_time	DATETIME	
);insert into	role	(role_name,create_time,role_id,role_desc)	values	("管理员","2024-06-01 10:00:00.0 00:00:00.0","1","系统超级管理员");
insert into	role	(role_name,create_time,role_id,role_desc)	values	("普通用户","2024-06-01 10:05:00.0 00:00:00.0","2","一般操作员");
