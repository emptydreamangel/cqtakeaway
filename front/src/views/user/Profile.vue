<template>
  <div class="profile-page">
    <!-- 用户信息头部 -->
    <div class="profile-header">
      <div class="user-info" @click="handleEditProfile">
        <el-avatar :size="60" :src="userStore.avatar || defaultAvatar" />
        <div class="user-meta">
          <div class="nickname">{{ userStore.nickname }}</div>
          <div class="phone">{{ userStore.user?.phone || "未绑定手机" }}</div>
        </div>
        <el-icon class="arrow"><ArrowRight /></el-icon>
      </div>
    </div>

    <!-- 订单快捷入口 -->
    <div class="order-section">
      <div class="section-header">
        <span class="title">我的订单</span>
        <span class="more" @click="goOrders(-1)">
          全部订单 <el-icon><ArrowRight /></el-icon>
        </span>
      </div>
      <div class="order-shortcuts">
        <div class="shortcut-item" @click="goOrders(0)">
          <el-icon><CreditCard /></el-icon>
          <span>待支付</span>
        </div>
        <div class="shortcut-item" @click="goOrders(1)">
          <el-icon><Clock /></el-icon>
          <span>待接单</span>
        </div>
        <div class="shortcut-item" @click="goOrders(3)">
          <el-icon><Van /></el-icon>
          <span>配送中</span>
        </div>
        <div class="shortcut-item" @click="goOrders(4)">
          <el-icon><Star /></el-icon>
          <span>待评价</span>
        </div>
      </div>
    </div>

    <!-- 功能菜单 -->
    <div class="menu-section">
      <div class="menu-item" @click="goPage('/address')">
        <el-icon><Location /></el-icon>
        <span class="text">收货地址</span>
        <el-icon class="arrow"><ArrowRight /></el-icon>
      </div>
      <div class="menu-item" @click="goPage('/favorites')">
        <el-icon><StarFilled /></el-icon>
        <span class="text">我的收藏</span>
        <el-icon class="arrow"><ArrowRight /></el-icon>
      </div>
      <div class="menu-item" @click="goPage('/coupons')">
        <el-icon><Ticket /></el-icon>
        <span class="text">我的优惠券</span>
        <el-icon class="arrow"><ArrowRight /></el-icon>
      </div>
      <div class="menu-item" @click="goPage('/settings')">
        <el-icon><Setting /></el-icon>
        <span class="text">设置</span>
        <el-icon class="arrow"><ArrowRight /></el-icon>
      </div>
    </div>

    <!-- 退出登录 -->
    <div class="logout-section" v-if="userStore.isLoggedIn">
      <el-button type="danger" plain @click="handleLogout">退出登录</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { ElMessageBox, ElMessage } from "element-plus";
import {
  ArrowRight,
  CreditCard,
  Clock,
  Van,
  Star,
  Location,
  StarFilled,
  Ticket,
  Setting,
} from "@element-plus/icons-vue";
import { useUserStore } from "@/stores/user";

const router = useRouter();
const userStore = useUserStore();

const defaultAvatar =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCI+PHJlY3Qgd2lkdGg9IjYwIiBoZWlnaHQ9IjYwIiBmaWxsPSIjZmY2YjAwIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGRvbWluYW50LWJhc2VsaW5lPSJtaWRkbGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGZpbGw9IiNmZmYiIGZvbnQtc2l6ZT0iMjQiPuWktOWDjzwvdGV4dD48L3N2Zz4=";

// 编辑个人信息
const handleEditProfile = () => {
  if (!userStore.isLoggedIn) {
    router.push("/login");
    return;
  }
  ElMessage.info("个人信息编辑功能开发中");
};

// 去订单列表
const goOrders = (_status: number) => {
  if (!userStore.isLoggedIn) {
    router.push("/login");
    return;
  }
  router.push("/orders");
};

// 去指定页面
const goPage = (path: string) => {
  if (!userStore.isLoggedIn) {
    router.push("/login");
    return;
  }
  router.push(path);
};

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm("确定要退出登录吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    userStore.logout();
    ElMessage.success("已退出登录");
  } catch {
    // 取消
  }
};
</script>

<style lang="scss" scoped>
.profile-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.profile-header {
  background: linear-gradient(135deg, #ff6b00 0%, #ff8533 100%);
  padding: 24px 16px;

  .user-info {
    display: flex;
    align-items: center;
    cursor: pointer;

    .el-avatar {
      border: 2px solid rgba(255, 255, 255, 0.5);
    }

    .user-meta {
      flex: 1;
      margin-left: 16px;

      .nickname {
        font-size: 18px;
        font-weight: 600;
        color: #fff;
        margin-bottom: 4px;
      }

      .phone {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
      }
    }

    .arrow {
      font-size: 20px;
      color: rgba(255, 255, 255, 0.8);
    }
  }
}

.order-section {
  background-color: #fff;
  margin: 12px;
  border-radius: 12px;
  padding: 16px;

  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;

    .title {
      font-size: 16px;
      font-weight: 500;
      color: #333;
    }

    .more {
      display: flex;
      align-items: center;
      font-size: 12px;
      color: #999;
      cursor: pointer;

      .el-icon {
        font-size: 14px;
      }
    }
  }

  .order-shortcuts {
    display: flex;
    justify-content: space-around;

    .shortcut-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      cursor: pointer;

      .el-icon {
        font-size: 24px;
        color: #ff6b00;
      }

      span {
        font-size: 12px;
        color: #666;
      }
    }
  }
}

.menu-section {
  background-color: #fff;
  margin: 12px;
  border-radius: 12px;
  overflow: hidden;

  .menu-item {
    display: flex;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;

    &:last-child {
      border-bottom: none;
    }

    > .el-icon {
      font-size: 20px;
      color: #ff6b00;
    }

    .text {
      flex: 1;
      margin-left: 12px;
      font-size: 14px;
      color: #333;
    }

    .arrow {
      font-size: 16px;
      color: #999;
    }
  }
}

.logout-section {
  padding: 24px 16px;

  .el-button {
    width: 100%;
    height: 44px;
    font-size: 14px;
    border-radius: 22px;
  }
}
</style>
