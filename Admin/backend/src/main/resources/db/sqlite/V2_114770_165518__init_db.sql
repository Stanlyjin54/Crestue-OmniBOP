drop table if exists `system_config`;
create table `system_config` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `name` 	 VARCHAR(512) not null  ,
	 `chinese_name` 	 VARCHAR(512)   ,
	 `description` 	 VARCHAR(512)   ,
	 `content` 	 VARCHAR(512)   ,
	 `remark` 	 VARCHAR(512)   ,
	 `type` 	 VARCHAR(512)   
 );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("order.expire.time","订单多少时间未支付自动关闭，单位分钟","","30","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("order.automatic.receiptTime","订单多长时间自动确认收货，单位 天","","5","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("order.automatic.schedule.corn","订单自动确认收货的定时任务的执行周期","","*/10 * * * * ?","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wechat.app-id","微信小程序appid","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wechat.app-secret","微信小程序secret","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wechat.is-register","默认配置不需要修改","","0","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.pay-back-url","微信支付回调url","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.mch-serial-no","微信支付序列号","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.app-id","微信支付对应的appid","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.mch-id","微信支付商户号","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.api-v3-key","微信支付 apiv3 key","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.is_test","微信支付是否开启测试,开启测试后，支付金额统一为0.01，正式上线请关闭（true/false）","","true","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.is_test_amount","微信支付测试金额","","0.01","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.mock_enable","是否开启mock支付（没有申请支付的情况下，true/false）","","true","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.wechat_enable","是否开启微信支付(true/false)","","false","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.mp-app-id","微信公众号的APPID","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wechat.mp-redirect_uri","重定向的url","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.mp-app-secret","","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wechat.mp-frontend_redirect_uri","前端的重定向地址","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("order.orderStatusField","订单表中的订单状态字段","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("order.refundStatusValue","系统订单中退款状态与业务订单表退款状态的中的映射关系","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.private-key","微信支付私钥内容 （apiclient_key.pem）","","","","" );
insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) values ("wx.pay.order_pre_key","支付商户号的统一前缀(请谨慎修改,该值变化后将使存量的支付订单不可用)","","order-114770-","","" );

drop table if exists `delayed_tasks`;
create table `delayed_tasks` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `task_type` 	 VARCHAR(512) not null  ,
	 `task_data` 	 VARCHAR(512) not null  ,
	 `status` 	 VARCHAR(512) not null  ,
	 `execute_time` 	 DATETIME not null  ,
	 `create_time` 	 DATETIME  DEFAULT (CURRENT_TIMESTAMP) ,
	 `update_time` 	 DATETIME not null  
 );


drop table if exists `general_orders`;
create table `general_orders` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `order_no` 	 VARCHAR(512) not null  ,
	 `user_id` 	 INTEGER not null  ,
	 `op_id` 	 VARCHAR(512)   ,
	 `order_type` 	 VARCHAR(512)   ,
	 `order_status` 	 VARCHAR(512) not null  ,
	 `pay_status` 	 VARCHAR(512) not null  ,
	 `delivery_status` 	 VARCHAR(512)   ,
	 `products_id` 	 INTEGER not null  ,
	 `quality` 	 INTEGER not null  ,
	 `total_amount` 	 DECIMAL(18,6) not null  ,
	 `discount_amount` 	 DECIMAL(18,6)   ,
	 `shipping_fee` 	 DECIMAL(18,6)   ,
	 `actual_amount` 	 DECIMAL(18,6) not null  ,
	 `payment_channel` 	 VARCHAR(512)   ,
	 `payment_order_id` 	 VARCHAR(512)   ,
	 `payment_time` 	 DATETIME   ,
	 `payment_amount` 	 DECIMAL(18,6)   ,
	 `shipping_method` 	 VARCHAR(512)   ,
	 `shipping_address` 	 VARCHAR(512)   ,
	 `shipping_company` 	 VARCHAR(512)   ,
	 `tracking_number` 	 VARCHAR(512)   ,
	 `tel` 	 VARCHAR(512)   ,
	 `recipient` 	 VARCHAR(512)   ,
	 `create_time` 	 DATETIME  DEFAULT (CURRENT_TIMESTAMP) ,
	 `update_time` 	 DATETIME not null  ,
	 `cancel_time` 	 DATETIME   ,
	 `complete_time` 	 DATETIME   ,
	 `deliver_time` 	 DATETIME   ,
	 `refund_id` 	 VARCHAR(512)   ,
	 `refund_no` 	 VARCHAR(512)   ,
	 `refund_type` 	 VARCHAR(512)   ,
	 `refund_amount` 	 DECIMAL(18,6)   ,
	 `refund_reason` 	 VARCHAR(512)   ,
	 `refund_status` 	 VARCHAR(512)   ,
	 `remark` 	 VARCHAR(512)   ,
	 `is_deleted` 	 INTEGER   ,
	 `table_name` 	 VARCHAR(512)   ,
	 `field_name` 	 VARCHAR(512)   
 );


drop table if exists `user_info`;
create table `user_info` (
 	 `user_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `phone_number` 	 VARCHAR(64) not null  ,
	 `username` 	 VARCHAR(512) not null  ,
	 `password` 	 VARCHAR(64) not null  ,
	 `avatar_resource_key` 	 VARCHAR(255)   ,
	 `registration_date` 	 DATETIME   
 );
insert into `user_info` (`phone_number`, `username`, `password`, `avatar_resource_key`, `registration_date` ) values ("13800138000","小飞飞","password123","0","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/girl-smile.jpg','user_info', '1', 'avatar');
insert into `user_info` (`phone_number`, `username`, `password`, `avatar_resource_key`, `registration_date` ) values ("13900139000","李晓明","securePass!9","0","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/reading-books.jpg','user_info', '2', 'avatar');
insert into `user_info` (`phone_number`, `username`, `password`, `avatar_resource_key`, `registration_date` ) values ("13700137000","王磊","passWord!@#","0","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/puppy.jpg','user_info', '3', 'avatar');
insert into `user_info` (`phone_number`, `username`, `password`, `avatar_resource_key`, `registration_date` ) values ("13600136000","赵娜","abc123456","0","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/woman-reading-book.jpg','user_info', '4', 'avatar');
insert into `user_info` (`phone_number`, `username`, `password`, `avatar_resource_key`, `registration_date` ) values ("13500135000","陈强","qwerty789","0","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/doctor-patient.jpg','user_info', '5', 'avatar');
insert into `user_info` (`phone_number`, `username`, `password`, `avatar_resource_key`, `registration_date` ) values ("13400134000","孙婷","zxcvbn123","0","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/kitty-family.jpg','user_info', '6', 'avatar');

drop table if exists `product_info`;
create table `product_info` (
 	 `product_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `product_name` 	 VARCHAR(512) not null  ,
	 `barcode` 	 VARCHAR(512)   ,
	 `category` 	 VARCHAR(512)   ,
	 `brand` 	 VARCHAR(512)   ,
	 `specification` 	 VARCHAR(512)   ,
	 `unit` 	 VARCHAR(512)   ,
	 `images_resource_key` 	 VARCHAR(255)   ,
	 `serial_number` 	 VARCHAR(512)   ,
	 `shelf_life` 	 DATETIME   ,
	 `min_stock` 	 INTEGER   ,
	 `product_status_enum_product_status_enum_id_1` 	 INTEGER not null  
 );
insert into `product_info` (`product_name`, `barcode`, `category`, `brand`, `specification`, `unit`, `images_resource_key`, `serial_number`, `shelf_life`, `min_stock`, `product_status_enum_product_status_enum_id_1` ) values ("苹果手机","6901234567890","手机","苹果","256GB","台","0","SN2023001","2025-01-01 00:00:00","10","1" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/iphone-device.jpg','product_info', '1', 'images');
insert into `product_info` (`product_name`, `barcode`, `category`, `brand`, `specification`, `unit`, `images_resource_key`, `serial_number`, `shelf_life`, `min_stock`, `product_status_enum_product_status_enum_id_1` ) values ("联想笔记本","6900987654321","电脑","联想","15.6寸","台","0","SN2023002","2025-01-01 00:00:00","5","1" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/laptop-apple.jpg','product_info', '2', 'images');
insert into `product_info` (`product_name`, `barcode`, `category`, `brand`, `specification`, `unit`, `images_resource_key`, `serial_number`, `shelf_life`, `min_stock`, `product_status_enum_product_status_enum_id_1` ) values ("可口可乐","6901111111111","饮料","可口可乐","500ml","瓶","0","SN2023003","2025-01-01 00:00:00","50","2" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/iced-beverage.jpg','product_info', '3', 'images');
insert into `product_info` (`product_name`, `barcode`, `category`, `brand`, `specification`, `unit`, `images_resource_key`, `serial_number`, `shelf_life`, `min_stock`, `product_status_enum_product_status_enum_id_1` ) values ("耐克运动鞋","6902222222222","鞋类","耐克","42码","双","0","SN2023004","2025-01-01 00:00:00","20","1" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/girl-smile.jpg','product_info', '4', 'images');
insert into `product_info` (`product_name`, `barcode`, `category`, `brand`, `specification`, `unit`, `images_resource_key`, `serial_number`, `shelf_life`, `min_stock`, `product_status_enum_product_status_enum_id_1` ) values ("华为耳机","6903333333333","耳机","华为","无线","个","0","SN2023005","2025-01-01 00:00:00","15","1" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/ai-robot.jpg','product_info', '5', 'images');
insert into `product_info` (`product_name`, `barcode`, `category`, `brand`, `specification`, `unit`, `images_resource_key`, `serial_number`, `shelf_life`, `min_stock`, `product_status_enum_product_status_enum_id_1` ) values ("三星电视","6904444444444","家电","三星","55寸","台","0","SN2023006","2025-01-01 00:00:00","8","1" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/retro-film.jpg','product_info', '6', 'images');

drop table if exists `product_status_enum`;
create table `product_status_enum` (
 	 `product_status_enum_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `status_name` 	 VARCHAR(512) not null  
 );
insert into `product_status_enum` (`status_name` ) values ("正常" );
insert into `product_status_enum` (`status_name` ) values ("临期" );
insert into `product_status_enum` (`status_name` ) values ("停售" );

drop table if exists `inventory_record`;
create table `inventory_record` (
 	 `inventory_record_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `product_info_product_info_id_1` 	 INTEGER not null  ,
	 `warehouse_info_warehouse_info_id_1` 	 INTEGER not null  ,
	 `quantity` 	 INTEGER not null  ,
	 `batch_number` 	 VARCHAR(512)   ,
	 `expire_date` 	 DATETIME   ,
	 `serial_number` 	 VARCHAR(512)   
 );
insert into `inventory_record` (`product_info_product_info_id_1`, `warehouse_info_warehouse_info_id_1`, `quantity`, `batch_number`, `expire_date`, `serial_number` ) values ("1","1","100","BATCH202301","2025-01-01 00:00:00","SN2023001A" );
insert into `inventory_record` (`product_info_product_info_id_1`, `warehouse_info_warehouse_info_id_1`, `quantity`, `batch_number`, `expire_date`, `serial_number` ) values ("2","2","50","BATCH202302","2025-01-01 00:00:00","SN2023002A" );
insert into `inventory_record` (`product_info_product_info_id_1`, `warehouse_info_warehouse_info_id_1`, `quantity`, `batch_number`, `expire_date`, `serial_number` ) values ("3","3","200","BATCH202303","2025-01-01 00:00:00","SN2023003A" );
insert into `inventory_record` (`product_info_product_info_id_1`, `warehouse_info_warehouse_info_id_1`, `quantity`, `batch_number`, `expire_date`, `serial_number` ) values ("4","1","80","BATCH202304","2025-01-01 00:00:00","SN2023004A" );
insert into `inventory_record` (`product_info_product_info_id_1`, `warehouse_info_warehouse_info_id_1`, `quantity`, `batch_number`, `expire_date`, `serial_number` ) values ("5","2","60","BATCH202305","2025-01-01 00:00:00","SN2023005A" );
insert into `inventory_record` (`product_info_product_info_id_1`, `warehouse_info_warehouse_info_id_1`, `quantity`, `batch_number`, `expire_date`, `serial_number` ) values ("6","3","30","BATCH202306","2025-01-01 00:00:00","SN2023006A" );

drop table if exists `warehouse_info`;
create table `warehouse_info` (
 	 `warehouse_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `warehouse_name` 	 VARCHAR(512) not null  ,
	 `location` 	 VARCHAR(512)   
 );
insert into `warehouse_info` (`warehouse_name`, `location` ) values ("成都主仓库","四川省成都市高新区天府大道100号" );
insert into `warehouse_info` (`warehouse_name`, `location` ) values ("上海分仓库","上海市浦东新区东方路200号" );
insert into `warehouse_info` (`warehouse_name`, `location` ) values ("北京仓库","北京市朝阳区建国路300号" );
insert into `warehouse_info` (`warehouse_name`, `location` ) values ("广州仓库","广东省广州市天河区体育西路400号" );
insert into `warehouse_info` (`warehouse_name`, `location` ) values ("深圳仓库","广东省深圳市南山区科技园500号" );
insert into `warehouse_info` (`warehouse_name`, `location` ) values ("杭州仓库","浙江省杭州市滨江区江南大道600号" );

drop table if exists `customer_info`;
create table `customer_info` (
 	 `customer_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `customer_name` 	 VARCHAR(512) not null  ,
	 `phone` 	 VARCHAR(64)   ,
	 `email` 	 VARCHAR(64)   ,
	 `address` 	 VARCHAR(512)   ,
	 `rfm_level_enum_rfm_level_enum_id_1` 	 INTEGER   ,
	 `customer_tag_enum_customer_tag_enum_id_1` 	 INTEGER   ,
	 `wechat_id` 	 VARCHAR(512)   ,
	 `wechat_data_encrypted` 	 VARCHAR(512)   ,
	 `source` 	 VARCHAR(512)   
 );
insert into `customer_info` (`customer_name`, `phone`, `email`, `address`, `rfm_level_enum_rfm_level_enum_id_1`, `customer_tag_enum_customer_tag_enum_id_1`, `wechat_id`, `wechat_data_encrypted`, `source` ) values ("成都客户A","13888888888","customerA@example.com","四川省成都市武侯区","1","1","chengduA","encryptedDataA","电商平台" );
insert into `customer_info` (`customer_name`, `phone`, `email`, `address`, `rfm_level_enum_rfm_level_enum_id_1`, `customer_tag_enum_customer_tag_enum_id_1`, `wechat_id`, `wechat_data_encrypted`, `source` ) values ("上海客户B","13999999999","customerB@example.com","上海市浦东新区","2","2","shanghaiB","encryptedDataB","线下门店" );
insert into `customer_info` (`customer_name`, `phone`, `email`, `address`, `rfm_level_enum_rfm_level_enum_id_1`, `customer_tag_enum_customer_tag_enum_id_1`, `wechat_id`, `wechat_data_encrypted`, `source` ) values ("北京客户C","13777777777","customerC@example.com","北京市海淀区","3","1","beijingC","encryptedDataC","官网" );
insert into `customer_info` (`customer_name`, `phone`, `email`, `address`, `rfm_level_enum_rfm_level_enum_id_1`, `customer_tag_enum_customer_tag_enum_id_1`, `wechat_id`, `wechat_data_encrypted`, `source` ) values ("广州客户D","13666666666","customerD@example.com","广东省广州市天河区","1","2","guangzhouD","encryptedDataD","社交媒体" );
insert into `customer_info` (`customer_name`, `phone`, `email`, `address`, `rfm_level_enum_rfm_level_enum_id_1`, `customer_tag_enum_customer_tag_enum_id_1`, `wechat_id`, `wechat_data_encrypted`, `source` ) values ("深圳客户E","13555555555","customerE@example.com","广东省深圳市南山区","2","1","shenzhenE","encryptedDataE","推荐" );
insert into `customer_info` (`customer_name`, `phone`, `email`, `address`, `rfm_level_enum_rfm_level_enum_id_1`, `customer_tag_enum_customer_tag_enum_id_1`, `wechat_id`, `wechat_data_encrypted`, `source` ) values ("杭州客户F","13444444444","customerF@example.com","浙江省杭州市滨江区","3","2","hangzhouF","encryptedDataF","广告" );

drop table if exists `rfm_level_enum`;
create table `rfm_level_enum` (
 	 `rfm_level_enum_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `level_name` 	 VARCHAR(512) not null  
 );
insert into `rfm_level_enum` (`level_name` ) values ("高价值" );
insert into `rfm_level_enum` (`level_name` ) values ("潜力" );
insert into `rfm_level_enum` (`level_name` ) values ("沉睡" );

drop table if exists `customer_tag_enum`;
create table `customer_tag_enum` (
 	 `customer_tag_enum_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `tag_name` 	 VARCHAR(512) not null  ,
	 `is_ai_generated` 	 BOOL   
 );
insert into `customer_tag_enum` (`tag_name`, `is_ai_generated` ) values ("高活跃","" );
insert into `customer_tag_enum` (`tag_name`, `is_ai_generated` ) values ("待跟进","True" );

drop table if exists `order_info`;
create table `order_info` (
 	 `order_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `order_no` 	 VARCHAR(512) not null  ,
	 `customer_info_customer_info_id_1` 	 INTEGER not null  ,
	 `user_info_user_info_id_1` 	 INTEGER not null  ,
	 `order_status_enum_order_status_enum_id_1` 	 INTEGER not null  ,
	 `total_amount` 	 DECIMAL(18,6) not null  ,
	 `profit_amount` 	 DECIMAL(18,6)   ,
	 `commission` 	 DECIMAL(18,6)   ,
	 `shipping_fee` 	 DECIMAL(18,6)   ,
	 `order_date` 	 DATETIME not null  ,
	 `general_orders` 	 VARCHAR(64)   
 );
insert into `order_info` (`order_no`, `customer_info_customer_info_id_1`, `user_info_user_info_id_1`, `order_status_enum_order_status_enum_id_1`, `total_amount`, `profit_amount`, `commission`, `shipping_fee`, `order_date`, `general_orders` ) values ("ORD20230001","1","1","2","1500.0","300.0","50.0","20.0","2025-01-01 00:00:00","PAY20240001" );
insert into `order_info` (`order_no`, `customer_info_customer_info_id_1`, `user_info_user_info_id_1`, `order_status_enum_order_status_enum_id_1`, `total_amount`, `profit_amount`, `commission`, `shipping_fee`, `order_date`, `general_orders` ) values ("ORD20230002","2","2","3","2500.0","500.0","80.0","30.0","2025-01-01 00:00:00","PAY20240002" );
insert into `order_info` (`order_no`, `customer_info_customer_info_id_1`, `user_info_user_info_id_1`, `order_status_enum_order_status_enum_id_1`, `total_amount`, `profit_amount`, `commission`, `shipping_fee`, `order_date`, `general_orders` ) values ("ORD20230003","3","3","4","1200.0","200.0","40.0","15.0","2025-01-01 00:00:00","PAY20240003" );
insert into `order_info` (`order_no`, `customer_info_customer_info_id_1`, `user_info_user_info_id_1`, `order_status_enum_order_status_enum_id_1`, `total_amount`, `profit_amount`, `commission`, `shipping_fee`, `order_date`, `general_orders` ) values ("ORD20230004","4","4","5","1800.0","350.0","60.0","25.0","2025-01-01 00:00:00","PAY20240004" );
insert into `order_info` (`order_no`, `customer_info_customer_info_id_1`, `user_info_user_info_id_1`, `order_status_enum_order_status_enum_id_1`, `total_amount`, `profit_amount`, `commission`, `shipping_fee`, `order_date`, `general_orders` ) values ("ORD20230005","5","5","1","2200.0","400.0","70.0","18.0","2025-01-01 00:00:00","PAY20240005" );
insert into `order_info` (`order_no`, `customer_info_customer_info_id_1`, `user_info_user_info_id_1`, `order_status_enum_order_status_enum_id_1`, `total_amount`, `profit_amount`, `commission`, `shipping_fee`, `order_date`, `general_orders` ) values ("ORD20230006","6","6","6","3000.0","600.0","90.0","35.0","2025-01-01 00:00:00","PAY20240006" );

drop table if exists `order_status_enum`;
create table `order_status_enum` (
 	 `order_status_enum_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `status_name` 	 VARCHAR(512) not null  
 );
insert into `order_status_enum` (`status_name` ) values ("待付款" );
insert into `order_status_enum` (`status_name` ) values ("已支付" );
insert into `order_status_enum` (`status_name` ) values ("待发货" );
insert into `order_status_enum` (`status_name` ) values ("已发货" );
insert into `order_status_enum` (`status_name` ) values ("待收货" );
insert into `order_status_enum` (`status_name` ) values ("已完成" );
insert into `order_status_enum` (`status_name` ) values ("已取消" );
insert into `order_status_enum` (`status_name` ) values ("已超时" );

drop table if exists `order_item`;
create table `order_item` (
 	 `order_item_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `order_info_order_info_id_1` 	 INTEGER not null  ,
	 `product_info_product_info_id_1` 	 INTEGER not null  ,
	 `quantity` 	 INTEGER not null  ,
	 `unit_price` 	 DECIMAL(18,6) not null  ,
	 `total_price` 	 DECIMAL(18,6) not null  
 );
insert into `order_item` (`order_info_order_info_id_1`, `product_info_product_info_id_1`, `quantity`, `unit_price`, `total_price` ) values ("1","1","2","750.0","1500.0" );
insert into `order_item` (`order_info_order_info_id_1`, `product_info_product_info_id_1`, `quantity`, `unit_price`, `total_price` ) values ("1","3","1","200.0","200.0" );
insert into `order_item` (`order_info_order_info_id_1`, `product_info_product_info_id_1`, `quantity`, `unit_price`, `total_price` ) values ("2","2","1","2500.0","2500.0" );
insert into `order_item` (`order_info_order_info_id_1`, `product_info_product_info_id_1`, `quantity`, `unit_price`, `total_price` ) values ("3","4","3","400.0","1200.0" );
insert into `order_item` (`order_info_order_info_id_1`, `product_info_product_info_id_1`, `quantity`, `unit_price`, `total_price` ) values ("4","5","2","900.0","1800.0" );
insert into `order_item` (`order_info_order_info_id_1`, `product_info_product_info_id_1`, `quantity`, `unit_price`, `total_price` ) values ("5","6","1","2200.0","2200.0" );

drop table if exists `purchase_info`;
create table `purchase_info` (
 	 `purchase_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `purchase_no` 	 VARCHAR(512) not null  ,
	 `user_info_user_info_id_1` 	 INTEGER not null  ,
	 `supplier` 	 VARCHAR(512)   ,
	 `total_amount` 	 DECIMAL(18,6) not null  ,
	 `purchase_date` 	 DATETIME not null  ,
	 `purchase_status_enum_purchase_status_enum_id_1` 	 INTEGER not null  
 );
insert into `purchase_info` (`purchase_no`, `user_info_user_info_id_1`, `supplier`, `total_amount`, `purchase_date`, `purchase_status_enum_purchase_status_enum_id_1` ) values ("PUR20230001","1","供应商A","5000.0","2025-01-01 00:00:00","2" );
insert into `purchase_info` (`purchase_no`, `user_info_user_info_id_1`, `supplier`, `total_amount`, `purchase_date`, `purchase_status_enum_purchase_status_enum_id_1` ) values ("PUR20230002","2","供应商B","8000.0","2025-01-01 00:00:00","3" );
insert into `purchase_info` (`purchase_no`, `user_info_user_info_id_1`, `supplier`, `total_amount`, `purchase_date`, `purchase_status_enum_purchase_status_enum_id_1` ) values ("PUR20230003","3","供应商C","3000.0","2025-01-01 00:00:00","1" );
insert into `purchase_info` (`purchase_no`, `user_info_user_info_id_1`, `supplier`, `total_amount`, `purchase_date`, `purchase_status_enum_purchase_status_enum_id_1` ) values ("PUR20230004","4","供应商D","6000.0","2025-01-01 00:00:00","4" );
insert into `purchase_info` (`purchase_no`, `user_info_user_info_id_1`, `supplier`, `total_amount`, `purchase_date`, `purchase_status_enum_purchase_status_enum_id_1` ) values ("PUR20230005","5","供应商E","7000.0","2025-01-01 00:00:00","2" );
insert into `purchase_info` (`purchase_no`, `user_info_user_info_id_1`, `supplier`, `total_amount`, `purchase_date`, `purchase_status_enum_purchase_status_enum_id_1` ) values ("PUR20230006","6","供应商F","9000.0","2025-01-01 00:00:00","3" );

drop table if exists `purchase_status_enum`;
create table `purchase_status_enum` (
 	 `purchase_status_enum_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `status_name` 	 VARCHAR(512) not null  
 );
insert into `purchase_status_enum` (`status_name` ) values ("待采购" );
insert into `purchase_status_enum` (`status_name` ) values ("已下单" );
insert into `purchase_status_enum` (`status_name` ) values ("已到货" );
insert into `purchase_status_enum` (`status_name` ) values ("已完成" );

drop table if exists `transfer_info`;
create table `transfer_info` (
 	 `transfer_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `from_warehouse_info_warehouse_info_id_1` 	 INTEGER not null  ,
	 `to_warehouse_info_warehouse_info_id_2` 	 INTEGER not null  ,
	 `product_info_product_info_id_1` 	 INTEGER not null  ,
	 `quantity` 	 INTEGER not null  ,
	 `transfer_status_enum_transfer_status_enum_id_1` 	 INTEGER not null  ,
	 `apply_user_info_user_info_id_1` 	 INTEGER not null  ,
	 `confirm_user_info_user_info_id_2` 	 INTEGER   ,
	 `apply_date` 	 DATETIME not null  ,
	 `confirm_date` 	 DATETIME   
 );
insert into `transfer_info` (`from_warehouse_info_warehouse_info_id_1`, `to_warehouse_info_warehouse_info_id_2`, `product_info_product_info_id_1`, `quantity`, `transfer_status_enum_transfer_status_enum_id_1`, `apply_user_info_user_info_id_1`, `confirm_user_info_user_info_id_2`, `apply_date`, `confirm_date` ) values ("1","2","1","20","1","1","2","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `transfer_info` (`from_warehouse_info_warehouse_info_id_1`, `to_warehouse_info_warehouse_info_id_2`, `product_info_product_info_id_1`, `quantity`, `transfer_status_enum_transfer_status_enum_id_1`, `apply_user_info_user_info_id_1`, `confirm_user_info_user_info_id_2`, `apply_date`, `confirm_date` ) values ("2","3","2","10","2","2","3","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `transfer_info` (`from_warehouse_info_warehouse_info_id_1`, `to_warehouse_info_warehouse_info_id_2`, `product_info_product_info_id_1`, `quantity`, `transfer_status_enum_transfer_status_enum_id_1`, `apply_user_info_user_info_id_1`, `confirm_user_info_user_info_id_2`, `apply_date`, `confirm_date` ) values ("3","1","3","50","3","3","","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `transfer_info` (`from_warehouse_info_warehouse_info_id_1`, `to_warehouse_info_warehouse_info_id_2`, `product_info_product_info_id_1`, `quantity`, `transfer_status_enum_transfer_status_enum_id_1`, `apply_user_info_user_info_id_1`, `confirm_user_info_user_info_id_2`, `apply_date`, `confirm_date` ) values ("4","5","4","30","2","4","5","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `transfer_info` (`from_warehouse_info_warehouse_info_id_1`, `to_warehouse_info_warehouse_info_id_2`, `product_info_product_info_id_1`, `quantity`, `transfer_status_enum_transfer_status_enum_id_1`, `apply_user_info_user_info_id_1`, `confirm_user_info_user_info_id_2`, `apply_date`, `confirm_date` ) values ("5","6","5","15","1","5","6","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `transfer_info` (`from_warehouse_info_warehouse_info_id_1`, `to_warehouse_info_warehouse_info_id_2`, `product_info_product_info_id_1`, `quantity`, `transfer_status_enum_transfer_status_enum_id_1`, `apply_user_info_user_info_id_1`, `confirm_user_info_user_info_id_2`, `apply_date`, `confirm_date` ) values ("6","4","6","25","3","6","","2025-01-01 00:00:00","2025-01-01 00:00:00" );

drop table if exists `transfer_status_enum`;
create table `transfer_status_enum` (
 	 `transfer_status_enum_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `status_name` 	 VARCHAR(512) not null  
 );
insert into `transfer_status_enum` (`status_name` ) values ("待确认" );
insert into `transfer_status_enum` (`status_name` ) values ("已确认" );
insert into `transfer_status_enum` (`status_name` ) values ("已完成" );

drop table if exists `business_opportunity`;
create table `business_opportunity` (
 	 `business_opportunity_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `customer_info_customer_info_id_1` 	 INTEGER not null  ,
	 `opportunity_name` 	 VARCHAR(512) not null  ,
	 `status` 	 VARCHAR(512) not null  ,
	 `follow_up_suggestion` 	 ai   
 );
insert into `business_opportunity` (`customer_info_customer_info_id_1`, `opportunity_name`, `status`, `follow_up_suggestion` ) values ("1","大客户合作","进行中","" );
insert into `business_opportunity` (`customer_info_customer_info_id_1`, `opportunity_name`, `status`, `follow_up_suggestion` ) values ("2","新品推广","待跟进","" );
insert into `business_opportunity` (`customer_info_customer_info_id_1`, `opportunity_name`, `status`, `follow_up_suggestion` ) values ("3","续约谈判","已完成","" );
insert into `business_opportunity` (`customer_info_customer_info_id_1`, `opportunity_name`, `status`, `follow_up_suggestion` ) values ("4","渠道拓展","进行中","" );
insert into `business_opportunity` (`customer_info_customer_info_id_1`, `opportunity_name`, `status`, `follow_up_suggestion` ) values ("5","价格调整","待跟进","" );
insert into `business_opportunity` (`customer_info_customer_info_id_1`, `opportunity_name`, `status`, `follow_up_suggestion` ) values ("6","售后服务","已完成","" );

drop table if exists `ai_assistant_log`;
create table `ai_assistant_log` (
 	 `ai_assistant_log_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `user_info_user_info_id_1` 	 INTEGER not null  ,
	 `query_text` 	 VARCHAR(512) not null  ,
	 `response_text` 	 VARCHAR(512) not null  ,
	 `query_time` 	 DATETIME not null  
 );
insert into `ai_assistant_log` (`user_info_user_info_id_1`, `query_text`, `response_text`, `query_time` ) values ("1","库存不足怎么办？","建议及时补货，避免断货影响销售。","2025-01-01 00:00:00" );
insert into `ai_assistant_log` (`user_info_user_info_id_1`, `query_text`, `response_text`, `query_time` ) values ("2","如何提升客户活跃度？","通过定期促销和会员活动提升客户活跃度。","2025-01-01 00:00:00" );
insert into `ai_assistant_log` (`user_info_user_info_id_1`, `query_text`, `response_text`, `query_time` ) values ("3","订单状态查询","订单正在处理中，预计两天内发货。","2025-01-01 00:00:00" );
insert into `ai_assistant_log` (`user_info_user_info_id_1`, `query_text`, `response_text`, `query_time` ) values ("4","采购流程介绍","采购流程包括需求确认、下单、验收和付款。","2025-01-01 00:00:00" );
insert into `ai_assistant_log` (`user_info_user_info_id_1`, `query_text`, `response_text`, `query_time` ) values ("5","如何处理退货？","请联系售后部门，填写退货申请表。","2025-01-01 00:00:00" );
insert into `ai_assistant_log` (`user_info_user_info_id_1`, `query_text`, `response_text`, `query_time` ) values ("6","调拨申请流程","调拨申请需填写申请单并经仓库确认。","2025-01-01 00:00:00" );

drop table if exists `ad_recommend_info`;
create table `ad_recommend_info` (
 	 `ad_recommend_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `ad_title` 	 VARCHAR(512) not null  ,
	 `ad_image_resource_key` 	 VARCHAR(255)   ,
	 `ad_url` 	 VARCHAR(512)   ,
	 `cpc_fee` 	 DECIMAL(18,6)   
 );
insert into `ad_recommend_info` (`ad_title`, `ad_image_resource_key`, `ad_url`, `cpc_fee` ) values ("夏季大促销","0","https://example.com/summer-promo","2.5" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/summer-drinks.jpg','ad_recommend_info', '1', 'ad_image');
insert into `ad_recommend_info` (`ad_title`, `ad_image_resource_key`, `ad_url`, `cpc_fee` ) values ("新品上市","0","https://example.com/new-arrival","3.0" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/ai-robot.jpg','ad_recommend_info', '2', 'ad_image');
insert into `ad_recommend_info` (`ad_title`, `ad_image_resource_key`, `ad_url`, `cpc_fee` ) values ("限时折扣","0","https://example.com/limited-offer","1.8" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/hamburger-dinner.jpg','ad_recommend_info', '3', 'ad_image');
insert into `ad_recommend_info` (`ad_title`, `ad_image_resource_key`, `ad_url`, `cpc_fee` ) values ("会员专享","0","https://example.com/member-only","2.2" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/flowers-vase.jpg','ad_recommend_info', '4', 'ad_image');
insert into `ad_recommend_info` (`ad_title`, `ad_image_resource_key`, `ad_url`, `cpc_fee` ) values ("节日特惠","0","https://example.com/holiday-sale","2.9" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/love-valentine.jpg','ad_recommend_info', '5', 'ad_image');
insert into `ad_recommend_info` (`ad_title`, `ad_image_resource_key`, `ad_url`, `cpc_fee` ) values ("新品推荐","0","https://example.com/recommendation","3.1" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/film-poster.jpg','ad_recommend_info', '6', 'ad_image');

drop table if exists `profit_board`;
create table `profit_board` (
 	 `profit_board_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `target_type` 	 VARCHAR(512) not null  ,
	 `target_id` 	 INTEGER not null  ,
	 `profit_amount` 	 DECIMAL(18,6) not null  ,
	 `commission` 	 DECIMAL(18,6)   ,
	 `shipping_fee` 	 DECIMAL(18,6)   ,
	 `stat_date` 	 DATETIME not null  
 );
insert into `profit_board` (`target_type`, `target_id`, `profit_amount`, `commission`, `shipping_fee`, `stat_date` ) values ("product_info","1","12000.0","2000.0","500.0","2025-01-01 00:00:00" );
insert into `profit_board` (`target_type`, `target_id`, `profit_amount`, `commission`, `shipping_fee`, `stat_date` ) values ("product_info","2","15000.0","2500.0","600.0","2025-01-01 00:00:00" );
insert into `profit_board` (`target_type`, `target_id`, `profit_amount`, `commission`, `shipping_fee`, `stat_date` ) values ("customer_info","1","8000.0","1200.0","300.0","2025-01-01 00:00:00" );
insert into `profit_board` (`target_type`, `target_id`, `profit_amount`, `commission`, `shipping_fee`, `stat_date` ) values ("customer_info","2","9000.0","1300.0","350.0","2025-01-01 00:00:00" );
insert into `profit_board` (`target_type`, `target_id`, `profit_amount`, `commission`, `shipping_fee`, `stat_date` ) values ("warehouse_info","1","11000.0","1800.0","450.0","2025-01-01 00:00:00" );
insert into `profit_board` (`target_type`, `target_id`, `profit_amount`, `commission`, `shipping_fee`, `stat_date` ) values ("warehouse_info","2","10000.0","1700.0","400.0","2025-01-01 00:00:00" );

drop table if exists `procurement_suggestion`;
create table `procurement_suggestion` (
 	 `procurement_suggestion_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `product_info_product_info_id_1` 	 INTEGER not null  ,
	 `suggest_quantity` 	 INTEGER not null  ,
	 `reason` 	 VARCHAR(512)   ,
	 `suggest_date` 	 DATETIME not null  
 );
insert into `procurement_suggestion` (`product_info_product_info_id_1`, `suggest_quantity`, `reason`, `suggest_date` ) values ("1","50","库存低于安全库存，建议补货","2025-01-01 00:00:00" );
insert into `procurement_suggestion` (`product_info_product_info_id_1`, `suggest_quantity`, `reason`, `suggest_date` ) values ("2","30","新品推广需增加库存","2025-01-01 00:00:00" );
insert into `procurement_suggestion` (`product_info_product_info_id_1`, `suggest_quantity`, `reason`, `suggest_date` ) values ("3","100","临期商品库存补充","2025-01-01 00:00:00" );
insert into `procurement_suggestion` (`product_info_product_info_id_1`, `suggest_quantity`, `reason`, `suggest_date` ) values ("4","40","季节性需求增加","2025-01-01 00:00:00" );
insert into `procurement_suggestion` (`product_info_product_info_id_1`, `suggest_quantity`, `reason`, `suggest_date` ) values ("5","25","促销活动准备","2025-01-01 00:00:00" );
insert into `procurement_suggestion` (`product_info_product_info_id_1`, `suggest_quantity`, `reason`, `suggest_date` ) values ("6","15","新品上市备货","2025-01-01 00:00:00" );

drop table if exists `business_card_ocr`;
create table `business_card_ocr` (
 	 `business_card_ocr_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `customer_info_customer_info_id_1` 	 INTEGER not null  ,
	 `card_image_resource_key` 	 VARCHAR(255)   ,
	 `ocr_result` 	 VARCHAR(512) not null  
 );
insert into `business_card_ocr` (`customer_info_customer_info_id_1`, `card_image_resource_key`, `ocr_result` ) values ("1","0","姓名：张三
电话：13800138000
公司：成都科技有限公司" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/business-meeting.jpg','business_card_ocr', '1', 'card_image');
insert into `business_card_ocr` (`customer_info_customer_info_id_1`, `card_image_resource_key`, `ocr_result` ) values ("2","0","姓名：李四
电话：13900139000
公司：上海贸易有限公司" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/meeting.jpg','business_card_ocr', '2', 'card_image');
insert into `business_card_ocr` (`customer_info_customer_info_id_1`, `card_image_resource_key`, `ocr_result` ) values ("3","0","姓名：王五
电话：13700137000
公司：北京咨询有限公司" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/business-team.jpg','business_card_ocr', '3', 'card_image');
insert into `business_card_ocr` (`customer_info_customer_info_id_1`, `card_image_resource_key`, `ocr_result` ) values ("4","0","姓名：赵六
电话：13600136000
公司：广州设计有限公司" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/photographer-picture.jpg','business_card_ocr', '4', 'card_image');
insert into `business_card_ocr` (`customer_info_customer_info_id_1`, `card_image_resource_key`, `ocr_result` ) values ("5","0","姓名：钱七
电话：13500135000
公司：深圳制造有限公司" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/photography-gear.jpg','business_card_ocr', '5', 'card_image');
insert into `business_card_ocr` (`customer_info_customer_info_id_1`, `card_image_resource_key`, `ocr_result` ) values ("6","0","姓名：孙八
电话：13400134000
公司：杭州服务有限公司" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.token.net/preview/coffee-mug.jpg','business_card_ocr', '6', 'card_image');

drop table if exists `fission_tool`;
create table `fission_tool` (
 	 `fission_tool_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `tool_type` 	 VARCHAR(512) not null  ,
	 `title` 	 VARCHAR(512) not null  ,
	 `config` 	 VARCHAR(512)   ,
	 `start_date` 	 DATETIME not null  ,
	 `end_date` 	 DATETIME not null  
 );
insert into `fission_tool` (`tool_type`, `title`, `config`, `start_date`, `end_date` ) values ("邀请裂变","好友邀请赢奖励",'{"reward":"优惠券","max_invites":10}',"2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `fission_tool` (`tool_type`, `title`, `config`, `start_date`, `end_date` ) values ("分享裂变","分享活动得积分",'{"points_per_share":5}',"2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `fission_tool` (`tool_type`, `title`, `config`, `start_date`, `end_date` ) values ("抽奖裂变","参与抽奖赢大奖",'{"prizes":["手机","平板","优惠券"]}',"2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `fission_tool` (`tool_type`, `title`, `config`, `start_date`, `end_date` ) values ("签到裂变","连续签到送礼品",'{"consecutive_days":7}',"2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `fission_tool` (`tool_type`, `title`, `config`, `start_date`, `end_date` ) values ("购买裂变","购买商品送积分",'{"points_per_purchase":10}',"2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `fission_tool` (`tool_type`, `title`, `config`, `start_date`, `end_date` ) values ("评价裂变","评价商品得奖励",'{"reward":"积分"}',"2025-01-01 00:00:00","2025-01-01 00:00:00" );

drop table if exists `live_stream_sync`;
create table `live_stream_sync` (
 	 `live_stream_sync_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `order_info_order_info_id_1` 	 INTEGER not null  ,
	 `platform` 	 VARCHAR(512) not null  ,
	 `sync_time` 	 DATETIME not null  
 );
insert into `live_stream_sync` (`order_info_order_info_id_1`, `platform`, `sync_time` ) values ("1","抖音","2025-01-01 00:00:00" );
insert into `live_stream_sync` (`order_info_order_info_id_1`, `platform`, `sync_time` ) values ("2","快手","2025-01-01 00:00:00" );
insert into `live_stream_sync` (`order_info_order_info_id_1`, `platform`, `sync_time` ) values ("3","淘宝直播","2025-01-01 00:00:00" );
insert into `live_stream_sync` (`order_info_order_info_id_1`, `platform`, `sync_time` ) values ("4","京东直播","2025-01-01 00:00:00" );
insert into `live_stream_sync` (`order_info_order_info_id_1`, `platform`, `sync_time` ) values ("5","拼多多直播","2025-01-01 00:00:00" );
insert into `live_stream_sync` (`order_info_order_info_id_1`, `platform`, `sync_time` ) values ("6","小红书直播","2025-01-01 00:00:00" );

drop table if exists `dynamic_api_setting`;
create table `dynamic_api_setting` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `key_name` 	 VARCHAR(512)   ,
	 `description` 	 VARCHAR(512)   ,
	 `url` 	 VARCHAR(512)   ,
	 `token` 	 VARCHAR(512)   ,
	 `app_id` 	 VARCHAR(512)   ,
	 `api_key` 	 VARCHAR(512)   ,
	 `api_secret` 	 VARCHAR(512)   ,
	 `method` 	 VARCHAR(512)   ,
	 `body_type` 	 VARCHAR(512)   ,
	 `body_template` 	 VARCHAR(512)   ,
	 `header` 	 VARCHAR(512)   ,
	 `auth_type` 	 VARCHAR(512)   ,
	 `protocol` 	 VARCHAR(512)   ,
	 `data_path` 	 VARCHAR(512)   ,
	 `data_type` 	 VARCHAR(512)   
 );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("word2pic","AI文本生成图片","http://1.94.150.175:8087/api/word2pic","sk-S9MO0B99pZGBAK3Z0/hYwVpBQvdQZyuhxRUsd2itGR4=","","","","post","template",'{"input":"${{text}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"token","http","","" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("pic2word","AI图片识别理解","http://1.94.150.175:8087/api/pic2word","sk-S9MO0B99pZGBAK3Z0/hYwVpBQvdQZyuhxRUsd2itGR4=","","","","post","template",'{"input":"请详细描述一下图中有什么？如果图片中是文本，请将文本内容原样输出。","content":"${{content}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"token","http","","" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("text2text","AI文本生成文本","http://1.94.150.175:8087/llm/chat-block","sk-S9MO0B99pZGBAK3Z0/hYwVpBQvdQZyuhxRUsd2itGR4=","","","","post","","",'{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"token","http","","" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("tts","文本生成语音","http://1.94.150.175:8087/api/text2speech","sk-S9MO0B99pZGBAK3Z0/hYwVpBQvdQZyuhxRUsd2itGR4=","","","","post","template",'{"content":"${{text}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"token","http","","" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("amap_geo","高德-将详细的结构化地址转换为经纬度坐标","http://1.94.150.175:8087/mcp/amap?method=maps_geo","sk-S9MO0B99pZGBAK3Z0/hYwVpBQvdQZyuhxRUsd2itGR4=","","","","post","template",'{"city":"${{city}}","address":"${{address}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"token","http","$.data.content[0].text","json" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("amap_regeocode","高德-经纬度坐标转换为行政区划地址信息","http://1.94.150.175:8087/mcp/amap?method=maps_regeocode","sk-S9MO0B99pZGBAK3Z0/hYwVpBQvdQZyuhxRUsd2itGR4=","","","","post","template",'{"location":"${{location}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"token","http","$.data.content[0].text","json" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("amap_direction_driving","高德-驾车路径规划","http://1.94.150.175:8087/mcp/amap?method=maps_direction_driving","sk-S9MO0B99pZGBAK3Z0/hYwVpBQvdQZyuhxRUsd2itGR4=","","","","post","template",'{"origin":"${{origin}}","destination":"${{destination}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"token","http","$.data.content[0].text","json" );

drop table if exists `login`;

    create table `login` (
        `login_id`      INTEGER PRIMARY KEY AUTOINCREMENT,
        `relevance_id`  VARCHAR ,
        `password`      VARCHAR ,
        `user_name`     VARCHAR ,
        `relevance_table`       VARCHAR ,
        `phone_number`  VARCHAR ,
        `wx_open_id`    VARCHAR
    );
    
    insert into `login` (`relevance_id`,`password`,`user_name`,`relevance_table`,`phone_number`,`wx_open_id` ) values ("","$2a$10$bOrRjyHCMT2DUnXoVuFWNO1dSd6Zj/fyjS0TETmcD1ZXlR/BGklc2","admin","","admin","" );
    
    insert into `login` (`relevance_id`,`password`,`user_name`,`relevance_table`,`phone_number`,`wx_open_id` ) values ("1","$2a$10$bOrRjyHCMT2DUnXoVuFWNO1dSd6Zj/fyjS0TETmcD1ZXlR/BGklc2","test","userInfo","18852718858","" );

