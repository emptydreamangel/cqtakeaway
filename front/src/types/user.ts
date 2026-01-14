// 用户信息
export interface User {
  id: number;
  phone: string;
  password?: string;
  nickname: string;
  avatar?: string;
  gender?: number; // 0-未知 1-男 2-女
  birthday?: string;
  status: number; // 0-禁用 1-正常
  createTime?: string;
  updateTime?: string;
  lastLoginTime?: string;
}

// 登录请求
export interface LoginParams {
  phone: string;
  password: string;
}

// 注册请求
export interface RegisterParams {
  phone: string;
  password: string;
  nickname: string;
}

// 用户地址
export interface UserAddress {
  id: number;
  userId: number;
  contactName: string;
  contactPhone: string;
  province: string;
  city: string;
  district: string;
  detailAddress: string;
  isDefault: number; // 0-否 1-是
  createTime?: string;
  updateTime?: string;
}

// 用户收藏
export interface UserFavorite {
  id: number;
  userId: number;
  targetType: number; // 1-商家 2-商品
  targetId: number;
  createTime?: string;
}
