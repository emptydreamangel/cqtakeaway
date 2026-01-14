// 商家信息
export interface Shop {
  shop_id: number;
  shop_name: string;
  logo?: string;
  banner?: string;
  category_id: number;
  categoryName?: string;
  phone: string;
  province: string;
  city: string;
  district: string;
  address: string;
  longitude?: number;
  latitude?: number;
  business_hours?: string;
  description?: string;
  notice?: string;
  min_order_amount: number; // 起送价
  delivery_fee: number; // 配送费
  delivery_time?: number; // 配送时间(分钟)
  rating: number; // 评分
  sales_count: number; // 月销量
  status: number; // 0-休息中 1-营业中 2-已打烊
  is_auth: number; // 0-未认证 1-已认证
  created_at?: string;
  updated_at?: string;
}

// 商家分类
export interface ShopCategory {
  category_id: number;
  category_name: string;
  icon?: string;
  sort_order: number;
  status: number;
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
