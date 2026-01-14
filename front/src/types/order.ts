import type { Product, ProductSpec } from "./product";
import type { Shop } from "./shop";

// 订单状态
export enum OrderStatus {
  PENDING_PAYMENT = 0, // 待支付
  PENDING_ACCEPT = 1, // 待接单
  PENDING_DELIVERY = 2, // 待配送
  DELIVERING = 3, // 配送中
  COMPLETED = 4, // 已完成
  CANCELLED = 5, // 已取消
}

// 支付方式
export enum PayMethod {
  WECHAT = 1, // 微信支付
  ALIPAY = 2, // 支付宝
  BALANCE = 3, // 余额支付
}

// 订单信息
export interface Order {
  id: number;
  orderNo: string;
  userId: number;
  shopId: number;
  shop?: Shop;
  addressId: number;
  contactName: string;
  contactPhone: string;
  deliveryAddress: string;
  totalAmount: number; // 商品总价
  deliveryFee: number; // 配送费
  discountAmount: number; // 优惠金额
  payAmount: number; // 实付金额
  payMethod?: number;
  payTime?: string;
  status: number;
  remark?: string;
  cancelReason?: string;
  expectedDeliveryTime?: string;
  actualDeliveryTime?: string;
  createTime?: string;
  updateTime?: string;
  items?: OrderItem[];
}

// 订单明细
export interface OrderItem {
  id: number;
  orderId: number;
  productId: number;
  product?: Product;
  specId?: number;
  spec?: ProductSpec;
  productName: string;
  productImage?: string;
  specName?: string;
  price: number;
  quantity: number;
  totalPrice: number;
  createTime?: string;
}

// 订单状态日志
export interface OrderStatusLog {
  id: number;
  orderId: number;
  fromStatus: number;
  toStatus: number;
  operatorType: number; // 1-用户 2-商家 3-骑手 4-系统
  operatorId?: number;
  remark?: string;
  createTime?: string;
}

// 创建订单请求
export interface CreateOrderParams {
  userId: number;
  shopId: number;
  addressId: number;
  remark?: string;
  items: CreateOrderItemParams[];
  couponId?: number;
}

export interface CreateOrderItemParams {
  productId: number;
  specId?: number;
  quantity: number;
}
