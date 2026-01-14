import { request } from "@/utils/request";
import type {
  PaymentRecord,
  RefundRecord,
  CreatePaymentParams,
  PageParams,
  PageResult,
} from "@/types";

const PAYMENT_URL = "/payment-records";
const REFUND_URL = "/refund-records";

export const paymentApi = {
  // 获取支付记录详情
  getPaymentById(id: number): Promise<PaymentRecord> {
    return request.get(`${PAYMENT_URL}/${id}`);
  },

  // 获取订单支付记录
  getPaymentByOrder(orderId: number): Promise<PaymentRecord> {
    return request.get(`${PAYMENT_URL}/order/${orderId}`);
  },

  // 获取用户支付记录
  getPaymentsByUser(userId: number): Promise<PaymentRecord[]> {
    return request.get(`${PAYMENT_URL}/user/${userId}`);
  },

  // 分页查询支付记录
  getPaymentPage(
    params: PageParams & {
      userId?: number;
      payMethod?: number;
      status?: number;
    }
  ): Promise<PageResult<PaymentRecord>> {
    return request.get(`${PAYMENT_URL}/page`, { params });
  },

  // 创建支付记录
  createPayment(params: CreatePaymentParams): Promise<PaymentRecord> {
    return request.post(PAYMENT_URL, params);
  },

  // 支付成功
  paymentSuccess(id: number, transactionId: string): Promise<void> {
    return request.put(`${PAYMENT_URL}/${id}/success`, { transactionId });
  },

  // 支付失败
  paymentFail(id: number): Promise<void> {
    return request.put(`${PAYMENT_URL}/${id}/fail`);
  },
};

export const refundApi = {
  // 获取退款记录详情
  getRefundById(id: number): Promise<RefundRecord> {
    return request.get(`${REFUND_URL}/${id}`);
  },

  // 获取订单退款记录
  getRefundByOrder(orderId: number): Promise<RefundRecord> {
    return request.get(`${REFUND_URL}/order/${orderId}`);
  },

  // 获取用户退款记录
  getRefundsByUser(userId: number): Promise<RefundRecord[]> {
    return request.get(`${REFUND_URL}/user/${userId}`);
  },

  // 分页查询退款记录
  getRefundPage(
    params: PageParams & { userId?: number; status?: number }
  ): Promise<PageResult<RefundRecord>> {
    return request.get(`${REFUND_URL}/page`, { params });
  },
};
