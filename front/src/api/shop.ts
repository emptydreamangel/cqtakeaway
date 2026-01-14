import { request } from "@/utils/request";
import type { Shop, ShopCategory, PageParams, PageResult } from "@/types";

const BASE_URL = "/shop";
const CATEGORY_URL = "/shop-category";

export const shopApi = {
  // 获取营业中的商家
  getBusinessShops(): Promise<Shop[]> {
    return request.get(`${BASE_URL}/business`);
  },

  // 按分类查询商家
  getShopsByCategory(categoryId: number): Promise<Shop[]> {
    return request.get(`${BASE_URL}/category/${categoryId}`);
  },

  // 获取商家详情
  getShopById(id: number): Promise<Shop> {
    return request.get(`${BASE_URL}/${id}`);
  },

  // 搜索商家
  searchShops(name: string): Promise<Shop[]> {
    return request.get(`${BASE_URL}/search`, { params: { name } });
  },

  // 按地区查询商家
  getShopsByRegion(
    province: string,
    city: string,
    district?: string
  ): Promise<Shop[]> {
    return request.get(`${BASE_URL}/region`, {
      params: { province, city, district },
    });
  },

  // 评分最高商家
  getTopRatingShops(limit: number = 10): Promise<Shop[]> {
    return request.get(`${BASE_URL}/top/rating`, { params: { limit } });
  },

  // 销量最高商家
  getTopSalesShops(limit: number = 10): Promise<Shop[]> {
    return request.get(`${BASE_URL}/top/sales`, { params: { limit } });
  },

  // 分页查询商家
  getShopPage(params: PageParams): Promise<PageResult<Shop>> {
    return request.get(`${BASE_URL}/page`, { params });
  },

  // 分页查询营业中商家
  getBusinessShopPage(params: PageParams): Promise<PageResult<Shop>> {
    return request.get(`${BASE_URL}/business/page`, { params });
  },

  // 分类商家分页
  getCategoryShopPage(
    categoryId: number,
    params: PageParams
  ): Promise<PageResult<Shop>> {
    return request.get(`${BASE_URL}/category/${categoryId}/page`, { params });
  },
};

export const shopCategoryApi = {
  // 获取所有商家分类
  getAllCategories(): Promise<ShopCategory[]> {
    return request.get(`${CATEGORY_URL}/list`);
  },

  // 获取分类详情
  getCategoryById(id: number): Promise<ShopCategory> {
    return request.get(`${CATEGORY_URL}/${id}`);
  },

  // 按层级查询分类
  getCategoriesByLevel(level: number): Promise<ShopCategory[]> {
    return request.get(`${CATEGORY_URL}/level/${level}`);
  },

  // 获取子分类
  getChildCategories(parentId: number): Promise<ShopCategory[]> {
    return request.get(`${CATEGORY_URL}/parent/${parentId}`);
  },
};
