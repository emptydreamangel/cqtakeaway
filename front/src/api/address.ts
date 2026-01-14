import { request } from "@/utils/request";
import type { UserAddress } from "@/types";

const BASE_URL = "/user-address";

export const addressApi = {
  // 获取用户所有地址
  getAddressesByUser(userId: number): Promise<UserAddress[]> {
    return request.get(`${BASE_URL}/user/${userId}`);
  },

  // 获取地址详情
  getAddressById(id: number): Promise<UserAddress> {
    return request.get(`${BASE_URL}/${id}`);
  },

  // 获取默认地址
  getDefaultAddress(userId: number): Promise<UserAddress> {
    return request.get(`${BASE_URL}/user/${userId}/default`);
  },

  // 添加地址
  addAddress(
    data: Omit<UserAddress, "id" | "createTime" | "updateTime">
  ): Promise<UserAddress> {
    return request.post(BASE_URL, data);
  },

  // 更新地址
  updateAddress(id: number, data: Partial<UserAddress>): Promise<UserAddress> {
    return request.put(`${BASE_URL}/${id}`, data);
  },

  // 设为默认地址
  setDefaultAddress(id: number): Promise<void> {
    return request.put(`${BASE_URL}/${id}/set-default`);
  },

  // 删除地址
  deleteAddress(id: number): Promise<void> {
    return request.delete(`${BASE_URL}/${id}`);
  },
};
