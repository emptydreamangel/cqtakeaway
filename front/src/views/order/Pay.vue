<template>
  <div class="pay-page">
    <NavBar title="支付订单" />

    <div class="pay-content" v-loading="loading">
      <!-- 订单金额 -->
      <div class="pay-amount">
        <div class="amount-label">支付金额</div>
        <div class="amount-value">
          <span class="symbol">¥</span>
          <span class="number">{{
            order?.payAmount?.toFixed(2) || "0.00"
          }}</span>
        </div>
      </div>

      <!-- 支付方式 -->
      <div class="pay-methods">
        <div class="section-title">选择支付方式</div>
        <div
          v-for="method in payMethods"
          :key="method.value"
          class="pay-method-item"
          :class="{ selected: selectedMethod === method.value }"
          @click="selectedMethod = method.value"
        >
          <div class="method-icon" :style="{ backgroundColor: method.color }">
            <el-icon><component :is="method.icon" /></el-icon>
          </div>
          <div class="method-info">
            <div class="method-name">{{ method.label }}</div>
            <div class="method-desc">{{ method.desc }}</div>
          </div>
          <el-icon v-if="selectedMethod === method.value" class="check-icon"
            ><Check
          /></el-icon>
        </div>
      </div>

      <!-- 订单信息 -->
      <div class="order-info">
        <div class="info-item">
          <span class="label">订单编号</span>
          <span class="value">{{ order?.orderNo }}</span>
        </div>
        <div class="info-item">
          <span class="label">商家名称</span>
          <span class="value">{{ order?.shop?.name || "-" }}</span>
        </div>
        <div class="info-item">
          <span class="label">商品数量</span>
          <span class="value">{{ order?.items?.length || 0 }}件</span>
        </div>
      </div>
    </div>

    <!-- 支付按钮 -->
    <div class="pay-footer">
      <el-button
        type="primary"
        size="large"
        :loading="paying"
        @click="handlePay"
      >
        确认支付
      </el-button>
    </div>

    <!-- 支付结果弹窗 -->
    <el-dialog
      v-model="showResult"
      :show-close="false"
      :close-on-click-modal="false"
      width="300px"
      center
    >
      <div class="pay-result">
        <div class="result-icon" :class="{ success: paySuccess }">
          <el-icon v-if="paySuccess"><CircleCheck /></el-icon>
          <el-icon v-else><CircleClose /></el-icon>
        </div>
        <div class="result-text">
          {{ paySuccess ? "支付成功" : "支付失败" }}
        </div>
        <div class="result-amount" v-if="paySuccess">
          ¥{{ order?.payAmount?.toFixed(2) }}
        </div>
      </div>
      <template #footer>
        <el-button type="primary" @click="handleResultClose">
          {{ paySuccess ? "查看订单" : "重新支付" }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, markRaw } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import {
  Check,
  CircleCheck,
  CircleClose,
  CreditCard,
  Wallet,
  Money,
} from "@element-plus/icons-vue";
import { orderApi, paymentApi } from "@/api";
import type { Order } from "@/types";
import { PayMethod } from "@/types";
import { useUserStore } from "@/stores/user";
import NavBar from "@/components/common/NavBar.vue";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const orderId = computed(() => Number(route.params.orderId));
const order = ref<Order | null>(null);
const loading = ref(false);
const paying = ref(false);
const selectedMethod = ref(PayMethod.WECHAT);
const showResult = ref(false);
const paySuccess = ref(false);

const payMethods = [
  {
    value: PayMethod.WECHAT,
    label: "微信支付",
    desc: "推荐使用微信支付",
    icon: markRaw(CreditCard),
    color: "#07c160",
  },
  {
    value: PayMethod.ALIPAY,
    label: "支付宝",
    desc: "支付宝快捷支付",
    icon: markRaw(Wallet),
    color: "#1677ff",
  },
  {
    value: PayMethod.BALANCE,
    label: "余额支付",
    desc: "使用账户余额支付",
    icon: markRaw(Money),
    color: "#ff6b00",
  },
];

// 获取订单信息
const fetchOrder = async () => {
  loading.value = true;
  try {
    order.value = await orderApi.getOrderById(orderId.value);

    // 检查订单状态
    if (order.value.status !== 0) {
      ElMessage.warning("订单状态已变更");
      router.replace(`/order/${orderId.value}`);
    }
  } catch (error) {
    console.error("获取订单失败:", error);
    ElMessage.error("获取订单失败");
  } finally {
    loading.value = false;
  }
};

// 确认支付
const handlePay = async () => {
  if (!order.value || !userStore.userId) return;

  paying.value = true;
  try {
    // 创建支付记录
    const payment = await paymentApi.createPayment({
      orderId: order.value.id,
      userId: userStore.userId,
      payMethod: selectedMethod.value,
      payAmount: order.value.payAmount,
    });

    // 模拟支付过程（实际应调用第三方支付）
    await new Promise((resolve) => setTimeout(resolve, 1500));

    // 模拟支付成功（90%概率成功）
    const success = Math.random() > 0.1;

    if (success) {
      // 更新支付状态
      await paymentApi.paymentSuccess(payment.id, `MOCK_${Date.now()}`);
      // 更新订单状态
      await orderApi.payOrder(order.value.id, selectedMethod.value);
      paySuccess.value = true;
    } else {
      await paymentApi.paymentFail(payment.id);
      paySuccess.value = false;
    }

    showResult.value = true;
  } catch (error: any) {
    ElMessage.error(error?.response?.data?.message || "支付失败");
    paySuccess.value = false;
    showResult.value = true;
  } finally {
    paying.value = false;
  }
};

// 处理结果弹窗关闭
const handleResultClose = () => {
  showResult.value = false;
  if (paySuccess.value) {
    router.replace(`/order/${orderId.value}`);
  }
};

onMounted(() => {
  fetchOrder();
});
</script>

<style lang="scss" scoped>
.pay-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 80px;
}

.pay-amount {
  background-color: #fff;
  padding: 32px 16px;
  text-align: center;
  margin-bottom: 12px;

  .amount-label {
    font-size: 14px;
    color: #666;
    margin-bottom: 12px;
  }

  .amount-value {
    color: #ff6b00;

    .symbol {
      font-size: 20px;
    }

    .number {
      font-size: 40px;
      font-weight: 600;
    }
  }
}

.pay-methods {
  background-color: #fff;
  padding: 16px;
  margin-bottom: 12px;

  .section-title {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    margin-bottom: 16px;
  }

  .pay-method-item {
    display: flex;
    align-items: center;
    padding: 16px 0;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;

    &:last-child {
      border-bottom: none;
    }

    &.selected {
      .method-icon {
        transform: scale(1.1);
      }
    }

    .method-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: transform 0.3s;

      .el-icon {
        font-size: 20px;
        color: #fff;
      }
    }

    .method-info {
      flex: 1;
      margin-left: 12px;

      .method-name {
        font-size: 14px;
        color: #333;
      }

      .method-desc {
        font-size: 12px;
        color: #999;
        margin-top: 4px;
      }
    }

    .check-icon {
      font-size: 20px;
      color: #ff6b00;
    }
  }
}

.order-info {
  background-color: #fff;
  padding: 12px 16px;

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

.pay-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background-color: #fff;
  border-top: 1px solid #eee;

  .el-button {
    width: 100%;
    height: 48px;
    font-size: 16px;
    background-color: #ff6b00;
    border-color: #ff6b00;
    border-radius: 24px;
  }
}

.pay-result {
  text-align: center;
  padding: 24px 0;

  .result-icon {
    width: 60px;
    height: 60px;
    margin: 0 auto 16px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #ff4d4f;

    &.success {
      background-color: #52c41a;
    }

    .el-icon {
      font-size: 36px;
      color: #fff;
    }
  }

  .result-text {
    font-size: 18px;
    font-weight: 500;
    color: #333;
    margin-bottom: 8px;
  }

  .result-amount {
    font-size: 24px;
    font-weight: 600;
    color: #ff6b00;
  }
}

:deep(.el-dialog__footer) {
  padding: 0 20px 20px;

  .el-button {
    width: 100%;
    background-color: #ff6b00;
    border-color: #ff6b00;
  }
}
</style>
