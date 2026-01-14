<template>
  <div class="order-list-page">
    <!-- 订单筛选 -->
    <div class="order-tabs">
      <div
        v-for="tab in tabs"
        :key="tab.value"
        class="tab-item"
        :class="{ active: currentTab === tab.value }"
        @click="handleTabChange(tab.value)"
      >
        {{ tab.label }}
      </div>
    </div>

    <!-- 订单列表 -->
    <div class="order-list" v-loading="loading">
      <OrderCard
        v-for="order in orders"
        :key="order.id"
        :order="order"
        @pay="handlePay"
        @review="handleReview"
        @reorder="handleReorder"
      />
      <Empty v-if="!loading && orders.length === 0" description="暂无订单">
        <template #default>
          <el-button type="primary" @click="goHome">去点餐</el-button>
        </template>
      </Empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { orderApi } from "@/api";
import type { Order } from "@/types";
import { useUserStore } from "@/stores/user";
import OrderCard from "@/components/order/OrderCard.vue";
import Empty from "@/components/common/Empty.vue";

const router = useRouter();
const userStore = useUserStore();

const tabs = [
  { label: "全部", value: -1 },
  { label: "待支付", value: 0 },
  { label: "待接单", value: 1 },
  { label: "配送中", value: 3 },
  { label: "已完成", value: 4 },
];

const currentTab = ref(-1);
const orders = ref<Order[]>([]);
const loading = ref(false);

// 获取订单列表
const fetchOrders = async () => {
  if (!userStore.userId) return;

  loading.value = true;
  try {
    let res: Order[];
    if (currentTab.value === -1) {
      res = await orderApi.getOrdersByUser(userStore.userId);
    } else {
      res = await orderApi.getOrdersByUserAndStatus(
        userStore.userId,
        currentTab.value
      );
    }
    orders.value = res;
  } catch (error) {
    console.error("获取订单失败:", error);
  } finally {
    loading.value = false;
  }
};

// 切换Tab
const handleTabChange = (value: number) => {
  currentTab.value = value;
};

// 支付
const handlePay = (order: Order) => {
  router.push(`/pay/${order.id}`);
};

// 评价
const handleReview = (_order: Order) => {
  ElMessage.info("评价功能开发中");
};

// 再来一单
const handleReorder = (order: Order) => {
  router.push(`/shop/${order.shopId}`);
};

// 去首页
const goHome = () => {
  router.push("/home");
};

// 监听Tab变化
watch(currentTab, () => {
  fetchOrders();
});

onMounted(() => {
  fetchOrders();
});
</script>

<style lang="scss" scoped>
.order-list-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.order-tabs {
  display: flex;
  background-color: #fff;
  padding: 0 12px;
  position: sticky;
  top: 0;
  z-index: 10;

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 16px 0;
    font-size: 14px;
    color: #666;
    cursor: pointer;
    position: relative;

    &.active {
      color: #ff6b00;
      font-weight: 500;

      &::after {
        content: "";
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 24px;
        height: 3px;
        background-color: #ff6b00;
        border-radius: 2px;
      }
    }
  }
}

.order-list {
  padding: 12px;
}
</style>
