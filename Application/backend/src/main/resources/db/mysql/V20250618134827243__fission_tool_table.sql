drop table if exists `fission_tool`;
create table `fission_tool` (
	fission_tool_id	INT	 not null auto_increment,
	tool_type	VARCHAR(512)	not null,
	title	VARCHAR(512)	not null,
	config	TEXT	,
	start_date	DATETIME	not null,
	end_date	DATETIME	not null,
	primary key (fission_tool_id)
);insert into	fission_tool	(end_date,tool_type,title,config,fission_tool_id,start_date)	values	("2024-06-30","邀请裂变","好友邀请赢奖励","{"reward":"优惠券","max_invites":10}","1","2024-06-01");
insert into	fission_tool	(end_date,tool_type,title,config,fission_tool_id,start_date)	values	("2024-07-05","分享裂变","分享活动得积分","{"points_per_share":5}","2","2024-06-05");
insert into	fission_tool	(end_date,tool_type,title,config,fission_tool_id,start_date)	values	("2024-07-10","抽奖裂变","参与抽奖赢大奖","{"prizes":["手机","平板","优惠券"]}","3","2024-06-10");
insert into	fission_tool	(end_date,tool_type,title,config,fission_tool_id,start_date)	values	("2024-06-30","签到裂变","连续签到送礼品","{"consecutive_days":7}","4","2024-06-01");
insert into	fission_tool	(end_date,tool_type,title,config,fission_tool_id,start_date)	values	("2024-07-15","购买裂变","购买商品送积分","{"points_per_purchase":10}","5","2024-06-15");
insert into	fission_tool	(end_date,tool_type,title,config,fission_tool_id,start_date)	values	("2024-07-20","评价裂变","评价商品得奖励","{"reward":"积分"}","6","2024-06-20");
