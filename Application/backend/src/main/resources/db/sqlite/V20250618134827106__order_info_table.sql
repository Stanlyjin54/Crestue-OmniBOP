drop table if exists `order_info`;
create table `order_info` (
	order_info_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	order_no	VARCHAR(512)	not null,
	customer_info_customer_info_id_1	INTEGER	not null,
	user_info_user_info_id_1	INTEGER	not null,
	order_status_enum_order_status_enum_id_1	INTEGER	not null,
	total_amount	DECIMAL(18,6)	not null,
	profit_amount	DECIMAL(18,6)	,
	commission	DECIMAL(18,6)	,
	shipping_fee	DECIMAL(18,6)	,
	order_date	DATETIME	not null,
	general_orders	VARCHAR(64)	
);insert into	order_info	(order_no,shipping_fee,order_date,general_orders,order_info_id,user_info_user_info_id_1,customer_info_customer_info_id_1,total_amount,commission,profit_amount,order_status_enum_order_status_enum_id_1)	values	("ORD20230001","20.0","2024-06-01 10:00:00.0 00:00:00.0","PAY20240001","1","1","1","1500.0","50.0","300.0","2");
insert into	order_info	(order_no,shipping_fee,order_date,general_orders,order_info_id,user_info_user_info_id_1,customer_info_customer_info_id_1,total_amount,commission,profit_amount,order_status_enum_order_status_enum_id_1)	values	("ORD20230002","30.0","2024-06-02 11:30:00.0 00:00:00.0","PAY20240002","2","2","2","2500.0","80.0","500.0","3");
insert into	order_info	(order_no,shipping_fee,order_date,general_orders,order_info_id,user_info_user_info_id_1,customer_info_customer_info_id_1,total_amount,commission,profit_amount,order_status_enum_order_status_enum_id_1)	values	("ORD20230003","15.0","2024-06-03 14:45:00.0 00:00:00.0","PAY20240003","3","3","3","1200.0","40.0","200.0","4");
insert into	order_info	(order_no,shipping_fee,order_date,general_orders,order_info_id,user_info_user_info_id_1,customer_info_customer_info_id_1,total_amount,commission,profit_amount,order_status_enum_order_status_enum_id_1)	values	("ORD20230004","25.0","2024-06-04 09:20:00.0 00:00:00.0","PAY20240004","4","4","4","1800.0","60.0","350.0","5");
insert into	order_info	(order_no,shipping_fee,order_date,general_orders,order_info_id,user_info_user_info_id_1,customer_info_customer_info_id_1,total_amount,commission,profit_amount,order_status_enum_order_status_enum_id_1)	values	("ORD20230005","18.0","2024-06-05 16:10:00.0 00:00:00.0","PAY20240005","5","5","5","2200.0","70.0","400.0","1");
insert into	order_info	(order_no,shipping_fee,order_date,general_orders,order_info_id,user_info_user_info_id_1,customer_info_customer_info_id_1,total_amount,commission,profit_amount,order_status_enum_order_status_enum_id_1)	values	("ORD20230006","35.0","2024-06-06 13:00:00.0 00:00:00.0","PAY20240006","6","6","6","3000.0","90.0","600.0","6");
