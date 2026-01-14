# 积天外卖系统 - API接口文档

> 基于 Spring Boot 3.2.1 + MyBatis-Plus 3.5.5  
> 服务地址：http://localhost:8080  
> 最后更新：2026-01-14

---

## 目录

1. [用户模块](#1-用户模块)
2. [商家模块](#2-商家模块)
3. [商品模块](#3-商品模块)
4. [订单模块](#4-订单模块)
5. [配送模块](#5-配送模块)
6. [评价模块](#6-评价模块)
7. [优惠券模块](#7-优惠券模块)
8. [支付模块](#8-支付模块)
9. [系统管理模块](#9-系统管理模块)

---

## 1. 用户模块

**基础路径**: `/api/user`

### 1.1 用户管理 (UserController)

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/list` | 获取所有用户列表 | - |
| GET | `/{id}` | 根据ID获取用户信息 | id: 用户ID |
| GET | `/phone/{phone}` | 根据手机号获取用户 | phone: 手机号 |
| GET | `/page` | 分页查询用户 | current, size, nickname, phone, gender, status |
| POST | `/register` | 用户注册 | Body: User对象 |
| POST | `/login` | 用户登录 | Body: {phone, password} |
| PUT | `/{id}` | 更新用户信息 | id + Body: User对象 |
| PATCH | `/{id}/status` | 更新用户状态 | id + Body: {status} |
| PATCH | `/{id}/password` | 修改密码 | id + Body: {newPassword} |
| DELETE | `/{id}` | 删除用户 | id: 用户ID |
| GET | `/gender/{gender}` | 按性别查询用户 | gender: 性别 |
| GET | `/status/{status}` | 按状态查询用户 | status: 状态 |
| GET | `/check/phone/{phone}` | 检查手机号是否存在 | phone: 手机号 |
| GET | `/recent-login` | 最近登录用户 | limit: 数量限制 |
| GET | `/active` | 活跃用户列表 | days: 天数 |
| GET | `/stats/gender` | 按性别统计用户 | - |
| GET | `/stats/status` | 按状态统计用户 | - |

### 1.2 用户地址管理 (UserAddressController)

**基础路径**: `/api/user-address`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/user/{userId}` | 获取用户所有地址 | userId: 用户ID |
| GET | `/{id}` | 获取地址详情 | id: 地址ID |
| GET | `/user/{userId}/default` | 获取默认地址 | userId: 用户ID |
| POST | `` | 添加地址 | Body: UserAddress对象 |
| PUT | `/{id}` | 更新地址 | id + Body: UserAddress对象 |
| PUT | `/{id}/set-default` | 设为默认地址 | id: 地址ID |
| DELETE | `/{id}` | 删除地址 | id: 地址ID |

### 1.3 用户收藏管理 (UserFavoriteController)

**基础路径**: `/api/user-favorite`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/user/{userId}` | 获取用户收藏 | userId: 用户ID |
| GET | `/user/{userId}/shops` | 收藏的商家 | userId: 用户ID |
| GET | `/user/{userId}/products` | 收藏的商品 | userId: 用户ID |
| GET | `/check` | 检查是否收藏 | userId, targetType, targetId |
| POST | `` | 添加收藏 | Body: UserFavorite对象 |
| DELETE | `/{id}` | 取消收藏 | id: 收藏ID |
| DELETE | `/user/{userId}/target` | 取消指定收藏 | userId, targetType, targetId |
| GET | `/page` | 分页查询收藏 | current, size, userId, targetType |

### 1.4 购物车管理 (ShoppingCartController)

**基础路径**: `/api/shopping-cart`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/user/{userId}` | 获取购物车 | userId: 用户ID |
| GET | `/user/{userId}/shop/{shopId}` | 指定商家购物车 | userId, shopId |
| GET | `/{id}` | 购物车项详情 | id: 购物车ID |
| POST | `` | 添加到购物车 | Body: ShoppingCart对象 |
| PUT | `/{id}` | 更新购物车 | id + Body: ShoppingCart对象 |
| PUT | `/{id}/quantity` | 更新数量 | id + Body: {quantity} |
| DELETE | `/{id}` | 删除购物车项 | id: 购物车ID |
| DELETE | `/user/{userId}/clear` | 清空购物车 | userId: 用户ID |
| GET | `/user/{userId}/count` | 购物车商品数量 | userId: 用户ID |

---

## 2. 商家模块

**基础路径**: `/api/shop`

### 2.1 商家管理 (ShopController)

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/business` | 营业中的商家 | - |
| GET | `/category/{categoryId}` | 按分类查询商家 | categoryId: 分类ID |
| GET | `/status/{status}` | 按状态查询商家 | status: 状态 |
| GET | `/{id}` | 商家详情 | id: 商家ID |
| GET | `/search` | 搜索商家 | name: 商家名称 |
| GET | `/region` | 按地区查询商家 | province, city, district |
| GET | `/location` | 按位置范围查询 | minLng, maxLng, minLat, maxLat |
| GET | `/top/rating` | 评分最高商家 | limit: 数量 |
| GET | `/top/sales` | 销量最高商家 | limit: 数量 |
| GET | `/page` | 分页查询商家 | pageNum, pageSize |
| GET | `/business/page` | 分页查询营业商家 | pageNum, pageSize |
| GET | `/category/{categoryId}/page` | 分类商家分页 | categoryId, pageNum, pageSize |
| POST | `` | 添加商家 | Body: Shop对象 |
| PUT | `/{id}` | 更新商家 | id + Body: Shop对象 |
| DELETE | `/{id}` | 删除商家 | id: 商家ID |
| DELETE | `/batch` | 批量删除商家 | Body: {shopIds: []} |
| PATCH | `/{id}/status` | 更新商家状态 | id + Body: {status} |
| PATCH | `/{id}/business` | 设为营业中 | id: 商家ID |
| PATCH | `/{id}/rest` | 设为休息中 | id: 商家ID |
| PATCH | `/{id}/closed` | 设为打烊 | id: 商家ID |
| PATCH | `/{id}/authenticate` | 认证商家 | id: 商家ID |
| PATCH | `/{id}/unauthenticate` | 取消认证 | id: 商家ID |
| PATCH | `/{id}/sales` | 增加销量 | id + Body: {increment} |
| GET | `/stats/count` | 统计数据 | - |
| GET | `/stats/category/{categoryId}` | 分类商家数量 | categoryId: 分类ID |

### 2.2 商家分类管理 (ShopCategoryController)

**基础路径**: `/api/shop-category`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/list` | 所有商家分类 | - |
| GET | `/{id}` | 分类详情 | id: 分类ID |
| GET | `/level/{level}` | 按层级查询 | level: 层级 |
| GET | `/parent/{parentId}` | 子分类列表 | parentId: 父分类ID |
| POST | `` | 添加分类 | Body: ShopCategory对象 |
| PUT | `/{id}` | 更新分类 | id + Body: ShopCategory对象 |
| DELETE | `/{id}` | 删除分类 | id: 分类ID |

### 2.3 商家图片管理 (ShopImageController)

**基础路径**: `/api/shop-image`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/shop/{shopId}` | 商家所有图片 | shopId: 商家ID |
| GET | `/shop/{shopId}/type/{imageType}` | 按类型查询图片 | shopId, imageType |
| GET | `/{id}` | 图片详情 | id: 图片ID |
| POST | `` | 上传图片 | Body: ShopImage对象 |
| PUT | `/{id}` | 更新图片 | id + Body: ShopImage对象 |
| DELETE | `/{id}` | 删除图片 | id: 图片ID |
| DELETE | `/shop/{shopId}/batch` | 批量删除 | shopId + Body: {imageIds: []} |

---

## 3. 商品模块

**基础路径**: `/api/product`

### 3.1 商品管理 (ProductController)

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/shop/{shopId}` | 商家商品列表 | shopId: 商家ID |
| GET | `/category/{categoryId}` | 分类商品列表 | categoryId: 分类ID |
| GET | `/shop/{shopId}/category/{categoryId}` | 商家+分类商品 | shopId, categoryId |
| GET | `/{id}` | 商品详情 | id: 商品ID |
| GET | `/search` | 搜索商品 | name: 商品名称 |
| GET | `/shop/{shopId}/search` | 商家内搜索 | shopId, name |
| GET | `/shop/{shopId}/hot` | 热销商品 | shopId, limit |
| GET | `/shop/{shopId}/new` | 新品列表 | shopId, limit |
| GET | `/page` | 分页查询商品 | pageNum, pageSize |
| GET | `/shop/{shopId}/page` | 商家商品分页 | shopId, pageNum, pageSize |
| POST | `` | 添加商品 | Body: Product对象 |
| PUT | `/{id}` | 更新商品 | id + Body: Product对象 |
| DELETE | `/{id}` | 删除商品 | id: 商品ID |
| DELETE | `/batch` | 批量删除商品 | Body: {productIds: []} |
| PATCH | `/{id}/status` | 更新商品状态 | id + Body: {status} |
| PATCH | `/{id}/on-sale` | 上架商品 | id: 商品ID |
| PATCH | `/{id}/off-sale` | 下架商品 | id: 商品ID |
| PATCH | `/{id}/stock` | 更新库存 | id + Body: {stock} |
| PATCH | `/{id}/sales` | 增加销量 | id + Body: {increment} |
| GET | `/stats/shop/{shopId}/count` | 商家商品数量 | shopId: 商家ID |
| GET | `/stats/shop/{shopId}/on-sale` | 在售商品数量 | shopId: 商家ID |

### 3.2 商品分类管理 (ProductCategoryController)

**基础路径**: `/api/product-category`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/list` | 所有商品分类 | - |
| GET | `/{id}` | 分类详情 | id: 分类ID |
| GET | `/shop/{shopId}` | 商家分类列表 | shopId: 商家ID |
| POST | `` | 添加分类 | Body: ProductCategory对象 |
| PUT | `/{id}` | 更新分类 | id + Body: ProductCategory对象 |
| DELETE | `/{id}` | 删除分类 | id: 分类ID |

### 3.3 商品规格管理 (ProductSpecController)

**基础路径**: `/api/product-spec`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/product/{productId}` | 商品规格列表 | productId: 商品ID |
| GET | `/{id}` | 规格详情 | id: 规格ID |
| GET | `/product/{productId}/available` | 可用规格 | productId: 商品ID |
| POST | `` | 添加规格 | Body: ProductSpec对象 |
| PUT | `/{id}` | 更新规格 | id + Body: ProductSpec对象 |
| DELETE | `/{id}` | 删除规格 | id: 规格ID |
| PATCH | `/{id}/stock` | 更新库存 | id + Body: {stock} |

---

## 4. 订单模块

**基础路径**: `/api/order`

### 4.1 订单管理 (OrderController)

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/no/{orderNo}` | 按订单号查询 | orderNo: 订单号 |
| GET | `/{id}` | 订单详情 | id: 订单ID |
| GET | `/user/{userId}` | 用户订单列表 | userId: 用户ID |
| GET | `/user/{userId}/page` | 用户订单分页 | userId, pageNum, pageSize |
| GET | `/shop/{shopId}` | 商家订单列表 | shopId: 商家ID |
| GET | `/shop/{shopId}/page` | 商家订单分页 | shopId, pageNum, pageSize |
| GET | `/status/{status}` | 按状态查询订单 | status: 订单状态 |
| GET | `/user/{userId}/status/{status}` | 用户+状态订单 | userId, status |
| GET | `/user/{userId}/status/{status}/page` | 用户+状态分页 | userId, status, pageNum, pageSize |
| GET | `/shop/{shopId}/status/{status}` | 商家+状态订单 | shopId, status |
| GET | `/shop/{shopId}/status/{status}/page` | 商家+状态分页 | shopId, status, pageNum, pageSize |
| POST | `` | 创建订单 | Body: Order对象 |
| PUT | `/{id}` | 更新订单 | id + Body: Order对象 |
| PATCH | `/{id}/pay` | 支付订单 | id + Body: {payMethod} |
| PATCH | `/{id}/accept` | 商家接单 | id: 订单ID |
| PATCH | `/{id}/dispatch` | 配送员接单 | id: 订单ID |
| PATCH | `/{id}/complete` | 完成订单 | id: 订单ID |
| PATCH | `/{id}/cancel` | 取消订单 | id + Body: {cancelReason} |
| GET | `/count/user/{userId}` | 用户订单数量 | userId: 用户ID |
| GET | `/count/user/{userId}/status/{status}` | 用户+状态数量 | userId, status |
| GET | `/count/shop/{shopId}` | 商家订单数量 | shopId: 商家ID |
| GET | `/count/shop/{shopId}/status/{status}` | 商家+状态数量 | shopId, status |
| GET | `/time-range` | 时间范围订单 | startTime, endTime |
| GET | `/user/{userId}/time-range` | 用户时间范围 | userId, startTime, endTime |
| GET | `/shop/{shopId}/time-range` | 商家时间范围 | shopId, startTime, endTime |

### 4.2 订单明细管理 (OrderItemController)

**基础路径**: `/api/order-item`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/order/{orderId}` | 订单明细列表 | orderId: 订单ID |
| GET | `/{id}` | 明细详情 | id: 明细ID |
| POST | `` | 添加明细 | Body: OrderItem对象 |
| PUT | `/{id}` | 更新明细 | id + Body: OrderItem对象 |
| DELETE | `/{id}` | 删除明细 | id: 明细ID |

### 4.3 订单状态日志 (OrderStatusLogController)

**基础路径**: `/api/order-status-log`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/order/{orderId}` | 订单状态日志 | orderId: 订单ID |
| GET | `/{id}` | 日志详情 | id: 日志ID |
| POST | `` | 添加日志 | Body: OrderStatusLog对象 |

---

## 5. 配送模块

**基础路径**: `/api/delivery-rider`, `/api/delivery-task`

### 5.1 骑手管理 (DeliveryRiderController)

**基础路径**: `/api/delivery-rider`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/list` | 所有骑手 | - |
| GET | `/{id}` | 骑手详情 | id: 骑手ID |
| GET | `/phone/{phone}` | 按手机号查询 | phone: 手机号 |
| GET | `/status/{status}` | 按状态查询 | status: 状态 |
| GET | `/available` | 可接单骑手 | - |
| GET | `/region` | 按地区查询 | province, city, district |
| GET | `/page` | 分页查询骑手 | pageNum, pageSize |
| POST | `` | 添加骑手 | Body: DeliveryRider对象 |
| PUT | `/{id}` | 更新骑手 | id + Body: DeliveryRider对象 |
| DELETE | `/{id}` | 删除骑手 | id: 骑手ID |
| PATCH | `/{id}/status` | 更新状态 | id + Body: {status} |
| PATCH | `/{id}/online` | 上线 | id: 骑手ID |
| PATCH | `/{id}/offline` | 下线 | id: 骑手ID |
| PATCH | `/{id}/rating` | 更新评分 | id + Body: {rating} |
| GET | `/stats/count` | 统计数据 | - |
| GET | `/stats/region` | 地区分布 | province, city |

### 5.2 配送任务管理 (DeliveryTaskController)

**基础路径**: `/api/delivery-task`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/order/{orderId}` | 订单配送任务 | orderId: 订单ID |
| GET | `/rider/{riderId}` | 骑手任务列表 | riderId: 骑手ID |
| GET | `/rider/{riderId}/status/{status}` | 骑手+状态任务 | riderId, status |
| GET | `/{id}` | 任务详情 | id: 任务ID |
| GET | `/status/{status}` | 按状态查询 | status: 任务状态 |
| GET | `/page` | 分页查询任务 | pageNum, pageSize |
| POST | `` | 创建任务 | Body: DeliveryTask对象 |
| PUT | `/{id}` | 更新任务 | id + Body: DeliveryTask对象 |
| PATCH | `/{id}/assign` | 分配骑手 | id + Body: {riderId} |
| PATCH | `/{id}/pickup` | 取货 | id: 任务ID |
| PATCH | `/{id}/delivering` | 配送中 | id: 任务ID |
| PATCH | `/{id}/complete` | 完成配送 | id: 任务ID |
| PATCH | `/{id}/cancel` | 取消任务 | id + Body: {cancelReason} |
| GET | `/count/rider/{riderId}` | 骑手任务数量 | riderId: 骑手ID |
| GET | `/count/rider/{riderId}/status/{status}` | 骑手+状态数量 | riderId, status |

---

## 6. 评价模块

**基础路径**: `/api/order-review`

### 6.1 订单评价 (OrderReviewController)

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/order/{orderId}` | 订单评价 | orderId: 订单ID |
| GET | `/shop/{shopId}` | 商家评价列表 | shopId: 商家ID |
| GET | `/shop/{shopId}/page` | 商家评价分页 | shopId, pageNum, pageSize |
| GET | `/user/{userId}` | 用户评价列表 | userId: 用户ID |
| GET | `/user/{userId}/page` | 用户评价分页 | userId, pageNum, pageSize |
| GET | `/{id}` | 评价详情 | id: 评价ID |
| GET | `/shop/{shopId}/rating/{rating}` | 按评分查询 | shopId, rating |
| GET | `/shop/{shopId}/with-reply` | 有回复的评价 | shopId |
| GET | `/shop/{shopId}/no-reply` | 未回复的评价 | shopId |
| POST | `` | 添加评价 | Body: OrderReview对象 |
| PUT | `/{id}` | 更新评价 | id + Body: OrderReview对象 |
| PUT | `/{id}/reply` | 商家回复 | id + Body: {shopReply} |
| DELETE | `/{id}` | 删除评价 | id: 评价ID |
| GET | `/shop/{shopId}/stats/rating` | 评分统计 | shopId: 商家ID |
| GET | `/shop/{shopId}/stats/count` | 评价数量统计 | shopId: 商家ID |
| GET | `/shop/{shopId}/avg-rating` | 平均评分 | shopId: 商家ID |
| GET | `/shop/{shopId}/recent` | 最近评价 | shopId, limit |

---

## 7. 优惠券模块

**基础路径**: `/api/coupons`, `/api/user-coupons`

### 7.1 优惠券管理 (CouponController)

**基础路径**: `/api/coupons`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/{couponId}` | 优惠券详情 | couponId: 优惠券ID |
| GET | `/page` | 分页查询优惠券 | pageNum, pageSize |
| GET | `/shop/{shopId}` | 商家优惠券 | shopId: 商家ID |
| GET | `/platform` | 平台优惠券 | - |
| GET | `/type/{couponType}` | 按类型查询 | couponType: 类型 |
| GET | `/available` | 可用优惠券 | - |
| GET | `/shop/{shopId}/available` | 商家可用券 | shopId: 商家ID |
| PUT | `/{couponId}/receive` | 领取优惠券 | couponId + Body: {userId} |
| PUT | `/{couponId}/use` | 使用优惠券 | couponId + Body: {userId} |
| PUT | `/{couponId}/status` | 更新状态 | couponId + Body: {status} |
| PUT | `/{couponId}` | 更新优惠券 | couponId + Body: Coupon对象 |
| DELETE | `/{couponId}` | 删除优惠券 | couponId: 优惠券ID |
| GET | `/stats/shop/{shopId}/count` | 商家券数量 | shopId: 商家ID |
| GET | `/stats/platform/count` | 平台券数量 | - |
| GET | `/expiring` | 即将过期券 | days: 天数 |
| GET | `/expired` | 已过期券 | - |
| PUT | `/expired/update-status` | 更新过期券状态 | - |
| GET | `/{couponId}/usage-stats` | 使用统计 | couponId: 优惠券ID |
| POST | `/batch/query` | 批量查询券 | Body: {couponIds: []} |

### 7.2 用户优惠券 (UserCouponController)

**基础路径**: `/api/user-coupons`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| POST | `/receive` | 领取优惠券 | Body: {userId, couponId} |
| PUT | `/use` | 使用优惠券 | Body: {id, orderId} |
| GET | `/user/{userId}` | 用户券列表 | userId: 用户ID |
| GET | `/user/{userId}/available` | 用户可用券 | userId: 用户ID |
| GET | `/page` | 分页查询用户券 | current, size, userId, status |
| GET | `/{id}` | 用户券详情 | id: 用户券ID |

---

## 8. 支付模块

**基础路径**: `/api/payment-records`, `/api/refund-records`

### 8.1 支付记录管理 (PaymentRecordController)

**基础路径**: `/api/payment-records`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/{id}` | 支付记录详情 | id: 记录ID |
| GET | `/order/{orderId}` | 订单支付记录 | orderId: 订单ID |
| GET | `/user/{userId}` | 用户支付记录 | userId: 用户ID |
| GET | `/out-trade-no/{outTradeNo}` | 按商户订单号查询 | outTradeNo: 商户订单号 |
| GET | `/page` | 分页查询记录 | current, size, userId, payMethod, status |
| POST | `` | 创建支付记录 | Body: PaymentRecord对象 |
| PUT | `/{id}/success` | 支付成功 | id + Body: {transactionId} |
| PUT | `/{id}/fail` | 支付失败 | id: 记录ID |
| GET | `/stats/status` | 按状态统计 | - |
| GET | `/stats/method` | 按支付方式统计 | - |

### 8.2 退款记录管理 (RefundRecordController)

**基础路径**: `/api/refund-records`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/{id}` | 退款记录详情 | id: 记录ID |
| GET | `/order/{orderId}` | 订单退款记录 | orderId: 订单ID |
| GET | `/user/{userId}` | 用户退款记录 | userId: 用户ID |
| GET | `/out-refund-no/{outRefundNo}` | 按退款单号查询 | outRefundNo: 退款单号 |
| GET | `/page` | 分页查询记录 | current, size, userId, status |
| POST | `` | 创建退款记录 | Body: RefundRecord对象 |
| PUT | `/{id}/success` | 退款成功 | id + Body: {refundId} |
| PUT | `/{id}/fail` | 退款失败 | id: 记录ID |
| GET | `/stats/status` | 按状态统计 | - |

---

## 9. 系统管理模块

**基础路径**: `/api/admin`, `/api/notifications`

### 9.1 管理员管理 (AdminController)

**基础路径**: `/api/admin`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/list` | 所有管理员 | - |
| GET | `/{id}` | 管理员详情 | id: 管理员ID |
| GET | `/username/{username}` | 按用户名查询 | username: 用户名 |
| GET | `/phone/{phone}` | 按手机号查询 | phone: 手机号 |
| GET | `/page` | 分页查询管理员 | pageNum, pageSize |
| POST | `/login` | 管理员登录 | Body: {username, password} |
| POST | `` | 添加管理员 | Body: Admin对象 |
| PUT | `/{id}` | 更新管理员 | id + Body: Admin对象 |
| DELETE | `/{id}` | 删除管理员 | id: 管理员ID |
| PATCH | `/{id}/password` | 修改密码 | id + Body: {password} |

### 9.2 消息通知管理 (NotificationController)

**基础路径**: `/api/notifications`

| 方法 | 路径 | 功能 | 参数 |
|------|------|------|------|
| GET | `/{id}` | 通知详情 | id: 通知ID |
| GET | `/user/{userId}` | 用户通知列表 | userId: 用户ID |
| GET | `/user/{userId}/unread` | 未读通知 | userId: 用户ID |
| GET | `/user/{userId}/type/{notificationType}` | 按类型查询 | userId, notificationType |
| GET | `/page` | 分页查询通知 | current, size, userId, notificationType, isRead |
| POST | `` | 创建通知 | Body: Notification对象 |
| PUT | `/{id}/read` | 标记已读 | id: 通知ID |
| PUT | `/user/{userId}/read-all` | 全部已读 | userId: 用户ID |
| DELETE | `/{id}` | 删除通知 | id: 通知ID |
| DELETE | `/user/{userId}/clear` | 清空通知 | userId: 用户ID |
| GET | `/user/{userId}/count/unread` | 未读数量 | userId: 用户ID |
| GET | `/user/{userId}/count/total` | 总通知数 | userId: 用户ID |
| GET | `/user/{userId}/recent` | 最近通知 | userId, limit |
| POST | `/batch` | 批量发送通知 | Body: {userIds: [], ...} |

---

## 附录

### A. 订单状态码

| 状态码 | 说明 |
|--------|------|
| 0 | 待支付 |
| 1 | 待接单 |
| 2 | 待配送 |
| 3 | 配送中 |
| 4 | 已完成 |
| 5 | 已取消 |

### B. 支付方式

| 代码 | 说明 |
|------|------|
| 1 | 微信支付 |
| 2 | 支付宝 |
| 3 | 余额支付 |

### C. 优惠券类型

| 代码 | 说明 |
|------|------|
| 1 | 满减券 |
| 2 | 折扣券 |
| 3 | 无门槛券 |

### D. 通知类型

| 代码 | 说明 |
|------|------|
| 1 | 订单通知 |
| 2 | 系统通知 |
| 3 | 促销通知 |

---

## 开发注意事项

1. **认证方式**: 当前使用Spring Security基础认证，生产环境需要实现JWT或OAuth2
2. **分页参数**: 统一使用`current`/`pageNum`表示当前页，`size`/`pageSize`表示每页大小
3. **响应格式**: 成功返回200状态码和数据，错误返回对应状态码和`{message: "错误信息"}`
4. **日期格式**: 使用ISO 8601格式，如`2026-01-14T15:30:00`
5. **BigDecimal字段**: 金额类字段使用BigDecimal类型，避免精度丢失

---

**文档版本**: v1.0  
**生成时间**: 2026-01-14  
**技术栈**: Spring Boot 3.2.1 + MyBatis-Plus 3.5.5 + PostgreSQL
