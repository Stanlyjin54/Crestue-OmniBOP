drop table if exists `purchase_info`;
create table `purchase_info` (
	purchase_info_id	INT	 not null auto_increment,
	purchase_no	VARCHAR(512)	not null,
	user_info_user_info_id_1	INT	not null,
	supplier	VARCHAR(512)	,
	total_amount	DECIMAL(18,6)	not null,
	purchase_date	DATETIME	not null,
	purchase_status_enum_purchase_status_enum_id_1	INT	not null,
	primary key (purchase_info_id)
);insert into	purchase_info	(purchase_no,purchase_info_id,purchase_status_enum_purchase_status_enum_id_1,user_info_user_info_id_1,total_amount,supplier,purchase_date)	values	("PUR20230001","1","2","1","5000.0","供应商A","2024-05-20 08:00:00");
insert into	purchase_info	(purchase_no,purchase_info_id,purchase_status_enum_purchase_status_enum_id_1,user_info_user_info_id_1,total_amount,supplier,purchase_date)	values	("PUR20230002","2","3","2","8000.0","供应商B","2024-05-22 09:30:00");
insert into	purchase_info	(purchase_no,purchase_info_id,purchase_status_enum_purchase_status_enum_id_1,user_info_user_info_id_1,total_amount,supplier,purchase_date)	values	("PUR20230003","3","1","3","3000.0","供应商C","2024-05-25 14:00:00");
insert into	purchase_info	(purchase_no,purchase_info_id,purchase_status_enum_purchase_status_enum_id_1,user_info_user_info_id_1,total_amount,supplier,purchase_date)	values	("PUR20230004","4","4","4","6000.0","供应商D","2024-05-28 10:15:00");
insert into	purchase_info	(purchase_no,purchase_info_id,purchase_status_enum_purchase_status_enum_id_1,user_info_user_info_id_1,total_amount,supplier,purchase_date)	values	("PUR20230005","5","2","5","7000.0","供应商E","2024-05-30 16:45:00");
insert into	purchase_info	(purchase_no,purchase_info_id,purchase_status_enum_purchase_status_enum_id_1,user_info_user_info_id_1,total_amount,supplier,purchase_date)	values	("PUR20230006","6","3","6","9000.0","供应商F","2024-06-01 11:20:00");
