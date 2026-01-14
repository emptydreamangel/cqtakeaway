// 支付状态
export enum PaymentStatus {
  PENDING = 0, // 待支付
  SUCCESS = 1, // 支付成功
  FAILED = 2, // 支付失败
}

// 支付记录
export interface PaymentRecord {
  id: number;
  orderId: number;
  userId: number;
  outTradeNo: string; // 商户订单号
  transactionId?: string; // 第三方交易号
  payMethod: number;
  payAmount: number;
  status: number;
  payTime?: string;
  createTime?: string;
  updateTime?: string;
}

// 创建支付请求
export interface CreatePaymentParams {
  orderId: number;
  userId: number;
  payMethod: number;
  payAmount: number;
}

// 退款状态
export enum RefundStatus {
  PENDING = 0, // 待退款
  SUCCESS = 1, // 退款成功
  FAILED = 2, // 退款失败
}

// 退款记录
export interface RefundRecord {
  id: number;
  orderId: number;
  userId: number;
  outRefundNo: string; // 退款单号
  refundId?: string; // 第三方退款单号
  refundAmount: number;
  refundReason?: string;
  status: number;
  refundTime?: string;
  createTime?: string;
  updateTime?: string;
}
