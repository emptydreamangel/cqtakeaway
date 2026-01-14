## 1️⃣ 用户模块
### users (用户表)
```sql
CREATE TABLE users (
    user_id       BIGSERIAL PRIMARY KEY,
    phone         VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nickname      VARCHAR(50),
    avatar        VARCHAR(255),
    gender        SMALLINT DEFAULT 0, -- 0:未知 1:男 2:女
    status        SMALLINT DEFAULT 1, -- 0:禁用 1:正常
    register_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_time TIMESTAMP
);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_status ON users(status);
COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.gender IS '0:未知 1:男 2:女';
```
### user_addresses (用户收货地址表)
```sql
CREATE TABLE user_addresses (
    address_id    BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    province      VARCHAR(50) NOT NULL,
    city          VARCHAR(50) NOT NULL,
    district      VARCHAR(50) NOT NULL,
    detail_address VARCHAR(200) NOT NULL,
    longitude     NUMERIC(10,7),
    latitude      NUMERIC(10,7),
    is_default    SMALLINT DEFAULT 0, -- 0:否 1:是
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE user_addresses ADD CONSTRAINT fk_user_addresses_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
CREATE INDEX idx_user_addresses_user ON user_addresses(user_id);
COMMENT ON TABLE user_addresses IS '用户收货地址表';
```
### user_favorites (用户收藏表)
```sql
CREATE TABLE user_favorites (
    favorite_id BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    shop_id     BIGINT NOT NULL,
    product_id  BIGINT,
    type        SMALLINT NOT NULL, -- 1:店铺 2:商品
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE user_favorites ADD CONSTRAINT fk_user_favorites_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
CREATE UNIQUE INDEX uk_user_shop_product ON user_favorites(user_id, shop_id, product_id) WHERE product_id IS NOT NULL;
CREATE INDEX idx_user_favorites_user ON user_favorites(user_id);
COMMENT ON TABLE user_favorites IS '用户收藏表';
```
---
## 2️⃣ 商家模块
### shop_categories (商家分类表)
```sql
CREATE TABLE shop_categories (
    category_id   SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL,
    icon          VARCHAR(255),
    sort_order    INT DEFAULT 0,
    status        SMALLINT DEFAULT 1 -- 0:禁用 1:正常
);
COMMENT ON TABLE shop_categories IS '商家分类表';
```
### shops (商家表)
```sql
CREATE TABLE shops (
    shop_id        BIGSERIAL PRIMARY KEY,
    category_id    INT NOT NULL,
    shop_name      VARCHAR(100) NOT NULL,
    logo           VARCHAR(255),
    banner         VARCHAR(255),
    description    TEXT,
    province       VARCHAR(50) NOT NULL,
    city           VARCHAR(50) NOT NULL,
    district       VARCHAR(50) NOT NULL,
    address        VARCHAR(200) NOT NULL,
    longitude      NUMERIC(10,7) NOT NULL,
    latitude       NUMERIC(10,7) NOT NULL,
    phone          VARCHAR(20),
    business_hours JSONB, -- 营业时间，JSON格式存储更灵活
    delivery_time  INT, -- 预计配送时间(分钟)
    min_order_amount NUMERIC(10,2) DEFAULT 0,
    delivery_fee   NUMERIC(10,2) DEFAULT 0,
    packing_fee    NUMERIC(10,2) DEFAULT 0,
    rating         NUMERIC(3,2) DEFAULT 5.00,
    sales_count    INT DEFAULT 0,
    status         SMALLINT DEFAULT 1, -- 0:休息中 1:营业中 2:打烊
    is_auth        SMALLINT DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE shops ADD CONSTRAINT fk_shops_category FOREIGN KEY (category_id) REFERENCES shop_categories(category_id);
CREATE INDEX idx_shops_category ON shops(category_id);
CREATE INDEX idx_shops_location ON shops(longitude, latitude);
CREATE INDEX idx_shops_status ON shops(status);
COMMENT ON TABLE shops IS '商家表';
```
### shop_images (商家图片表)
```sql
CREATE TABLE shop_images (
    image_id   BIGSERIAL PRIMARY KEY,
    shop_id    BIGINT NOT NULL,
    image_url  VARCHAR(255) NOT NULL,
    image_type SMALLINT, -- 1:环境图 2:菜品图
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE shop_images ADD CONSTRAINT fk_shop_images_shop FOREIGN KEY (shop_id) REFERENCES shops(shop_id) ON DELETE CASCADE;
COMMENT ON TABLE shop_images IS '商家图片表';
```
---
## 3️⃣ 商品模块
### product_categories (商品分类表)
```sql
CREATE TABLE product_categories (
    category_id   BIGSERIAL PRIMARY KEY,
    shop_id       BIGINT NOT NULL,
    category_name VARCHAR(50) NOT NULL,
    sort_order    INT DEFAULT 0,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE product_categories ADD CONSTRAINT fk_product_categories_shop FOREIGN KEY (shop_id) REFERENCES shops(shop_id) ON DELETE CASCADE;
CREATE INDEX idx_product_categories_shop ON product_categories(shop_id);
COMMENT ON TABLE product_categories IS '商品分类表';
```
### products (商品表)
```sql
CREATE TABLE products (
    product_id     BIGSERIAL PRIMARY KEY,
    shop_id        BIGINT NOT NULL,
    category_id    BIGINT NOT NULL,
    product_name   VARCHAR(100) NOT NULL,
    description    TEXT,
    main_image     VARCHAR(255),
    images         JSONB, -- 图片数组
    price          NUMERIC(10,2) NOT NULL,
    original_price NUMERIC(10,2),
    stock          INT DEFAULT -1, -- -1表示无限
    sales_count    INT DEFAULT 0,
    status         SMALLINT DEFAULT 1, -- 0:下架 1:上架
    sort_order     INT DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE products ADD CONSTRAINT fk_products_shop FOREIGN KEY (shop_id) REFERENCES shops(shop_id) ON DELETE CASCADE;
ALTER TABLE products ADD CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES product_categories(category_id) ON DELETE CASCADE;
CREATE INDEX idx_products_shop ON products(shop_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_status ON products(status);
COMMENT ON TABLE products IS '商品表';
```
### product_specs (商品规格表)
```sql
CREATE TABLE product_specs (
    spec_id   BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    spec_name VARCHAR(50) NOT NULL,
    price_add NUMERIC(10,2) DEFAULT 0,
    stock     INT DEFAULT -1,
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE product_specs ADD CONSTRAINT fk_product_specs_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE;
COMMENT ON TABLE product_specs IS '商品规格表';
```
### shopping_carts (购物车表)
```sql
CREATE TABLE shopping_carts (
    cart_id    BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    shop_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT NOT NULL DEFAULT 1,
    spec_id    BIGINT,
    options    JSONB, -- 存储选中的选项
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE shopping_carts ADD CONSTRAINT fk_shopping_carts_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
CREATE UNIQUE INDEX uk_cart_user_product ON shopping_carts(user_id, shop_id, product_id, COALESCE(spec_id, 0));
COMMENT ON TABLE shopping_carts IS '购物车表';
```
---
## 4️⃣ 订单模块
### orders (订单表)
```sql
CREATE TABLE orders (
    order_id      BIGSERIAL PRIMARY KEY,
    order_no      VARCHAR(32) UNIQUE NOT NULL,
    user_id       BIGINT NOT NULL,
    shop_id       BIGINT NOT NULL,
    total_amount  NUMERIC(10,2) NOT NULL,
    delivery_fee  NUMERIC(10,2) DEFAULT 0,
    packing_fee   NUMERIC(10,2) DEFAULT 0,
    discount_amount NUMERIC(10,2) DEFAULT 0,
    coupon_discount NUMERIC(10,2) DEFAULT 0,
    pay_amount    NUMERIC(10,2) NOT NULL,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    receiver_address VARCHAR(300) NOT NULL,
    receiver_longitude NUMERIC(10,7),
    receiver_latitude NUMERIC(10,7),
    remark        VARCHAR(200),
    delivery_time INT,
    status        SMALLINT DEFAULT 0, -- 0:待支付 1:待接单 2:待配送 3:配送中 4:已完成 5:已取消
    pay_status    SMALLINT DEFAULT 0, -- 0:未支付 1:已支付 2:退款中 3:已退款
    pay_time      TIMESTAMP,
    pay_method    SMALLINT, -- 1:微信 2:支付宝
    accept_time   TIMESTAMP,
    dispatch_time TIMESTAMP,
    complete_time TIMESTAMP,
    cancel_time   TIMESTAMP,
    cancel_reason VARCHAR(200),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE orders ADD CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(user_id);
ALTER TABLE orders ADD CONSTRAINT fk_orders_shop FOREIGN KEY (shop_id) REFERENCES shops(shop_id);
CREATE INDEX idx_orders_no ON orders(order_no);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_shop ON orders(shop_id);
CREATE INDEX idx_orders_status ON orders(status);
COMMENT ON TABLE orders IS '订单表';
```
### order_items (订单商品明细表)
```sql
CREATE TABLE order_items (
    item_id      BIGSERIAL PRIMARY KEY,
    order_id     BIGINT NOT NULL,
    product_id   BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    product_image VARCHAR(255),
    product_price NUMERIC(10,2) NOT NULL,
    quantity     INT NOT NULL,
    spec_name    VARCHAR(100),
    options      JSONB, -- 选项详情
    subtotal     NUMERIC(10,2) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE;
CREATE INDEX idx_order_items_order ON order_items(order_id);
COMMENT ON TABLE order_items IS '订单商品明细表';
```
### order_status_log (订单状态日志表)
```sql
CREATE TABLE order_status_log (
    log_id        BIGSERIAL PRIMARY KEY,
    order_id      BIGINT NOT NULL,
    old_status    SMALLINT,
    new_status    SMALLINT NOT NULL,
    operator_type SMALLINT, -- 1:用户 2:商家 3:骑手 4:系统
    operator_id   BIGINT,
    remark        VARCHAR(200),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE order_status_log ADD CONSTRAINT fk_order_status_log_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE;
COMMENT ON TABLE order_status_log IS '订单状态日志表';
```
---
## 5️⃣ 配送模块
### delivery_riders (骑手表)
```sql
CREATE TABLE delivery_riders (
    rider_id      BIGSERIAL PRIMARY KEY,
    rider_name    VARCHAR(50) NOT NULL,
    phone         VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    id_card       VARCHAR(20),
    avatar        VARCHAR(255),
    vehicle_type  SMALLINT, -- 1:电动车 2:摩托车
    vehicle_no    VARCHAR(20),
    balance       NUMERIC(10,2) DEFAULT 0,
    status        SMALLINT DEFAULT 1, -- 0:休息 1:接单中 2:配送中
    is_online     SMALLINT DEFAULT 0,
    current_longitude NUMERIC(10,7),
    current_latitude NUMERIC(10,7),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_delivery_riders_phone ON delivery_riders(phone);
CREATE INDEX idx_delivery_riders_status ON delivery_riders(status);
CREATE INDEX idx_delivery_riders_online ON delivery_riders(is_online);
COMMENT ON TABLE delivery_riders IS '骑手表';
```
### delivery_tasks (配送任务表)
```sql
CREATE TABLE delivery_tasks (
    task_id      BIGSERIAL PRIMARY KEY,
    order_id     BIGINT NOT NULL,
    rider_id     BIGINT,
    shop_id      BIGINT NOT NULL,
    shop_address VARCHAR(200) NOT NULL,
    shop_longitude NUMERIC(10,7) NOT NULL,
    shop_latitude NUMERIC(10,7) NOT NULL,
    user_id      BIGINT NOT NULL,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    receiver_address VARCHAR(300) NOT NULL,
    receiver_longitude NUMERIC(10,7) NOT NULL,
    receiver_latitude NUMERIC(10,7) NOT NULL,
    distance     INT,
    delivery_fee NUMERIC(10,2) NOT NULL,
    rider_income NUMERIC(10,2),
    status       SMALLINT DEFAULT 0, -- 0:待接单 1:已接单 2:已取餐 3:配送中 4:已送达
    accept_time  TIMESTAMP,
    pickup_time  TIMESTAMP,
    complete_time TIMESTAMP,
    cancel_time  TIMESTAMP,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE delivery_tasks ADD CONSTRAINT fk_delivery_tasks_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE;
ALTER TABLE delivery_tasks ADD CONSTRAINT fk_delivery_tasks_rider FOREIGN KEY (rider_id) REFERENCES delivery_riders(rider_id) ON DELETE SET NULL;
CREATE INDEX idx_delivery_tasks_order ON delivery_tasks(order_id);
CREATE INDEX idx_delivery_tasks_rider ON delivery_tasks(rider_id);
CREATE INDEX idx_delivery_tasks_status ON delivery_tasks(status);
COMMENT ON TABLE delivery_tasks IS '配送任务表';
```
---
## 6️⃣ 评价模块
### order_reviews (订单评价表)
```sql
CREATE TABLE order_reviews (
    review_id    BIGSERIAL PRIMARY KEY,
    order_id     BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    shop_id      BIGINT NOT NULL,
    rider_id     BIGINT,
    shop_rating  SMALLINT, -- 1-5分
    delivery_rating SMALLINT,
    taste_rating SMALLINT,
    content      TEXT,
    images       JSONB,
    is_anonymous SMALLINT DEFAULT 0,
    like_count   INT DEFAULT 0,
    reply_content TEXT,
    reply_time   TIMESTAMP,
    status       SMALLINT DEFAULT 1, -- 0:隐藏 1:显示
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE order_reviews ADD CONSTRAINT fk_order_reviews_order FOREIGN KEY (order_id) REFERENCES orders(order_id);
ALTER TABLE order_reviews ADD CONSTRAINT fk_order_reviews_user FOREIGN KEY (user_id) REFERENCES users(user_id);
ALTER TABLE order_reviews ADD CONSTRAINT fk_order_reviews_shop FOREIGN KEY (shop_id) REFERENCES shops(shop_id);
CREATE UNIQUE INDEX uk_order_reviews_order ON order_reviews(order_id);
COMMENT ON TABLE order_reviews IS '订单评价表';
```
---
## 7️⃣ 优惠券模块
### coupons (优惠券表)
```sql
CREATE TABLE coupons (
    coupon_id    BIGSERIAL PRIMARY KEY,
    coupon_name  VARCHAR(100) NOT NULL,
    coupon_type  SMALLINT NOT NULL, -- 1:满减 2:折扣 3:免配送费
    min_amount   NUMERIC(10,2),
    discount_amount NUMERIC(10,2),
    discount_rate NUMERIC(5,2),
    max_discount NUMERIC(10,2),
    total_count  INT NOT NULL,
    received_count INT DEFAULT 0,
    used_count   INT DEFAULT 0,
    valid_type   SMALLINT NOT NULL, -- 1:固定天数 2:固定日期
    valid_days   INT,
    start_time   TIMESTAMP,
    end_time     TIMESTAMP,
    shop_id      BIGINT,
    status       SMALLINT DEFAULT 1, -- 0:已结束 1:进行中
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE coupons ADD CONSTRAINT fk_coupons_shop FOREIGN KEY (shop_id) REFERENCES shops(shop_id);
COMMENT ON TABLE coupons IS '优惠券表';
```
### user_coupons (用户优惠券表)
```sql
CREATE TABLE user_coupons (
    user_coupon_id BIGSERIAL PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    coupon_id      BIGINT NOT NULL,
    status         SMALLINT DEFAULT 0, -- 0:未使用 1:已使用 2:已过期
    order_id       BIGINT,
    receive_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    use_time       TIMESTAMP,
    expire_time    TIMESTAMP NOT NULL
);
ALTER TABLE user_coupons ADD CONSTRAINT fk_user_coupons_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
ALTER TABLE user_coupons ADD CONSTRAINT fk_user_coupons_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id);
CREATE INDEX idx_user_coupons_user ON user_coupons(user_id);
CREATE INDEX idx_user_coupons_status ON user_coupons(status);
COMMENT ON TABLE user_coupons IS '用户优惠券表';
```
---
## 8️⃣ 支付模块
### payment_records (支付记录表)
```sql
CREATE TABLE payment_records (
    payment_id    BIGSERIAL PRIMARY KEY,
    order_id      BIGINT NOT NULL,
    user_id       BIGINT NOT NULL,
    out_trade_no  VARCHAR(64) UNIQUE NOT NULL,
    transaction_no VARCHAR(64),
    pay_method    SMALLINT NOT NULL, -- 1:微信 2:支付宝
    amount        NUMERIC(10,2) NOT NULL,
    status        SMALLINT DEFAULT 0, -- 0:待支付 1:成功 2:失败 3:退款
    notify_time   TIMESTAMP,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE payment_records ADD CONSTRAINT fk_payment_records_order FOREIGN KEY (order_id) REFERENCES orders(order_id);
CREATE INDEX idx_payment_records_order ON payment_records(order_id);
COMMENT ON TABLE payment_records IS '支付记录表';
```
### refund_records (退款记录表)
```sql
CREATE TABLE refund_records (
    refund_id     BIGSERIAL PRIMARY KEY,
    order_id      BIGINT NOT NULL,
    user_id       BIGINT NOT NULL,
    payment_id    BIGINT NOT NULL,
    out_refund_no VARCHAR(64) UNIQUE NOT NULL,
    refund_no     VARCHAR(64),
    refund_amount NUMERIC(10,2) NOT NULL,
    refund_reason VARCHAR(200),
    status        SMALLINT DEFAULT 0, -- 0:退款中 1:成功 2:失败
    success_time  TIMESTAMP,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE refund_records ADD CONSTRAINT fk_refund_records_order FOREIGN KEY (order_id) REFERENCES orders(order_id);
CREATE INDEX idx_refund_records_order ON refund_records(order_id);
COMMENT ON TABLE refund_records IS '退款记录表';
```
---
## 9️⃣ 系统管理模块
### admins (管理员表)
```sql
CREATE TABLE admins (
    admin_id      BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    real_name     VARCHAR(50),
    phone         VARCHAR(20),
    role_id       INT NOT NULL,
    status        SMALLINT DEFAULT 1,
    last_login_time TIMESTAMP,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE admins IS '管理员表';
```
### notifications (消息通知表)
```sql
CREATE TABLE notifications (
    notification_id BIGSERIAL PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    notification_type SMALLINT NOT NULL, -- 1:订单 2:活动 3:系统
    title          VARCHAR(100) NOT NULL,
    content        TEXT,
    link_url       VARCHAR(255),
    is_read        SMALLINT DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE notifications ADD CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
CREATE INDEX idx_notifications_user ON notifications(user_id);
COMMENT ON TABLE notifications IS '消息通知表';
```
---
## ⚙️ 附录：自动更新 updated_at 触发器
PostgreSQL 不像 MySQL 那样支持 `ON UPDATE CURRENT_TIMESTAMP`，如果需要数据库自动维护更新时间，可以创建如下触发器函数：
```sql
-- 1. 创建更新时间函数
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';
-- 2. 为需要自动更新时间的表绑定触发器
-- 例如，为 orders 表添加触发器：
CREATE TRIGGER update_orders_modtime
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();
```