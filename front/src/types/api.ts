// API响应通用类型
export interface ApiResponse<T = any> {
  code?: number;
  message?: string;
  data: T;
}

// 分页请求参数
export interface PageParams {
  current?: number;
  pageNum?: number;
  size?: number;
  pageSize?: number;
}

// 分页响应数据
export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}
