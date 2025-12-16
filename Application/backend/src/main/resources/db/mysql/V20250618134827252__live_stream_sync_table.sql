drop table if exists `live_stream_sync`;
create table `live_stream_sync` (
	live_stream_sync_id	INT	 not null auto_increment,
	order_info_order_info_id_1	INT	not null,
	platform	VARCHAR(512)	not null,
	sync_time	DATETIME	not null,
	primary key (live_stream_sync_id)
);insert into	live_stream_sync	(sync_time,live_stream_sync_id,platform,order_info_order_info_id_1)	values	("2024-06-01 10:10:00","1","抖音","1");
insert into	live_stream_sync	(sync_time,live_stream_sync_id,platform,order_info_order_info_id_1)	values	("2024-06-02 11:40:00","2","快手","2");
insert into	live_stream_sync	(sync_time,live_stream_sync_id,platform,order_info_order_info_id_1)	values	("2024-06-03 15:00:00","3","淘宝直播","3");
insert into	live_stream_sync	(sync_time,live_stream_sync_id,platform,order_info_order_info_id_1)	values	("2024-06-04 09:30:00","4","京东直播","4");
insert into	live_stream_sync	(sync_time,live_stream_sync_id,platform,order_info_order_info_id_1)	values	("2024-06-05 16:20:00","5","拼多多直播","5");
insert into	live_stream_sync	(sync_time,live_stream_sync_id,platform,order_info_order_info_id_1)	values	("2024-06-06 13:10:00","6","小红书直播","6");
