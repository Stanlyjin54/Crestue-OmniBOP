drop table if exists `order_item`;
create table `order_item` (
	order_item_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	order_info_order_info_id_1	INTEGER	not null,
	product_info_product_info_id_1	INTEGER	not null,
	quantity	INTEGER	not null,
	unit_price	DECIMAL(18,6)	not null,
	total_price	DECIMAL(18,6)	not null
);insert into	order_item	(order_item_id,quantity,total_price,product_info_product_info_id_1,unit_price,order_info_order_info_id_1)	values	("1","2","1500.0","1","750.0","1");
insert into	order_item	(order_item_id,quantity,total_price,product_info_product_info_id_1,unit_price,order_info_order_info_id_1)	values	("2","1","200.0","3","200.0","1");
insert into	order_item	(order_item_id,quantity,total_price,product_info_product_info_id_1,unit_price,order_info_order_info_id_1)	values	("3","1","2500.0","2","2500.0","2");
insert into	order_item	(order_item_id,quantity,total_price,product_info_product_info_id_1,unit_price,order_info_order_info_id_1)	values	("4","3","1200.0","4","400.0","3");
insert into	order_item	(order_item_id,quantity,total_price,product_info_product_info_id_1,unit_price,order_info_order_info_id_1)	values	("5","2","1800.0","5","900.0","4");
insert into	order_item	(order_item_id,quantity,total_price,product_info_product_info_id_1,unit_price,order_info_order_info_id_1)	values	("6","1","2200.0","6","2200.0","5");
