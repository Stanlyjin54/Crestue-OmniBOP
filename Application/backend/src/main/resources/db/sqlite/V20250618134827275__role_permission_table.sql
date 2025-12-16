drop table if exists `role_permission`;
create table `role_permission` (
	role_permission_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	role_id	INTEGER	not null,
	permission_id	INTEGER	not null
);insert into	role_permission	(role_id,permission_id,role_permission_id)	values	("1","1","1");
insert into	role_permission	(role_id,permission_id,role_permission_id)	values	("1","2","2");
