// 通知类型
export enum NotificationType {
  ORDER = 1, // 订单通知
  SYSTEM = 2, // 系统通知
  PROMOTION = 3, // 促销通知
}

// 通知信息
export interface Notification {
  id: number;
  userId: number;
  title: string;
  content: string;
  notificationType: number;
  isRead: number; // 0-未读 1-已读
  relatedId?: number; // 关联ID
  createTime?: string;
}

// 订单评价
export interface OrderReview {
  id: number;
  orderId: number;
  userId: number;
  shopId: number;
  rating: number; // 评分 1-5
  content?: string;
  images?: string; // 图片URLs，逗号分隔
  shopReply?: string;
  replyTime?: string;
  isAnonymous: number; // 0-不匿名 1-匿名
  createTime?: string;
  updateTime?: string;
}
