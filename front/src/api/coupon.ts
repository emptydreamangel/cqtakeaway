import { request } from "@/utils/request";
import type { Coupon, UserCoupon, PageParams, PageResult } from "@/types";

const COUPON_URL = "/coupons";
const USER_COUPON_URL = "/user-coupons";

export const couponApi = {
  // 获取优惠券详情
  getCouponById(id: number): Promise<Coupon> {
    return request.get(`${COUPON_URL}/${id}`);
  },

  // 分页查询优惠券
  getCouponPage(params: PageParams): Promise<PageResult<Coupon>> {
    return request.get(`${COUPON_URL}/page`, { params });
  },

  // 获取商家优惠券
  getCouponsByShop(shopId: number): Promise<Coupon[]> {
    return request.get(`${COUPON_URL}/shop/${shopId}`);
  },

  // 获取平台优惠券
  getPlatformCoupons(): Promise<Coupon[]> {
    return request.get(`${COUPON_URL}/platform`);
  },

  // 获取可用优惠券
  getAvailableCoupons(): Promise<Coupon[]> {
    return request.get(`${COUPON_URL}/available`);
  },

  // 获取商家可用优惠券
  getShopAvailableCoupons(shopId: number): Promise<Coupon[]> {
    return request.get(`${COUPON_URL}/shop/${shopId}/available`);
  },
};

export const userCouponApi = {
  // 领取优惠券
  receiveCoupon(userId: number, couponId: number): Promise<UserCoupon> {
    return request.post(`${USER_COUPON_URL}/receive`, { userId, couponId });
  },

  // 使用优惠券
  useCoupon(id: number, orderId: number): Promise<void> {
    return request.put(`${USER_COUPON_URL}/use`, { id, orderId });
  },

  // 获取用户优惠券列表
  getCouponsByUser(userId: number): Promise<UserCoupon[]> {
    return request.get(`${USER_COUPON_URL}/user/${userId}`);
  },

  // 获取用户可用优惠券
  getAvailableCouponsByUser(userId: number): Promise<UserCoupon[]> {
    return request.get(`${USER_COUPON_URL}/user/${userId}/available`);
  },

  // 分页查询用户优惠券
  getUserCouponPage(
    params: PageParams & { userId: number; status?: number }
  ): Promise<PageResult<UserCoupon>> {
    return request.get(`${USER_COUPON_URL}/page`, { params });
  },

  // 获取用户优惠券详情
  getUserCouponById(id: number): Promise<UserCoupon> {
    return request.get(`${USER_COUPON_URL}/${id}`);
  },
};
