drop table if exists `permission`;
create table `permission` (
	permission_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	permission_name	VARCHAR(512)	not null,
	permission_desc	VARCHAR(512)	,
	feature_code	VARCHAR(512)	not null
);insert into	permission	(permission_desc,permission_id,feature_code,permission_name)	values	("管理库存、采购等","1","inventory_manage","进销存管理");
insert into	permission	(permission_desc,permission_id,feature_code,permission_name)	values	("查看和编辑客户","2","customer_manage","客户管理");
