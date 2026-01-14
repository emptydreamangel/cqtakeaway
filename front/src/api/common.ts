import { request } from "@/utils/request";
import type {
  UserFavorite,
  OrderReview,
  Notification,
  PageParams,
  PageResult,
} from "@/types";

// 用户收藏API
const FAVORITE_URL = "/user-favorite";

export const favoriteApi = {
  // 获取用户收藏
  getFavoritesByUser(userId: number): Promise<UserFavorite[]> {
    return request.get(`${FAVORITE_URL}/user/${userId}`);
  },

  // 获取收藏的商家
  getFavoriteShops(userId: number): Promise<UserFavorite[]> {
    return request.get(`${FAVORITE_URL}/user/${userId}/shops`);
  },

  // 获取收藏的商品
  getFavoriteProducts(userId: number): Promise<UserFavorite[]> {
    return request.get(`${FAVORITE_URL}/user/${userId}/products`);
  },

  // 检查是否收藏
  checkFavorite(
    userId: number,
    targetType: number,
    targetId: number
  ): Promise<boolean> {
    return request.get(`${FAVORITE_URL}/check`, {
      params: { userId, targetType, targetId },
    });
  },

  // 添加收藏
  addFavorite(
    data: Omit<UserFavorite, "id" | "createTime">
  ): Promise<UserFavorite> {
    return request.post(FAVORITE_URL, data);
  },

  // 取消收藏
  removeFavorite(id: number): Promise<void> {
    return request.delete(`${FAVORITE_URL}/${id}`);
  },

  // 取消指定收藏
  removeFavoriteByTarget(
    userId: number,
    targetType: number,
    targetId: number
  ): Promise<void> {
    return request.delete(`${FAVORITE_URL}/user/${userId}/target`, {
      params: { targetType, targetId },
    });
  },

  // 分页查询收藏
  getFavoritePage(
    params: PageParams & { userId: number; targetType?: number }
  ): Promise<PageResult<UserFavorite>> {
    return request.get(`${FAVORITE_URL}/page`, { params });
  },
};

// 订单评价API
const REVIEW_URL = "/order-review";

export const reviewApi = {
  // 获取订单评价
  getReviewByOrder(orderId: number): Promise<OrderReview> {
    return request.get(`${REVIEW_URL}/order/${orderId}`);
  },

  // 获取商家评价列表
  getReviewsByShop(shopId: number): Promise<OrderReview[]> {
    return request.get(`${REVIEW_URL}/shop/${shopId}`);
  },

  // 商家评价分页
  getShopReviewPage(
    shopId: number,
    params: PageParams
  ): Promise<PageResult<OrderReview>> {
    return request.get(`${REVIEW_URL}/shop/${shopId}/page`, { params });
  },

  // 获取用户评价列表
  getReviewsByUser(userId: number): Promise<OrderReview[]> {
    return request.get(`${REVIEW_URL}/user/${userId}`);
  },

  // 获取评价详情
  getReviewById(id: number): Promise<OrderReview> {
    return request.get(`${REVIEW_URL}/${id}`);
  },

  // 添加评价
  addReview(
    data: Omit<OrderReview, "id" | "createTime" | "updateTime">
  ): Promise<OrderReview> {
    return request.post(REVIEW_URL, data);
  },

  // 获取商家平均评分
  getShopAvgRating(shopId: number): Promise<number> {
    return request.get(`${REVIEW_URL}/shop/${shopId}/avg-rating`);
  },

  // 获取商家最近评价
  getShopRecentReviews(
    shopId: number,
    limit: number = 10
  ): Promise<OrderReview[]> {
    return request.get(`${REVIEW_URL}/shop/${shopId}/recent`, {
      params: { limit },
    });
  },
};

// 通知API
const NOTIFICATION_URL = "/notifications";

export const notificationApi = {
  // 获取通知详情
  getNotificationById(id: number): Promise<Notification> {
    return request.get(`${NOTIFICATION_URL}/${id}`);
  },

  // 获取用户通知列表
  getNotificationsByUser(userId: number): Promise<Notification[]> {
    return request.get(`${NOTIFICATION_URL}/user/${userId}`);
  },

  // 获取未读通知
  getUnreadNotifications(userId: number): Promise<Notification[]> {
    return request.get(`${NOTIFICATION_URL}/user/${userId}/unread`);
  },

  // 分页查询通知
  getNotificationPage(
    params: PageParams & {
      userId: number;
      notificationType?: number;
      isRead?: number;
    }
  ): Promise<PageResult<Notification>> {
    return request.get(`${NOTIFICATION_URL}/page`, { params });
  },

  // 标记已读
  markAsRead(id: number): Promise<void> {
    return request.put(`${NOTIFICATION_URL}/${id}/read`);
  },

  // 全部已读
  markAllAsRead(userId: number): Promise<void> {
    return request.put(`${NOTIFICATION_URL}/user/${userId}/read-all`);
  },

  // 删除通知
  deleteNotification(id: number): Promise<void> {
    return request.delete(`${NOTIFICATION_URL}/${id}`);
  },

  // 清空通知
  clearNotifications(userId: number): Promise<void> {
    return request.delete(`${NOTIFICATION_URL}/user/${userId}/clear`);
  },

  // 获取未读数量
  getUnreadCount(userId: number): Promise<number> {
    return request.get(`${NOTIFICATION_URL}/user/${userId}/count/unread`);
  },

  // 获取最近通知
  getRecentNotifications(
    userId: number,
    limit: number = 10
  ): Promise<Notification[]> {
    return request.get(`${NOTIFICATION_URL}/user/${userId}/recent`, {
      params: { limit },
    });
  },
};
