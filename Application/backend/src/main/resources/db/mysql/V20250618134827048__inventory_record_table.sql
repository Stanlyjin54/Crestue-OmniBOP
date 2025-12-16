drop table if exists `inventory_record`;
create table `inventory_record` (
	inventory_record_id	INT	 not null auto_increment,
	product_info_product_info_id_1	INT	not null,
	warehouse_info_warehouse_info_id_1	INT	not null,
	quantity	INT	not null,
	batch_number	VARCHAR(512)	,
	expire_date	DATETIME	,
	serial_number	VARCHAR(512)	,
	primary key (inventory_record_id)
);insert into	inventory_record	(expire_date,quantity,batch_number,inventory_record_id,serial_number,product_info_product_info_id_1,warehouse_info_warehouse_info_id_1)	values	("2025-12-31","100","BATCH202301","1","SN2023001A","1","1");
insert into	inventory_record	(expire_date,quantity,batch_number,inventory_record_id,serial_number,product_info_product_info_id_1,warehouse_info_warehouse_info_id_1)	values	("2026-06-30","50","BATCH202302","2","SN2023002A","2","2");
insert into	inventory_record	(expire_date,quantity,batch_number,inventory_record_id,serial_number,product_info_product_info_id_1,warehouse_info_warehouse_info_id_1)	values	("2024-04-15","200","BATCH202303","3","SN2023003A","3","3");
insert into	inventory_record	(expire_date,quantity,batch_number,inventory_record_id,serial_number,product_info_product_info_id_1,warehouse_info_warehouse_info_id_1)	values	("2027-01-01","80","BATCH202304","4","SN2023004A","4","1");
insert into	inventory_record	(expire_date,quantity,batch_number,inventory_record_id,serial_number,product_info_product_info_id_1,warehouse_info_warehouse_info_id_1)	values	("2025-08-20","60","BATCH202305","5","SN2023005A","5","2");
insert into	inventory_record	(expire_date,quantity,batch_number,inventory_record_id,serial_number,product_info_product_info_id_1,warehouse_info_warehouse_info_id_1)	values	("2028-03-15","30","BATCH202306","6","SN2023006A","6","3");
