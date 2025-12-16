drop table if exists `user_info`;
create table `user_info` (
	user_info_id	INT	 not null auto_increment,
	phone_number	VARCHAR(64)	not null,
	username	VARCHAR(512)	not null,
	password	VARCHAR(64)	not null,
	avatar_resource_key	varchar(255)		default	'avatar_resource_key',
	registration_date	DATETIME	,
	primary key (user_info_id)
);insert into	user_info	(password,registration_date,phone_number,user_info_id,username)	values	("password123","2022-01-15","13800138000","1","小飞飞");
insert into	user_info	(password,registration_date,phone_number,user_info_id,username)	values	("securePass!9","2023-03-22","13900139000","2","李晓明");
insert into	user_info	(password,registration_date,phone_number,user_info_id,username)	values	("passWord!@#","2021-11-05","13700137000","3","王磊");
insert into	user_info	(password,registration_date,phone_number,user_info_id,username)	values	("abc123456","2020-07-19","13600136000","4","赵娜");
insert into	user_info	(password,registration_date,phone_number,user_info_id,username)	values	("qwerty789","2022-09-30","13500135000","5","陈强");
insert into	user_info	(password,registration_date,phone_number,user_info_id,username)	values	("zxcvbn123","2023-05-10","13400134000","6","孙婷");
