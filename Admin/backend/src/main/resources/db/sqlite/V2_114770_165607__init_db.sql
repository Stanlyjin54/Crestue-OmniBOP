create table if not exists `delayed_tasks` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `task_type` 	 VARCHAR(255) not null ,
	 `task_data` 	 VARCHAR(255) not null ,
	 `status` 	 VARCHAR(255) not null ,
	 `execute_time` 	 DATETIME not null ,
	 `create_time` 	 DATETIME not null ,
	 `update_time` 	 DATETIME not null 
 );


create table if not exists `general_orders` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `order_no` 	 VARCHAR(255) not null ,
	 `user_id` 	 INTEGER not null ,
	 `op_id` 	 VARCHAR(255)  ,
	 `order_type` 	 VARCHAR(255)  ,
	 `order_status` 	 VARCHAR(255) not null ,
	 `pay_status` 	 VARCHAR(255) not null ,
	 `delivery_status` 	 VARCHAR(255)  ,
	 `products_id` 	 INTEGER not null ,
	 `quality` 	 INTEGER not null ,
	 `total_amount` 	 DECIMAL(18,6) not null ,
	 `discount_amount` 	 DECIMAL(18,6)  ,
	 `shipping_fee` 	 DECIMAL(18,6)  ,
	 `actual_amount` 	 DECIMAL(18,6) not null ,
	 `payment_channel` 	 VARCHAR(255)  ,
	 `payment_order_id` 	 VARCHAR(255)  ,
	 `payment_time` 	 DATETIME  ,
	 `payment_amount` 	 DECIMAL(18,6)  ,
	 `shipping_method` 	 VARCHAR(255)  ,
	 `shipping_address` 	 VARCHAR(255)  ,
	 `shipping_company` 	 VARCHAR(255)  ,
	 `tracking_number` 	 VARCHAR(255)  ,
	 `tel` 	 VARCHAR(255)  ,
	 `recipient` 	 VARCHAR(255)  ,
	 `create_time` 	 DATETIME not null ,
	 `update_time` 	 DATETIME not null ,
	 `cancel_time` 	 DATETIME  ,
	 `complete_time` 	 DATETIME  ,
	 `deliver_time` 	 DATETIME  ,
	 `refund_id` 	 VARCHAR(255)  ,
	 `refund_no` 	 VARCHAR(255)  ,
	 `refund_type` 	 VARCHAR(255)  ,
	 `refund_amount` 	 DECIMAL(18,6)  ,
	 `refund_reason` 	 VARCHAR(255)  ,
	 `refund_status` 	 VARCHAR(255)  ,
	 `remark` 	 VARCHAR(255)  ,
	 `is_deleted` 	 INTEGER  ,
	 `table_name` 	 VARCHAR(255)  ,
	 `field_name` 	 VARCHAR(255)  
 );


create table if not exists `system_config` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `name` 	 VARCHAR(255) not null ,
	 `chinese_name` 	 VARCHAR(255)  ,
	 `description` 	 VARCHAR(255)  ,
	 `content` 	 VARCHAR(255)  ,
	 `remark` 	 VARCHAR(255)  ,
	 `type` 	 VARCHAR(255)  
 );


insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "order.expire.time","订单多少时间未支付自动关闭，单位分钟","None","30","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'order.expire.time');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "order.automatic.receiptTime","订单多长时间自动确认收货，单位 天","None","5","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'order.automatic.receiptTime');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "order.automatic.schedule.corn","订单自动确认收货的定时任务的执行周期","None","*/10 * * * * ?","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'order.automatic.schedule.corn');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wechat.app-id","微信小程序appid","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wechat.app-id');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wechat.app-secret","微信小程序secret","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wechat.app-secret');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wechat.is-register","默认配置不需要修改","None","0","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wechat.is-register');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.pay-back-url","微信支付回调url","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.pay-back-url');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.mch-serial-no","微信支付序列号","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.mch-serial-no');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.app-id","微信支付对应的appid","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.app-id');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.mch-id","微信支付商户号","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.mch-id');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.api-v3-key","微信支付 apiv3 key","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.api-v3-key');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.is_test","微信支付是否开启测试,开启测试后，支付金额统一为0.01，正式上线请关闭（true/false）","None","true","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.is_test');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.is_test_amount","微信支付测试金额","None","0.01","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.is_test_amount');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.mock_enable","是否开启mock支付（没有申请支付的情况下，true/false）","None","true","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.mock_enable');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.wechat_enable","是否开启微信支付(true/false)","None","false","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.wechat_enable');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.mp-app-id","微信公众号的APPID","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.mp-app-id');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wechat.mp-redirect_uri","重定向的url","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wechat.mp-redirect_uri');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.mp-app-secret","None","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.mp-app-secret');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wechat.mp-frontend_redirect_uri","前端的重定向地址","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wechat.mp-frontend_redirect_uri');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "order.orderStatusField","订单表中的订单状态字段","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'order.orderStatusField');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "order.refundStatusValue","系统订单中退款状态与业务订单表退款状态的中的映射关系","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'order.refundStatusValue');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.private-key","微信支付私钥内容 （apiclient_key.pem）","None","None","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.private-key');

insert into `system_config` (`name`, `chinese_name`, `description`, `content`, `remark`, `type` ) select "wx.pay.order_pre_key","支付商户号的统一前缀(请谨慎修改,该值变化后将使存量的支付订单不可用)","None","order-114770-","None","None"  WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE name =  'wx.pay.order_pre_key');
