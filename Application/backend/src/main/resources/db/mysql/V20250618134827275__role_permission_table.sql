drop table if exists `role_permission`;
create table `role_permission` (
	role_permission_id	INT	 not null auto_increment,
	role_id	INT	not null,
	permission_id	INT	not null,
	primary key (role_permission_id)
);insert into	role_permission	(role_id,permission_id,role_permission_id)	values	("1","1","1");
insert into	role_permission	(role_id,permission_id,role_permission_id)	values	("1","2","2");
