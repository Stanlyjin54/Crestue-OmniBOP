drop table if exists `warehouse_info`;
create table `warehouse_info` (
	warehouse_info_id	INT	 not null auto_increment,
	warehouse_name	VARCHAR(512)	not null,
	location	VARCHAR(512)	,
	primary key (warehouse_info_id)
);insert into	warehouse_info	(warehouse_name,location,warehouse_info_id)	values	("成都主仓库","四川省成都市高新区天府大道100号","1");
insert into	warehouse_info	(warehouse_name,location,warehouse_info_id)	values	("上海分仓库","上海市浦东新区东方路200号","2");
insert into	warehouse_info	(warehouse_name,location,warehouse_info_id)	values	("北京仓库","北京市朝阳区建国路300号","3");
insert into	warehouse_info	(warehouse_name,location,warehouse_info_id)	values	("广州仓库","广东省广州市天河区体育西路400号","4");
insert into	warehouse_info	(warehouse_name,location,warehouse_info_id)	values	("深圳仓库","广东省深圳市南山区科技园500号","5");
insert into	warehouse_info	(warehouse_name,location,warehouse_info_id)	values	("杭州仓库","浙江省杭州市滨江区江南大道600号","6");
