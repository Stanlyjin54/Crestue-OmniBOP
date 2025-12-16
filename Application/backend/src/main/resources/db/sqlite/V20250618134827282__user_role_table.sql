drop table if exists `user_role`;
create table `user_role` (
	user_role_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	user_info_id	INTEGER	not null,
	role_id	INTEGER	not null
);insert into	user_role	(user_role_id,role_id,user_info_id)	values	("1","1","1");
insert into	user_role	(user_role_id,role_id,user_info_id)	values	("2","2","2");
