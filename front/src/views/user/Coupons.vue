<template>
  <div class="coupons-page">
    <NavBar title="我的优惠券" />

    <el-tabs v-model="activeTab" class="coupons-tabs">
      <el-tab-pane label="可用" name="available">
        <div class="coupons-list" v-loading="loading">
          <div
            v-for="coupon in availableCoupons"
            :key="coupon.id"
            class="coupon-item"
          >
            <div class="coupon-left">
              <div class="coupon-amount">
                <span class="symbol">¥</span>
                <span class="value">{{
                  coupon.coupon?.discountAmount || 0
                }}</span>
              </div>
              <div class="coupon-condition">
                满{{ coupon.coupon?.minAmount }}可用
              </div>
            </div>
            <div class="coupon-right">
              <div class="coupon-name">{{ coupon.coupon?.name }}</div>
              <div class="coupon-expire">
                有效期至 {{ formatDate(coupon.coupon?.endTime) }}
              </div>
              <el-button size="small" type="primary" @click="handleUse(coupon)"
                >去使用</el-button
              >
            </div>
          </div>
          <Empty
            v-if="!loading && availableCoupons.length === 0"
            description="暂无可用优惠券"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="已使用" name="used">
        <div class="coupons-list" v-loading="loading">
          <div
            v-for="coupon in usedCoupons"
            :key="coupon.id"
            class="coupon-item disabled"
          >
            <div class="coupon-left">
              <div class="coupon-amount">
                <span class="symbol">¥</span>
                <span class="value">{{
                  coupon.coupon?.discountAmount || 0
                }}</span>
              </div>
              <div class="coupon-condition">
                满{{ coupon.coupon?.minAmount }}可用
              </div>
            </div>
            <div class="coupon-right">
              <div class="coupon-name">{{ coupon.coupon?.name }}</div>
              <div class="coupon-status">已使用</div>
            </div>
          </div>
          <Empty
            v-if="!loading && usedCoupons.length === 0"
            description="暂无已使用优惠券"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="已过期" name="expired">
        <div class="coupons-list" v-loading="loading">
          <div
            v-for="coupon in expiredCoupons"
            :key="coupon.id"
            class="coupon-item disabled"
          >
            <div class="coupon-left">
              <div class="coupon-amount">
                <span class="symbol">¥</span>
                <span class="value">{{
                  coupon.coupon?.discountAmount || 0
                }}</span>
              </div>
              <div class="coupon-condition">
                满{{ coupon.coupon?.minAmount }}可用
              </div>
            </div>
            <div class="coupon-right">
              <div class="coupon-name">{{ coupon.coupon?.name }}</div>
              <div class="coupon-status">已过期</div>
            </div>
          </div>
          <Empty
            v-if="!loading && expiredCoupons.length === 0"
            description="暂无已过期优惠券"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import { userCouponApi } from "@/api";
import type { UserCoupon } from "@/types";
import { UserCouponStatus } from "@/types";
import { useUserStore } from "@/stores/user";
import { formatDate } from "@/utils";
import NavBar from "@/components/common/NavBar.vue";
import Empty from "@/components/common/Empty.vue";

const router = useRouter();
const userStore = useUserStore();

const activeTab = ref("available");
const loading = ref(false);
const coupons = ref<UserCoupon[]>([]);

const availableCoupons = computed(() =>
  coupons.value.filter((c) => c.status === UserCouponStatus.UNUSED)
);
const usedCoupons = computed(() =>
  coupons.value.filter((c) => c.status === UserCouponStatus.USED)
);
const expiredCoupons = computed(() =>
  coupons.value.filter((c) => c.status === UserCouponStatus.EXPIRED)
);

// 获取优惠券列表
const fetchCoupons = async () => {
  if (!userStore.userId) return;

  loading.value = true;
  try {
    coupons.value = await userCouponApi.getCouponsByUser(userStore.userId);
  } catch (error) {
    console.error("获取优惠券失败:", error);
  } finally {
    loading.value = false;
  }
};

// 去使用优惠券
const handleUse = (coupon: UserCoupon) => {
  if (coupon.coupon?.shopId) {
    router.push(`/shop/${coupon.coupon.shopId}`);
  } else {
    router.push("/home");
  }
};

watch(activeTab, () => {
  // Tab切换不需要重新请求，数据已经全部加载
});

onMounted(() => {
  fetchCoupons();
});
</script>

<style lang="scss" scoped>
.coupons-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.coupons-tabs {
  :deep(.el-tabs__header) {
    background-color: #fff;
    margin: 0;
    padding: 0 16px;
  }

  :deep(.el-tabs__item) {
    font-size: 14px;

    &.is-active {
      color: #ff6b00;
    }
  }

  :deep(.el-tabs__active-bar) {
    background-color: #ff6b00;
  }

  :deep(.el-tabs__content) {
    padding: 12px;
  }
}

.coupons-list {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .coupon-item {
    display: flex;
    background-color: #fff;
    border-radius: 8px;
    overflow: hidden;

    &.disabled {
      opacity: 0.6;

      .coupon-left {
        background-color: #999;
      }
    }

    .coupon-left {
      width: 100px;
      padding: 16px;
      background: linear-gradient(135deg, #ff6b00 0%, #ff8533 100%);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .coupon-amount {
        color: #fff;

        .symbol {
          font-size: 12px;
        }

        .value {
          font-size: 28px;
          font-weight: 600;
        }
      }

      .coupon-condition {
        font-size: 10px;
        color: rgba(255, 255, 255, 0.8);
        margin-top: 4px;
      }
    }

    .coupon-right {
      flex: 1;
      padding: 12px 16px;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .coupon-name {
        font-size: 14px;
        font-weight: 500;
        color: #333;
        margin-bottom: 8px;
      }

      .coupon-expire {
        font-size: 12px;
        color: #999;
        margin-bottom: 8px;
      }

      .coupon-status {
        font-size: 12px;
        color: #999;
      }

      .el-button {
        width: fit-content;
        background-color: #ff6b00;
        border-color: #ff6b00;
      }
    }
  }
}
</style>
