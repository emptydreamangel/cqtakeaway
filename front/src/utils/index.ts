import { OrderStatus, PayMethod } from "@/types";

// 格式化金额
export function formatPrice(price: number | string): string {
  const num = typeof price === "string" ? parseFloat(price) : price;
  return num.toFixed(2);
}

// 格式化日期时间
export function formatDateTime(dateStr: string | undefined): string {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 格式化日期
export function formatDate(dateStr: string | undefined): string {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

// 获取订单状态文本
export function getOrderStatusText(status: number): string {
  const statusMap: Record<number, string> = {
    [OrderStatus.PENDING_PAYMENT]: "待支付",
    [OrderStatus.PENDING_ACCEPT]: "待接单",
    [OrderStatus.PENDING_DELIVERY]: "待配送",
    [OrderStatus.DELIVERING]: "配送中",
    [OrderStatus.COMPLETED]: "已完成",
    [OrderStatus.CANCELLED]: "已取消",
  };
  return statusMap[status] || "未知状态";
}

// 获取订单状态颜色
export function getOrderStatusType(status: number): string {
  const typeMap: Record<number, string> = {
    [OrderStatus.PENDING_PAYMENT]: "warning",
    [OrderStatus.PENDING_ACCEPT]: "primary",
    [OrderStatus.PENDING_DELIVERY]: "primary",
    [OrderStatus.DELIVERING]: "success",
    [OrderStatus.COMPLETED]: "info",
    [OrderStatus.CANCELLED]: "danger",
  };
  return typeMap[status] || "info";
}

// 获取支付方式文本
export function getPayMethodText(method: number): string {
  const methodMap: Record<number, string> = {
    [PayMethod.WECHAT]: "微信支付",
    [PayMethod.ALIPAY]: "支付宝",
    [PayMethod.BALANCE]: "余额支付",
  };
  return methodMap[method] || "未知方式";
}

// 防抖
export function debounce<T extends (...args: any[]) => any>(
  fn: T,
  delay: number
): (...args: Parameters<T>) => void {
  let timer: ReturnType<typeof setTimeout> | null = null;
  return function (this: any, ...args: Parameters<T>) {
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      fn.apply(this, args);
    }, delay);
  };
}

// 节流
export function throttle<T extends (...args: any[]) => any>(
  fn: T,
  delay: number
): (...args: Parameters<T>) => void {
  let lastTime = 0;
  return function (this: any, ...args: Parameters<T>) {
    const now = Date.now();
    if (now - lastTime >= delay) {
      lastTime = now;
      fn.apply(this, args);
    }
  };
}

// 生成唯一ID
export function generateId(): string {
  return Date.now().toString(36) + Math.random().toString(36).substring(2);
}
