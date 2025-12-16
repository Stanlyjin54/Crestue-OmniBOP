drop table if exists `user_role`;
create table `user_role` (
	user_role_id	INT	 not null auto_increment,
	user_info_id	INT	not null,
	role_id	INT	not null,
	primary key (user_role_id)
);insert into	user_role	(user_role_id,role_id,user_info_id)	values	("1","1","1");
insert into	user_role	(user_role_id,role_id,user_info_id)	values	("2","2","2");
