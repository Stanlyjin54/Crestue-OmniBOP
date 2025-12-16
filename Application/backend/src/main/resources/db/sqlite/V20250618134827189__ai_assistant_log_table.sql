drop table if exists `ai_assistant_log`;
create table `ai_assistant_log` (
	ai_assistant_log_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	user_info_user_info_id_1	INTEGER	not null,
	query_text	VARCHAR(512)	not null,
	response_text	TEXT	not null,
	query_time	DATETIME	not null
);insert into	ai_assistant_log	(ai_assistant_log_id,user_info_user_info_id_1,response_text,query_text,query_time)	values	("1","1","建议及时补货，避免断货影响销售。","库存不足怎么办？","2024-06-01 10:05:00.0 00:00:00.0");
insert into	ai_assistant_log	(ai_assistant_log_id,user_info_user_info_id_1,response_text,query_text,query_time)	values	("2","2","通过定期促销和会员活动提升客户活跃度。","如何提升客户活跃度？","2024-06-02 11:35:00.0 00:00:00.0");
insert into	ai_assistant_log	(ai_assistant_log_id,user_info_user_info_id_1,response_text,query_text,query_time)	values	("3","3","订单正在处理中，预计两天内发货。","订单状态查询","2024-06-03 14:50:00.0 00:00:00.0");
insert into	ai_assistant_log	(ai_assistant_log_id,user_info_user_info_id_1,response_text,query_text,query_time)	values	("4","4","采购流程包括需求确认、下单、验收和付款。","采购流程介绍","2024-06-04 09:25:00.0 00:00:00.0");
insert into	ai_assistant_log	(ai_assistant_log_id,user_info_user_info_id_1,response_text,query_text,query_time)	values	("5","5","请联系售后部门，填写退货申请表。","如何处理退货？","2024-06-05 16:15:00.0 00:00:00.0");
insert into	ai_assistant_log	(ai_assistant_log_id,user_info_user_info_id_1,response_text,query_text,query_time)	values	("6","6","调拨申请需填写申请单并经仓库确认。","调拨申请流程","2024-06-06 13:05:00.0 00:00:00.0");
