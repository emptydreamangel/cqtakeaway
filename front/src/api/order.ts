import { request } from "@/utils/request";
import type {
  Order,
  OrderItem,
  OrderStatusLog,
  CreateOrderParams,
  PageParams,
  PageResult,
} from "@/types";

const BASE_URL = "/order";
const ITEM_URL = "/order-item";
const LOG_URL = "/order-status-log";

export const orderApi = {
  // 按订单号查询
  getOrderByNo(orderNo: string): Promise<Order> {
    return request.get(`${BASE_URL}/no/${orderNo}`);
  },

  // 获取订单详情
  getOrderById(id: number): Promise<Order> {
    return request.get(`${BASE_URL}/${id}`);
  },

  // 获取用户订单列表
  getOrdersByUser(userId: number): Promise<Order[]> {
    return request.get(`${BASE_URL}/user/${userId}`);
  },

  // 用户订单分页
  getUserOrderPage(
    userId: number,
    params: PageParams
  ): Promise<PageResult<Order>> {
    return request.get(`${BASE_URL}/user/${userId}/page`, { params });
  },

  // 按状态查询用户订单
  getOrdersByUserAndStatus(userId: number, status: number): Promise<Order[]> {
    return request.get(`${BASE_URL}/user/${userId}/status/${status}`);
  },

  // 用户+状态分页
  getUserStatusOrderPage(
    userId: number,
    status: number,
    params: PageParams
  ): Promise<PageResult<Order>> {
    return request.get(`${BASE_URL}/user/${userId}/status/${status}/page`, {
      params,
    });
  },

  // 创建订单
  createOrder(params: CreateOrderParams): Promise<Order> {
    return request.post(BASE_URL, params);
  },

  // 支付订单
  payOrder(id: number, payMethod: number): Promise<Order> {
    return request.patch(`${BASE_URL}/${id}/pay`, { payMethod });
  },

  // 取消订单
  cancelOrder(id: number, cancelReason: string): Promise<Order> {
    return request.patch(`${BASE_URL}/${id}/cancel`, { cancelReason });
  },

  // 完成订单
  completeOrder(id: number): Promise<Order> {
    return request.patch(`${BASE_URL}/${id}/complete`);
  },

  // 获取用户订单数量
  getUserOrderCount(userId: number): Promise<number> {
    return request.get(`${BASE_URL}/count/user/${userId}`);
  },

  // 获取用户+状态订单数量
  getUserStatusOrderCount(userId: number, status: number): Promise<number> {
    return request.get(`${BASE_URL}/count/user/${userId}/status/${status}`);
  },
};

export const orderItemApi = {
  // 获取订单明细列表
  getItemsByOrder(orderId: number): Promise<OrderItem[]> {
    return request.get(`${ITEM_URL}/order/${orderId}`);
  },

  // 获取明细详情
  getItemById(id: number): Promise<OrderItem> {
    return request.get(`${ITEM_URL}/${id}`);
  },
};

export const orderStatusLogApi = {
  // 获取订单状态日志
  getLogsByOrder(orderId: number): Promise<OrderStatusLog[]> {
    return request.get(`${LOG_URL}/order/${orderId}`);
  },
};
