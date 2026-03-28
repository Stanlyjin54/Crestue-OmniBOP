-- 添加用户类型字段
ALTER TABLE user_info ADD COLUMN user_type VARCHAR(32) DEFAULT 'operator';
COMMENT ON COLUMN user_info.user_type IS '用户类型：consumer-消费者(B2C), operator-经营者(Application)';

-- 更新现有用户为经营者
UPDATE user_info SET user_type = 'operator';

-- 创建收货地址表
CREATE TABLE IF NOT EXISTS user_address (
    address_id INTEGER PRIMARY KEY AUTOINCREMENT COMMENT '地址ID',
    user_info_id INTEGER NOT NULL COMMENT '用户ID',
    receiver_name VARCHAR(128) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(32) NOT NULL COMMENT '收货人电话',
    province VARCHAR(128) COMMENT '省份',
    city VARCHAR(128) COMMENT '城市',
    district VARCHAR(128) COMMENT '区县',
    detail_address VARCHAR(512) NOT NULL COMMENT '详细地址',
    zip_code VARCHAR(16) COMMENT '邮编',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_info_id) REFERENCES user_info(user_info_id)
);

COMMENT ON TABLE user_address IS '用户收货地址表';

-- 创建B2C订单扩展字段（如果order_info表不存在这些字段）
-- 注意：这些字段可能已经存在，需要检查
-- ALTER TABLE order_info ADD COLUMN receiver_name VARCHAR(128);
-- ALTER TABLE order_info ADD COLUMN receiver_phone VARCHAR(32);
-- ALTER TABLE order_info ADD COLUMN receiver_address VARCHAR(512);
-- ALTER TABLE order_info ADD COLUMN cancel_reason VARCHAR(512);
-- ALTER TABLE order_info ADD COLUMN confirm_time DATETIME;

-- 创建商品SKU表（如果不存在）
CREATE TABLE IF NOT EXISTS product_sku (
    sku_id INTEGER PRIMARY KEY AUTOINCREMENT COMMENT 'SKU ID',
    product_info_id INTEGER NOT NULL COMMENT '商品ID',
    sku_code VARCHAR(128) NOT NULL COMMENT 'SKU编码',
    sku_name VARCHAR(256) COMMENT 'SKU名称（如：红色-大号）',
    price DECIMAL(10,2) NOT NULL COMMENT '售价',
    stock INTEGER DEFAULT 0 COMMENT '库存',
    specs JSON COMMENT '规格属性（JSON格式）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_info_id) REFERENCES product_info(product_info_id)
);

COMMENT ON TABLE product_sku IS '商品SKU表';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_user_address_user_id ON user_address(user_info_id);
CREATE INDEX IF NOT EXISTS idx_product_sku_product_id ON product_sku(product_info_id);
CREATE INDEX IF NOT EXISTS idx_user_info_user_type ON user_info(user_type);
