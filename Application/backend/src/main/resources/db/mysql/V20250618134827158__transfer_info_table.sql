drop table if exists `transfer_info`;
create table `transfer_info` (
	transfer_info_id	INT	 not null auto_increment,
	from_warehouse_info_warehouse_info_id_1	INT	not null,
	to_warehouse_info_warehouse_info_id_2	INT	not null,
	product_info_product_info_id_1	INT	not null,
	quantity	INT	not null,
	transfer_status_enum_transfer_status_enum_id_1	INT	not null,
	apply_user_info_user_info_id_1	INT	not null,
	confirm_user_info_user_info_id_2	INT	,
	apply_date	DATETIME	not null,
	confirm_date	DATETIME	,
	primary key (transfer_info_id)
);insert into	transfer_info	(apply_date,transfer_status_enum_transfer_status_enum_id_1,quantity,to_warehouse_info_warehouse_info_id_2,confirm_date,apply_user_info_user_info_id_1,confirm_user_info_user_info_id_2,product_info_product_info_id_1,from_warehouse_info_warehouse_info_id_1,transfer_info_id)	values	("2024-05-15 09:00:00","1","20","2","2024-05-16 10:00:00","1","2","1","1","1");
insert into	transfer_info	(apply_date,transfer_status_enum_transfer_status_enum_id_1,quantity,to_warehouse_info_warehouse_info_id_2,confirm_date,apply_user_info_user_info_id_1,confirm_user_info_user_info_id_2,product_info_product_info_id_1,from_warehouse_info_warehouse_info_id_1,transfer_info_id)	values	("2024-05-18 14:30:00","2","10","3","2024-05-19 15:00:00","2","3","2","2","2");
insert into	transfer_info	(apply_date,transfer_status_enum_transfer_status_enum_id_1,quantity,to_warehouse_info_warehouse_info_id_2,apply_user_info_user_info_id_1,product_info_product_info_id_1,from_warehouse_info_warehouse_info_id_1,transfer_info_id)	values	("2024-05-20 08:45:00","3","50","1","3","3","3","3");
insert into	transfer_info	(apply_date,transfer_status_enum_transfer_status_enum_id_1,quantity,to_warehouse_info_warehouse_info_id_2,confirm_date,apply_user_info_user_info_id_1,confirm_user_info_user_info_id_2,product_info_product_info_id_1,from_warehouse_info_warehouse_info_id_1,transfer_info_id)	values	("2024-05-22 13:00:00","2","30","5","2024-05-23 14:00:00","4","5","4","4","4");
insert into	transfer_info	(apply_date,transfer_status_enum_transfer_status_enum_id_1,quantity,to_warehouse_info_warehouse_info_id_2,confirm_date,apply_user_info_user_info_id_1,confirm_user_info_user_info_id_2,product_info_product_info_id_1,from_warehouse_info_warehouse_info_id_1,transfer_info_id)	values	("2024-05-25 10:10:00","1","15","6","2024-05-26 11:20:00","5","6","5","5","5");
insert into	transfer_info	(apply_date,transfer_status_enum_transfer_status_enum_id_1,quantity,to_warehouse_info_warehouse_info_id_2,apply_user_info_user_info_id_1,product_info_product_info_id_1,from_warehouse_info_warehouse_info_id_1,transfer_info_id)	values	("2024-05-28 16:30:00","3","25","4","6","6","6","6");
