drop table if exists `procurement_suggestion`;
create table `procurement_suggestion` (
	procurement_suggestion_id	INT	 not null auto_increment,
	product_info_product_info_id_1	INT	not null,
	suggest_quantity	INT	not null,
	reason	VARCHAR(512)	,
	suggest_date	DATETIME	not null,
	primary key (procurement_suggestion_id)
);insert into	procurement_suggestion	(reason,product_info_product_info_id_1,procurement_suggestion_id,suggest_date,suggest_quantity)	values	("库存低于安全库存，建议补货","1","1","2024-06-01","50");
insert into	procurement_suggestion	(reason,product_info_product_info_id_1,procurement_suggestion_id,suggest_date,suggest_quantity)	values	("新品推广需增加库存","2","2","2024-06-01","30");
insert into	procurement_suggestion	(reason,product_info_product_info_id_1,procurement_suggestion_id,suggest_date,suggest_quantity)	values	("临期商品库存补充","3","3","2024-06-01","100");
insert into	procurement_suggestion	(reason,product_info_product_info_id_1,procurement_suggestion_id,suggest_date,suggest_quantity)	values	("季节性需求增加","4","4","2024-06-01","40");
insert into	procurement_suggestion	(reason,product_info_product_info_id_1,procurement_suggestion_id,suggest_date,suggest_quantity)	values	("促销活动准备","5","5","2024-06-01","25");
insert into	procurement_suggestion	(reason,product_info_product_info_id_1,procurement_suggestion_id,suggest_date,suggest_quantity)	values	("新品上市备货","6","6","2024-06-01","15");
