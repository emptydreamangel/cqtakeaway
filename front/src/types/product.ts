// 商品信息
export interface Product {
  id: number;
  shopId: number;
  categoryId: number;
  categoryName?: string;
  name: string;
  image?: string;
  description?: string;
  price: number;
  originalPrice?: number;
  stock: number;
  sales: number;
  status: number; // 0-下架 1-上架
  sort: number;
  createTime?: string;
  updateTime?: string;
  specs?: ProductSpec[]; // 商品规格
}

// 商品分类
export interface ProductCategory {
  id: number;
  shopId: number;
  name: string;
  sort: number;
  createTime?: string;
}

// 商品规格
export interface ProductSpec {
  id: number;
  productId: number;
  name: string;
  price: number;
  stock: number;
  status: number; // 0-禁用 1-可用
  createTime?: string;
}
