drop table if exists `ad_recommend_info`;
create table `ad_recommend_info` (
	ad_recommend_info_id	INT	 not null auto_increment,
	ad_title	VARCHAR(512)	not null,
	ad_image_resource_key	varchar(255)	not null	default	'ad_image_resource_key',
	ad_url	VARCHAR(512)	,
	cpc_fee	DECIMAL(18,6)	,
	primary key (ad_recommend_info_id)
);insert into	ad_recommend_info	(ad_recommend_info_id,ad_url,cpc_fee,ad_title)	values	("1","https://example.com/summer-promo","2.5","夏季大促销");
insert into	ad_recommend_info	(ad_recommend_info_id,ad_url,cpc_fee,ad_title)	values	("2","https://example.com/new-arrival","3.0","新品上市");
insert into	ad_recommend_info	(ad_recommend_info_id,ad_url,cpc_fee,ad_title)	values	("3","https://example.com/limited-offer","1.8","限时折扣");
insert into	ad_recommend_info	(ad_recommend_info_id,ad_url,cpc_fee,ad_title)	values	("4","https://example.com/member-only","2.2","会员专享");
insert into	ad_recommend_info	(ad_recommend_info_id,ad_url,cpc_fee,ad_title)	values	("5","https://example.com/holiday-sale","2.9","节日特惠");
insert into	ad_recommend_info	(ad_recommend_info_id,ad_url,cpc_fee,ad_title)	values	("6","https://example.com/recommendation","3.1","新品推荐");
