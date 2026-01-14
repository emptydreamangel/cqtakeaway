<template>
  <div class="order-detail-page">
    <NavBar title="订单详情" />

    <div class="order-content" v-loading="loading">
      <!-- 订单状态 -->
      <div class="order-status" :class="statusClass">
        <div class="status-text">{{ statusText }}</div>
        <div class="status-desc">{{ statusDesc }}</div>
      </div>

      <!-- 收货地址 -->
      <div class="address-section">
        <el-icon class="address-icon"><Location /></el-icon>
        <div class="address-info">
          <div class="address-header">
            <span class="name">{{ order?.contactName }}</span>
            <span class="phone">{{ order?.contactPhone }}</span>
          </div>
          <div class="address-detail">{{ order?.deliveryAddress }}</div>
        </div>
      </div>

      <!-- 商品信息 -->
      <div class="goods-section">
        <div class="section-header" @click="goShop">
          <span class="shop-name">{{ order?.shop?.name || "商家" }}</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="goods-list">
          <div v-for="item in order?.items" :key="item.id" class="goods-item">
            <el-image
              :src="item.productImage || defaultImage"
              class="goods-image"
              fit="cover"
            />
            <div class="goods-info">
              <div class="goods-name">{{ item.productName }}</div>
              <div v-if="item.specName" class="goods-spec">
                {{ item.specName }}
              </div>
            </div>
            <div class="goods-right">
              <div class="goods-price">¥{{ item.price }}</div>
              <div class="goods-quantity">x{{ item.quantity }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 订单信息 -->
      <div class="order-info-section">
        <div class="info-item">
          <span class="label">订单编号</span>
          <span class="value">{{ order?.orderNo }}</span>
        </div>
        <div class="info-item">
          <span class="label">下单时间</span>
          <span class="value">{{ formatDateTime(order?.createTime) }}</span>
        </div>
        <div v-if="order?.payTime" class="info-item">
          <span class="label">支付时间</span>
          <span class="value">{{ formatDateTime(order?.payTime) }}</span>
        </div>
        <div v-if="order?.remark" class="info-item">
          <span class="label">备注</span>
          <span class="value">{{ order?.remark }}</span>
        </div>
      </div>

      <!-- 金额明细 -->
      <div class="amount-section">
        <div class="amount-item">
          <span class="label">商品总价</span>
          <span class="value">¥{{ order?.totalAmount?.toFixed(2) }}</span>
        </div>
        <div class="amount-item">
          <span class="label">配送费</span>
          <span class="value">¥{{ order?.deliveryFee?.toFixed(2) }}</span>
        </div>
        <div
          v-if="order?.discountAmount && order.discountAmount > 0"
          class="amount-item discount"
        >
          <span class="label">优惠</span>
          <span class="value">-¥{{ order?.discountAmount?.toFixed(2) }}</span>
        </div>
        <div class="amount-item total">
          <span class="label">实付金额</span>
          <span class="value">¥{{ order?.payAmount?.toFixed(2) }}</span>
        </div>
      </div>
    </div>

    <!-- 底部操作 -->
    <div class="order-actions">
      <el-button v-if="order?.status === 0" @click="handleCancel"
        >取消订单</el-button
      >
      <el-button v-if="order?.status === 0" type="primary" @click="handlePay"
        >去支付</el-button
      >
      <el-button
        v-if="order?.status === 4"
        type="primary"
        @click="handleReorder"
        >再来一单</el-button
      >
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Location, ArrowRight } from "@element-plus/icons-vue";
import { orderApi } from "@/api";
import type { Order } from "@/types";
import { OrderStatus } from "@/types";
import { formatDateTime, getOrderStatusText } from "@/utils";
import NavBar from "@/components/common/NavBar.vue";

const route = useRoute();
const router = useRouter();

const defaultImage =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCI+PHJlY3Qgd2lkdGg9IjYwIiBoZWlnaHQ9IjYwIiBmaWxsPSIjZWVlIi8+PC9zdmc+";

const orderId = computed(() => Number(route.params.id));
const order = ref<Order | null>(null);
const loading = ref(false);

const statusText = computed(() => getOrderStatusText(order.value?.status || 0));

const statusDesc = computed(() => {
  const status = order.value?.status;
  switch (status) {
    case OrderStatus.PENDING_PAYMENT:
      return "请尽快完成支付";
    case OrderStatus.PENDING_ACCEPT:
      return "商家正在准备中";
    case OrderStatus.PENDING_DELIVERY:
      return "等待骑手接单";
    case OrderStatus.DELIVERING:
      return "骑手正在配送中";
    case OrderStatus.COMPLETED:
      return "感谢您的信任";
    case OrderStatus.CANCELLED:
      return order.value?.cancelReason || "订单已取消";
    default:
      return "";
  }
});

const statusClass = computed(() => {
  const status = order.value?.status;
  if (status === OrderStatus.COMPLETED) return "success";
  if (status === OrderStatus.CANCELLED) return "cancelled";
  return "pending";
});

// 获取订单详情
const fetchOrder = async () => {
  loading.value = true;
  try {
    order.value = await orderApi.getOrderById(orderId.value);
  } catch (error) {
    console.error("获取订单失败:", error);
  } finally {
    loading.value = false;
  }
};

// 去商家页面
const goShop = () => {
  if (order.value?.shopId) {
    router.push(`/shop/${order.value.shopId}`);
  }
};

// 取消订单
const handleCancel = async () => {
  try {
    const { value: reason } = await ElMessageBox.prompt(
      "请输入取消原因",
      "取消订单",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPlaceholder: "请输入取消原因",
      }
    );

    await orderApi.cancelOrder(orderId.value, reason || "用户取消");
    ElMessage.success("订单已取消");
    fetchOrder();
  } catch {
    // 取消
  }
};

// 去支付
const handlePay = () => {
  router.push(`/pay/${orderId.value}`);
};

// 再来一单
const handleReorder = () => {
  if (order.value?.shopId) {
    router.push(`/shop/${order.value.shopId}`);
  }
};

onMounted(() => {
  fetchOrder();
});
</script>

<style lang="scss" scoped>
.order-detail-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 70px;
}

.order-status {
  padding: 24px 16px;
  color: #fff;

  &.pending {
    background: linear-gradient(135deg, #ff6b00 0%, #ff8533 100%);
  }

  &.success {
    background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
  }

  &.cancelled {
    background: linear-gradient(135deg, #666 0%, #999 100%);
  }

  .status-text {
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 8px;
  }

  .status-desc {
    font-size: 14px;
    opacity: 0.9;
  }
}

.address-section {
  display: flex;
  align-items: flex-start;
  background-color: #fff;
  padding: 16px;
  margin-bottom: 12px;

  .address-icon {
    font-size: 20px;
    color: #ff6b00;
    margin-right: 12px;
    margin-top: 2px;
  }

  .address-info {
    flex: 1;

    .address-header {
      display: flex;
      gap: 8px;
      margin-bottom: 8px;

      .name {
        font-size: 16px;
        font-weight: 500;
        color: #333;
      }

      .phone {
        font-size: 14px;
        color: #666;
      }
    }

    .address-detail {
      font-size: 14px;
      color: #666;
      line-height: 1.4;
    }
  }
}

.goods-section {
  background-color: #fff;
  margin-bottom: 12px;

  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;

    .shop-name {
      font-size: 14px;
      font-weight: 500;
      color: #333;
    }

    .el-icon {
      font-size: 14px;
      color: #999;
    }
  }

  .goods-list {
    .goods-item {
      display: flex;
      align-items: center;
      padding: 12px 16px;

      .goods-image {
        width: 50px;
        height: 50px;
        border-radius: 6px;
        flex-shrink: 0;
      }

      .goods-info {
        flex: 1;
        margin-left: 12px;

        .goods-name {
          font-size: 14px;
          color: #333;
        }

        .goods-spec {
          font-size: 12px;
          color: #999;
          margin-top: 4px;
        }
      }

      .goods-right {
        text-align: right;

        .goods-price {
          font-size: 14px;
          color: #333;
        }

        .goods-quantity {
          font-size: 12px;
          color: #999;
          margin-top: 4px;
        }
      }
    }
  }
}

.order-info-section {
  background-color: #fff;
  padding: 12px 16px;
  margin-bottom: 12px;

  .info-item {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;

    .label {
      font-size: 14px;
      color: #666;
    }

    .value {
      font-size: 14px;
      color: #333;
    }
  }
}

.amount-section {
  background-color: #fff;
  padding: 12px 16px;

  .amount-item {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;

    .label {
      font-size: 14px;
      color: #666;
    }

    .value {
      font-size: 14px;
      color: #333;
    }

    &.discount .value {
      color: #ff6b00;
    }

    &.total {
      padding-top: 12px;
      margin-top: 8px;
      border-top: 1px solid #f5f5f5;

      .label {
        font-weight: 500;
        color: #333;
      }

      .value {
        font-size: 18px;
        font-weight: 600;
        color: #ff6b00;
      }
    }
  }
}

.order-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 12px 16px;
  background-color: #fff;
  border-top: 1px solid #eee;

  .el-button--primary {
    background-color: #ff6b00;
    border-color: #ff6b00;
  }
}
</style>
