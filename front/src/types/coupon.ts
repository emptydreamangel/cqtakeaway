// 优惠券类型
export enum CouponType {
  FULL_REDUCTION = 1, // 满减券
  DISCOUNT = 2, // 折扣券
  NO_THRESHOLD = 3, // 无门槛券
}

// 优惠券状态
export enum CouponStatus {
  INACTIVE = 0, // 未激活
  ACTIVE = 1, // 可领取
  EXPIRED = 2, // 已过期
}

// 优惠券信息
export interface Coupon {
  id: number;
  name: string;
  shopId?: number; // 商家ID，为空表示平台券
  couponType: number;
  discountAmount?: number; // 优惠金额
  discountRate?: number; // 折扣率
  minAmount: number; // 最低消费
  maxDiscount?: number; // 最大优惠
  startTime: string;
  endTime: string;
  totalCount: number; // 总发放量
  receivedCount: number; // 已领取数量
  usedCount: number; // 已使用数量
  perLimit: number; // 每人限领
  status: number;
  description?: string;
  createTime?: string;
}

// 用户优惠券状态
export enum UserCouponStatus {
  UNUSED = 0, // 未使用
  USED = 1, // 已使用
  EXPIRED = 2, // 已过期
}

// 用户优惠券
export interface UserCoupon {
  id: number;
  userId: number;
  couponId: number;
  coupon?: Coupon;
  status: number;
  orderId?: number;
  receiveTime: string;
  useTime?: string;
  createTime?: string;
}
