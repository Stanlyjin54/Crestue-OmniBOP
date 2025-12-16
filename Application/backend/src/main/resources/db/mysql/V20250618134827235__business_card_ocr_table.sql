drop table if exists `business_card_ocr`;
create table `business_card_ocr` (
	business_card_ocr_id	INT	 not null auto_increment,
	customer_info_customer_info_id_1	INT	not null,
	card_image_resource_key	varchar(255)	not null	default	'card_image_resource_key',
	ocr_result	TEXT	not null,
	primary key (business_card_ocr_id)
);insert into	business_card_ocr	(customer_info_customer_info_id_1,business_card_ocr_id,ocr_result)	values	("1","1","姓名：张三
电话：13800138000
公司：成都科技有限公司");
insert into	business_card_ocr	(customer_info_customer_info_id_1,business_card_ocr_id,ocr_result)	values	("2","2","姓名：李四
电话：13900139000
公司：上海贸易有限公司");
insert into	business_card_ocr	(customer_info_customer_info_id_1,business_card_ocr_id,ocr_result)	values	("3","3","姓名：王五
电话：13700137000
公司：北京咨询有限公司");
insert into	business_card_ocr	(customer_info_customer_info_id_1,business_card_ocr_id,ocr_result)	values	("4","4","姓名：赵六
电话：13600136000
公司：广州设计有限公司");
insert into	business_card_ocr	(customer_info_customer_info_id_1,business_card_ocr_id,ocr_result)	values	("5","5","姓名：钱七
电话：13500135000
公司：深圳制造有限公司");
insert into	business_card_ocr	(customer_info_customer_info_id_1,business_card_ocr_id,ocr_result)	values	("6","6","姓名：孙八
电话：13400134000
公司：杭州服务有限公司");
