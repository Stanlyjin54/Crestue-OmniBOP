drop table if exists `product_info`;
create table `product_info` (
	product_info_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	product_name	VARCHAR(512)	not null,
	barcode	VARCHAR(512)	,
	category	VARCHAR(512)	,
	brand	VARCHAR(512)	,
	specification	VARCHAR(512)	,
	unit	VARCHAR(512)	,
	images_resource_key	varchar(255)		default	'images_resource_key',
	serial_number	VARCHAR(512)	,
	shelf_life	DATETIME	,
	min_stock	INTEGER	,
	product_status_enum_product_status_enum_id_1	INTEGER	not null
);insert into	product_info	(unit,product_info_id,product_status_enum_product_status_enum_id_1,specification,min_stock,serial_number,category,barcode,brand,product_name,shelf_life)	values	("台","1","1","256GB","10","SN2023001","手机","6901234567890","苹果","苹果手机","2025-12-31 00:00:00.0");
insert into	product_info	(unit,product_info_id,product_status_enum_product_status_enum_id_1,specification,min_stock,serial_number,category,barcode,brand,product_name,shelf_life)	values	("台","2","1","15.6寸","5","SN2023002","电脑","6900987654321","联想","联想笔记本","2026-06-30 00:00:00.0");
insert into	product_info	(unit,product_info_id,product_status_enum_product_status_enum_id_1,specification,min_stock,serial_number,category,barcode,brand,product_name,shelf_life)	values	("瓶","3","2","500ml","50","SN2023003","饮料","6901111111111","可口可乐","可口可乐","2024-04-15 00:00:00.0");
insert into	product_info	(unit,product_info_id,product_status_enum_product_status_enum_id_1,specification,min_stock,serial_number,category,barcode,brand,product_name,shelf_life)	values	("双","4","1","42码","20","SN2023004","鞋类","6902222222222","耐克","耐克运动鞋","2027-01-01 00:00:00.0");
insert into	product_info	(unit,product_info_id,product_status_enum_product_status_enum_id_1,specification,min_stock,serial_number,category,barcode,brand,product_name,shelf_life)	values	("个","5","1","无线","15","SN2023005","耳机","6903333333333","华为","华为耳机","2025-08-20 00:00:00.0");
insert into	product_info	(unit,product_info_id,product_status_enum_product_status_enum_id_1,specification,min_stock,serial_number,category,barcode,brand,product_name,shelf_life)	values	("台","6","1","55寸","8","SN2023006","家电","6904444444444","三星","三星电视","2028-03-15 00:00:00.0");
