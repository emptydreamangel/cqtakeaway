// 商家信息
export interface Shop {
  id: number;
  name: string;
  logo?: string;
  banner?: string;
  categoryId: number;
  categoryName?: string;
  phone: string;
  province: string;
  city: string;
  district: string;
  address: string;
  longitude?: number;
  latitude?: number;
  businessHours?: string;
  description?: string;
  notice?: string;
  minPrice: number; // 起送价
  deliveryFee: number; // 配送费
  deliveryTime?: number; // 配送时间(分钟)
  rating: number; // 评分
  monthlySales: number; // 月销量
  status: number; // 0-休息中 1-营业中 2-已打烊
  isAuthenticated: number; // 0-未认证 1-已认证
  createTime?: string;
  updateTime?: string;
}

// 商家分类
export interface ShopCategory {
  id: number;
  name: string;
  icon?: string;
  parentId: number;
  level: number;
  sort: number;
  createTime?: string;
}

// 商家图片
export interface ShopImage {
  id: number;
  shopId: number;
  imageUrl: string;
  imageType: number; // 1-门店照 2-环境照 3-资质照
  sort: number;
  createTime?: string;
}
