drop table if exists `business_opportunity`;
create table `business_opportunity` (
	business_opportunity_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	customer_info_customer_info_id_1	INTEGER	not null,
	opportunity_name	VARCHAR(512)	not null,
	status	VARCHAR(512)	not null,
	follow_up_suggestion	ai	
);insert into	business_opportunity	(customer_info_customer_info_id_1,opportunity_name,business_opportunity_id,status)	values	("1","大客户合作","1","进行中");
insert into	business_opportunity	(customer_info_customer_info_id_1,opportunity_name,business_opportunity_id,status)	values	("2","新品推广","2","待跟进");
insert into	business_opportunity	(customer_info_customer_info_id_1,opportunity_name,business_opportunity_id,status)	values	("3","续约谈判","3","已完成");
insert into	business_opportunity	(customer_info_customer_info_id_1,opportunity_name,business_opportunity_id,status)	values	("4","渠道拓展","4","进行中");
insert into	business_opportunity	(customer_info_customer_info_id_1,opportunity_name,business_opportunity_id,status)	values	("5","价格调整","5","待跟进");
insert into	business_opportunity	(customer_info_customer_info_id_1,opportunity_name,business_opportunity_id,status)	values	("6","售后服务","6","已完成");
