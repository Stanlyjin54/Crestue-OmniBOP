drop table if exists `permission_log`;
create table `permission_log` (
	permission_log_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	user_info_id	INTEGER	not null,
	action_type	VARCHAR(512)	not null,
	target_object	VARCHAR(512)	not null,
	change_content	TEXT	,
	action_time	DATETIME	not null
);insert into	permission_log	(action_time,action_type,target_object,permission_log_id,user_info_id,change_content)	values	("2024-06-01 10:10:00.0 00:00:00.0","新增角色","管理员","1","1","创建了角色：管理员");
insert into	permission_log	(action_time,action_type,target_object,permission_log_id,user_info_id,change_content)	values	("2024-06-01 10:20:00.0 00:00:00.0","分配权限","普通用户","2","2","分配了客户管理权限");
