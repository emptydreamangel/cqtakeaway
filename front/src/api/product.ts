import { request } from "@/utils/request";
import type {
  Product,
  ProductCategory,
  ProductSpec,
  PageParams,
  PageResult,
} from "@/types";

const BASE_URL = "/product";
const CATEGORY_URL = "/product-category";
const SPEC_URL = "/product-spec";

export const productApi = {
  // 获取商家商品列表
  getProductsByShop(shopId: number): Promise<Product[]> {
    return request.get(`${BASE_URL}/shop/${shopId}`);
  },

  // 获取分类商品列表
  getProductsByCategory(categoryId: number): Promise<Product[]> {
    return request.get(`${BASE_URL}/category/${categoryId}`);
  },

  // 获取商家+分类商品
  getProductsByShopAndCategory(
    shopId: number,
    categoryId: number
  ): Promise<Product[]> {
    return request.get(`${BASE_URL}/shop/${shopId}/category/${categoryId}`);
  },

  // 获取商品详情
  getProductById(id: number): Promise<Product> {
    return request.get(`${BASE_URL}/${id}`);
  },

  // 搜索商品
  searchProducts(name: string): Promise<Product[]> {
    return request.get(`${BASE_URL}/search`, { params: { name } });
  },

  // 商家内搜索商品
  searchProductsInShop(shopId: number, name: string): Promise<Product[]> {
    return request.get(`${BASE_URL}/shop/${shopId}/search`, {
      params: { name },
    });
  },

  // 获取热销商品
  getHotProducts(shopId: number, limit: number = 10): Promise<Product[]> {
    return request.get(`${BASE_URL}/shop/${shopId}/hot`, { params: { limit } });
  },

  // 获取新品
  getNewProducts(shopId: number, limit: number = 10): Promise<Product[]> {
    return request.get(`${BASE_URL}/shop/${shopId}/new`, { params: { limit } });
  },

  // 分页查询商品
  getProductPage(params: PageParams): Promise<PageResult<Product>> {
    return request.get(`${BASE_URL}/page`, { params });
  },

  // 商家商品分页
  getShopProductPage(
    shopId: number,
    params: PageParams
  ): Promise<PageResult<Product>> {
    return request.get(`${BASE_URL}/shop/${shopId}/page`, { params });
  },
};

export const productCategoryApi = {
  // 获取所有商品分类
  getAllCategories(): Promise<ProductCategory[]> {
    return request.get(`${CATEGORY_URL}/list`);
  },

  // 获取分类详情
  getCategoryById(id: number): Promise<ProductCategory> {
    return request.get(`${CATEGORY_URL}/${id}`);
  },

  // 获取商家分类列表
  getCategoriesByShop(shopId: number): Promise<ProductCategory[]> {
    return request.get(`${CATEGORY_URL}/shop/${shopId}`);
  },
};

export const productSpecApi = {
  // 获取商品规格列表
  getSpecsByProduct(productId: number): Promise<ProductSpec[]> {
    return request.get(`${SPEC_URL}/product/${productId}`);
  },

  // 获取规格详情
  getSpecById(id: number): Promise<ProductSpec> {
    return request.get(`${SPEC_URL}/${id}`);
  },

  // 获取可用规格
  getAvailableSpecs(productId: number): Promise<ProductSpec[]> {
    return request.get(`${SPEC_URL}/product/${productId}/available`);
  },
};
