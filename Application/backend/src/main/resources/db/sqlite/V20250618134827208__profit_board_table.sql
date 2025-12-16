drop table if exists `profit_board`;
create table `profit_board` (
	profit_board_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	target_type	VARCHAR(512)	not null,
	target_id	INTEGER	not null,
	profit_amount	DECIMAL(18,6)	not null,
	commission	DECIMAL(18,6)	,
	shipping_fee	DECIMAL(18,6)	,
	stat_date	DATETIME	not null
);insert into	profit_board	(shipping_fee,stat_date,target_type,profit_board_id,commission,target_id,profit_amount)	values	("500.0","2024-05-31 00:00:00.0","product_info","1","2000.0","1","12000.0");
insert into	profit_board	(shipping_fee,stat_date,target_type,profit_board_id,commission,target_id,profit_amount)	values	("600.0","2024-05-31 00:00:00.0","product_info","2","2500.0","2","15000.0");
insert into	profit_board	(shipping_fee,stat_date,target_type,profit_board_id,commission,target_id,profit_amount)	values	("300.0","2024-05-31 00:00:00.0","customer_info","3","1200.0","1","8000.0");
insert into	profit_board	(shipping_fee,stat_date,target_type,profit_board_id,commission,target_id,profit_amount)	values	("350.0","2024-05-31 00:00:00.0","customer_info","4","1300.0","2","9000.0");
insert into	profit_board	(shipping_fee,stat_date,target_type,profit_board_id,commission,target_id,profit_amount)	values	("450.0","2024-05-31 00:00:00.0","warehouse_info","5","1800.0","1","11000.0");
insert into	profit_board	(shipping_fee,stat_date,target_type,profit_board_id,commission,target_id,profit_amount)	values	("400.0","2024-05-31 00:00:00.0","warehouse_info","6","1700.0","2","10000.0");
