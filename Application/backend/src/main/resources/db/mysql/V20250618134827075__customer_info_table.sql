drop table if exists `customer_info`;
create table `customer_info` (
	customer_info_id	INT	 not null auto_increment,
	customer_name	VARCHAR(512)	not null,
	phone	VARCHAR(64)	,
	email	VARCHAR(64)	,
	address	VARCHAR(512)	,
	rfm_level_enum_rfm_level_enum_id_1	INT	,
	customer_tag_enum_customer_tag_enum_id_1	INT	,
	wechat_id	VARCHAR(512)	,
	wechat_data_encrypted	VARCHAR(512)	,
	source	VARCHAR(512)	,
	primary key (customer_info_id)
);insert into	customer_info	(wechat_id,address,customer_info_id,phone,wechat_data_encrypted,rfm_level_enum_rfm_level_enum_id_1,customer_tag_enum_customer_tag_enum_id_1,customer_name,source,email)	values	("chengduA","四川省成都市武侯区","1","13888888888","encryptedDataA","1","1","成都客户A","电商平台","customerA@example.com");
insert into	customer_info	(wechat_id,address,customer_info_id,phone,wechat_data_encrypted,rfm_level_enum_rfm_level_enum_id_1,customer_tag_enum_customer_tag_enum_id_1,customer_name,source,email)	values	("shanghaiB","上海市浦东新区","2","13999999999","encryptedDataB","2","2","上海客户B","线下门店","customerB@example.com");
insert into	customer_info	(wechat_id,address,customer_info_id,phone,wechat_data_encrypted,rfm_level_enum_rfm_level_enum_id_1,customer_tag_enum_customer_tag_enum_id_1,customer_name,source,email)	values	("beijingC","北京市海淀区","3","13777777777","encryptedDataC","3","1","北京客户C","官网","customerC@example.com");
insert into	customer_info	(wechat_id,address,customer_info_id,phone,wechat_data_encrypted,rfm_level_enum_rfm_level_enum_id_1,customer_tag_enum_customer_tag_enum_id_1,customer_name,source,email)	values	("guangzhouD","广东省广州市天河区","4","13666666666","encryptedDataD","1","2","广州客户D","社交媒体","customerD@example.com");
insert into	customer_info	(wechat_id,address,customer_info_id,phone,wechat_data_encrypted,rfm_level_enum_rfm_level_enum_id_1,customer_tag_enum_customer_tag_enum_id_1,customer_name,source,email)	values	("shenzhenE","广东省深圳市南山区","5","13555555555","encryptedDataE","2","1","深圳客户E","推荐","customerE@example.com");
insert into	customer_info	(wechat_id,address,customer_info_id,phone,wechat_data_encrypted,rfm_level_enum_rfm_level_enum_id_1,customer_tag_enum_customer_tag_enum_id_1,customer_name,source,email)	values	("hangzhouF","浙江省杭州市滨江区","6","13444444444","encryptedDataF","3","2","杭州客户F","广告","customerF@example.com");
