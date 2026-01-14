import axios, {
  type AxiosInstance,
  type AxiosRequestConfig,
  type AxiosResponse,
} from "axios";
import { ElMessage } from "element-plus";
import { getToken, removeToken } from "./storage";
import router from "@/router";

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: "/api",
  timeout: 15000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    console.error("请求错误:", error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    return response.data;
  },
  (error) => {
    const { response } = error;
    if (response) {
      const { status, data } = response;
      switch (status) {
        case 400:
          ElMessage.error(data?.message || "请求参数错误");
          break;
        case 401:
          ElMessage.error("登录已过期，请重新登录");
          removeToken();
          router.push("/login");
          break;
        case 403:
          ElMessage.error("没有权限访问");
          break;
        case 404:
          ElMessage.error("请求的资源不存在");
          break;
        case 500:
          ElMessage.error(data?.message || "服务器内部错误");
          break;
        default:
          ElMessage.error(data?.message || "网络错误");
      }
    } else {
      ElMessage.error("网络连接失败，请检查网络");
    }
    return Promise.reject(error);
  }
);

// 请求方法封装
export const request = {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.get(url, config);
  },

  post<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<T> {
    return service.post(url, data, config);
  },

  put<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<T> {
    return service.put(url, data, config);
  },

  patch<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<T> {
    return service.patch(url, data, config);
  },

  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.delete(url, config);
  },
};

export default service;
