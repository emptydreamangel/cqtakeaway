import type { Product, ProductSpec } from "./product";
import type { Shop } from "./shop";

// 购物车项
export interface CartItem {
  id: number;
  userId: number;
  shopId: number;
  shop?: Shop;
  productId: number;
  product?: Product;
  specId?: number;
  spec?: ProductSpec;
  quantity: number;
  selected: number; // 0-未选中 1-选中
  createTime?: string;
  updateTime?: string;
}

// 添加购物车请求
export interface AddCartParams {
  userId: number;
  shopId: number;
  productId: number;
  specId?: number;
  quantity: number;
}

// 更新购物车数量请求
export interface UpdateCartQuantityParams {
  quantity: number;
}
