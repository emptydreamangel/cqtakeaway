import { request } from "@/utils/request";
import type {
  CartItem,
  AddCartParams,
  UpdateCartQuantityParams,
} from "@/types";

const BASE_URL = "/shopping-cart";

export const cartApi = {
  // 获取用户购物车
  getCart(userId: number): Promise<CartItem[]> {
    return request.get(`${BASE_URL}/user/${userId}`);
  },

  // 获取指定商家购物车
  getCartByShop(userId: number, shopId: number): Promise<CartItem[]> {
    return request.get(`${BASE_URL}/user/${userId}/shop/${shopId}`);
  },

  // 获取购物车项详情
  getCartItemById(id: number): Promise<CartItem> {
    return request.get(`${BASE_URL}/${id}`);
  },

  // 添加到购物车
  addToCart(params: AddCartParams): Promise<CartItem> {
    return request.post(BASE_URL, params);
  },

  // 更新购物车
  updateCartItem(id: number, data: Partial<CartItem>): Promise<CartItem> {
    return request.put(`${BASE_URL}/${id}`, data);
  },

  // 更新数量
  updateQuantity(id: number, params: UpdateCartQuantityParams): Promise<void> {
    return request.put(`${BASE_URL}/${id}/quantity`, params);
  },

  // 删除购物车项
  removeFromCart(id: number): Promise<void> {
    return request.delete(`${BASE_URL}/${id}`);
  },

  // 清空购物车
  clearCart(userId: number): Promise<void> {
    return request.delete(`${BASE_URL}/user/${userId}/clear`);
  },

  // 获取购物车商品数量
  getCartCount(userId: number): Promise<number> {
    return request.get(`${BASE_URL}/user/${userId}/count`);
  },
};
