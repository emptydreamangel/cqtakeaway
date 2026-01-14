import { request } from "@/utils/request";
import type {
  User,
  LoginParams,
  RegisterParams,
  PageParams,
  PageResult,
} from "@/types";

const BASE_URL = "/user";

export const userApi = {
  // 用户登录
  login(params: LoginParams): Promise<User> {
    return request.post(`${BASE_URL}/login`, params);
  },

  // 用户注册
  register(params: RegisterParams): Promise<User> {
    return request.post(`${BASE_URL}/register`, params);
  },

  // 根据ID获取用户信息
  getUserById(id: number): Promise<User> {
    return request.get(`${BASE_URL}/${id}`);
  },

  // 根据手机号获取用户
  getUserByPhone(phone: string): Promise<User> {
    return request.get(`${BASE_URL}/phone/${phone}`);
  },

  // 更新用户信息
  updateUser(id: number, data: Partial<User>): Promise<User> {
    return request.put(`${BASE_URL}/${id}`, data);
  },

  // 修改密码
  updatePassword(id: number, newPassword: string): Promise<void> {
    return request.patch(`${BASE_URL}/${id}/password`, { newPassword });
  },

  // 检查手机号是否存在
  checkPhone(phone: string): Promise<boolean> {
    return request.get(`${BASE_URL}/check/phone/${phone}`);
  },

  // 分页查询用户
  getUserPage(
    params: PageParams & { nickname?: string; phone?: string }
  ): Promise<PageResult<User>> {
    return request.get(`${BASE_URL}/page`, { params });
  },
};
