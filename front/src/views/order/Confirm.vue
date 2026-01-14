<template>
  <div class="order-confirm-page">
    <NavBar title="确认订单" />

    <!-- 收货地址 -->
    <div class="address-section" @click="goSelectAddress">
      <div v-if="selectedAddress" class="address-info">
        <div class="address-header">
          <span class="name">{{ selectedAddress.contactName }}</span>
          <span class="phone">{{ selectedAddress.contactPhone }}</span>
          <el-tag
            v-if="selectedAddress.isDefault === 1"
            size="small"
            type="warning"
            >默认</el-tag
          >
        </div>
        <div class="address-detail">
          {{ selectedAddress.province }}{{ selectedAddress.city
          }}{{ selectedAddress.district }}{{ selectedAddress.detailAddress }}
        </div>
      </div>
      <div v-else class="no-address">
        <el-icon><Plus /></el-icon>
        <span>添加收货地址</span>
      </div>
      <el-icon class="arrow"><ArrowRight /></el-icon>
    </div>

    <!-- 商品列表 -->
    <div class="goods-section">
      <div class="section-header">
        <span class="shop-name">{{ currentShop?.name || "商家" }}</span>
      </div>
      <div class="goods-list">
        <div
          v-for="item in cartStore.selectedItems"
          :key="item.id"
          class="goods-item"
        >
          <el-image
            :src="item.product?.image || defaultImage"
            class="goods-image"
            fit="cover"
          />
          <div class="goods-info">
            <div class="goods-name">{{ item.product?.name }}</div>
            <div v-if="item.spec" class="goods-spec">{{ item.spec.name }}</div>
          </div>
          <div class="goods-right">
            <div class="goods-price">
              ¥{{ item.spec?.price || item.product?.price }}
            </div>
            <div class="goods-quantity">x{{ item.quantity }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 配送信息 -->
    <div class="delivery-section">
      <div class="delivery-item">
        <span class="label">配送方式</span>
        <span class="value">商家配送</span>
      </div>
      <div class="delivery-item">
        <span class="label">预计送达</span>
        <span class="value">{{ expectedTime }}</span>
      </div>
    </div>

    <!-- 备注 -->
    <div class="remark-section">
      <el-input
        v-model="remark"
        type="textarea"
        :rows="2"
        placeholder="选填，请输入备注信息"
        maxlength="100"
        show-word-limit
      />
    </div>

    <!-- 优惠券 -->
    <div class="coupon-section" @click="showCouponPicker = true">
      <span class="label">优惠券</span>
      <div class="coupon-value">
        <span v-if="selectedCoupon" class="discount"
          >-¥{{ couponDiscount.toFixed(2) }}</span
        >
        <span v-else class="count">{{ availableCoupons.length }}张可用</span>
        <el-icon><ArrowRight /></el-icon>
      </div>
    </div>

    <!-- 金额明细 -->
    <div class="amount-section">
      <div class="amount-item">
        <span class="label">商品总价</span>
        <span class="value">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
      </div>
      <div class="amount-item">
        <span class="label">配送费</span>
        <span class="value">¥{{ deliveryFee.toFixed(2) }}</span>
      </div>
      <div v-if="couponDiscount > 0" class="amount-item discount">
        <span class="label">优惠券</span>
        <span class="value">-¥{{ couponDiscount.toFixed(2) }}</span>
      </div>
    </div>

    <!-- 底部提交 -->
    <div class="submit-bar">
      <div class="total-info">
        <span class="label">合计：</span>
        <span class="price">¥{{ totalAmount.toFixed(2) }}</span>
      </div>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        提交订单
      </el-button>
    </div>

    <!-- 优惠券选择弹窗 -->
    <el-drawer
      v-model="showCouponPicker"
      title="选择优惠券"
      direction="btt"
      size="50%"
    >
      <div class="coupon-list">
        <div
          v-for="coupon in availableCoupons"
          :key="coupon.id"
          class="coupon-item"
          :class="{ selected: selectedCoupon?.id === coupon.id }"
          @click="handleSelectCoupon(coupon)"
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
          </div>
        </div>
        <Empty
          v-if="availableCoupons.length === 0"
          description="暂无可用优惠券"
        />
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { Plus, ArrowRight } from "@element-plus/icons-vue";
import { addressApi, orderApi, userCouponApi, shopApi } from "@/api";
import type { UserAddress, UserCoupon, Shop, CreateOrderParams } from "@/types";
import { useCartStore } from "@/stores/cart";
import { useUserStore } from "@/stores/user";
import { formatDate } from "@/utils";
import NavBar from "@/components/common/NavBar.vue";
import Empty from "@/components/common/Empty.vue";

const router = useRouter();
const cartStore = useCartStore();
const userStore = useUserStore();

const defaultImage =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCI+PHJlY3Qgd2lkdGg9IjYwIiBoZWlnaHQ9IjYwIiBmaWxsPSIjZWVlIi8+PC9zdmc+";

const selectedAddress = ref<UserAddress | null>(null);
const currentShop = ref<Shop | null>(null);
const remark = ref("");
const availableCoupons = ref<UserCoupon[]>([]);
const selectedCoupon = ref<UserCoupon | null>(null);
const showCouponPicker = ref(false);
const submitting = ref(false);

// 配送费
const deliveryFee = computed(() => currentShop.value?.deliveryFee || 0);

// 优惠金额
const couponDiscount = computed(() => {
  if (!selectedCoupon.value?.coupon) return 0;
  return selectedCoupon.value.coupon.discountAmount || 0;
});

// 预计送达时间
const expectedTime = computed(() => {
  const time = currentShop.value?.deliveryTime || 30;
  const now = new Date();
  now.setMinutes(now.getMinutes() + time);
  const hours = String(now.getHours()).padStart(2, "0");
  const minutes = String(now.getMinutes()).padStart(2, "0");
  return `${hours}:${minutes}`;
});

// 总金额
const totalAmount = computed(() => {
  return Math.max(
    0,
    cartStore.totalPrice + deliveryFee.value - couponDiscount.value
  );
});

// 获取默认地址
const fetchDefaultAddress = async () => {
  if (!userStore.userId) return;
  try {
    const res = await addressApi.getDefaultAddress(userStore.userId);
    selectedAddress.value = res;
  } catch {
    // 没有默认地址
  }
};

// 获取商家信息
const fetchShopInfo = async () => {
  if (!cartStore.currentShopId) return;
  try {
    currentShop.value = await shopApi.getShopById(cartStore.currentShopId);
  } catch (error) {
    console.error("获取商家信息失败:", error);
  }
};

// 获取可用优惠券
const fetchAvailableCoupons = async () => {
  if (!userStore.userId) return;
  try {
    const res = await userCouponApi.getAvailableCouponsByUser(userStore.userId);
    // 筛选满足条件的优惠券
    availableCoupons.value = res.filter((c) => {
      const minAmount = c.coupon?.minAmount || 0;
      return cartStore.totalPrice >= minAmount;
    });
  } catch (error) {
    console.error("获取优惠券失败:", error);
  }
};

// 选择优惠券
const handleSelectCoupon = (coupon: UserCoupon) => {
  if (selectedCoupon.value?.id === coupon.id) {
    selectedCoupon.value = null;
  } else {
    selectedCoupon.value = coupon;
  }
  showCouponPicker.value = false;
};

// 去选择地址
const goSelectAddress = () => {
  router.push("/address");
};

// 提交订单
const handleSubmit = async () => {
  if (!selectedAddress.value) {
    ElMessage.warning("请选择收货地址");
    return;
  }

  if (cartStore.selectedItems.length === 0) {
    ElMessage.warning("请选择商品");
    return;
  }

  if (!userStore.userId || !cartStore.currentShopId) {
    ElMessage.error("订单信息不完整");
    return;
  }

  submitting.value = true;
  try {
    const params: CreateOrderParams = {
      userId: userStore.userId,
      shopId: cartStore.currentShopId,
      addressId: selectedAddress.value.id,
      remark: remark.value,
      items: cartStore.selectedItems.map((item) => ({
        productId: item.productId,
        specId: item.specId,
        quantity: item.quantity,
      })),
      couponId: selectedCoupon.value?.couponId,
    };

    const order = await orderApi.createOrder(params);
    ElMessage.success("订单创建成功");

    // 清空购物车
    await cartStore.clearCart();

    // 跳转到支付页面
    router.replace(`/pay/${order.id}`);
  } catch (error: any) {
    ElMessage.error(error?.response?.data?.message || "创建订单失败");
  } finally {
    submitting.value = false;
  }
};

onMounted(() => {
  fetchDefaultAddress();
  fetchShopInfo();
  fetchAvailableCoupons();
});
</script>

<style lang="scss" scoped>
.order-confirm-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 70px;
}

.address-section {
  display: flex;
  align-items: center;
  background-color: #fff;
  padding: 16px;
  margin-bottom: 12px;
  cursor: pointer;

  .address-info {
    flex: 1;

    .address-header {
      display: flex;
      align-items: center;
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

  .no-address {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    color: #999;

    .el-icon {
      font-size: 18px;
    }
  }

  .arrow {
    font-size: 16px;
    color: #999;
  }
}

.goods-section {
  background-color: #fff;
  margin-bottom: 12px;

  .section-header {
    padding: 12px 16px;
    border-bottom: 1px solid #f5f5f5;

    .shop-name {
      font-size: 14px;
      font-weight: 500;
      color: #333;
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

.delivery-section {
  background-color: #fff;
  padding: 12px 16px;
  margin-bottom: 12px;

  .delivery-item {
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

.remark-section {
  background-color: #fff;
  padding: 12px 16px;
  margin-bottom: 12px;
}

.coupon-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  padding: 16px;
  margin-bottom: 12px;
  cursor: pointer;

  .label {
    font-size: 14px;
    color: #333;
  }

  .coupon-value {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;

    .discount {
      color: #ff6b00;
    }

    .count {
      color: #999;
    }

    .el-icon {
      color: #999;
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
  }
}

.submit-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 16px;
  background-color: #fff;
  border-top: 1px solid #eee;

  .total-info {
    .label {
      font-size: 14px;
      color: #666;
    }

    .price {
      font-size: 20px;
      font-weight: 500;
      color: #ff6b00;
    }
  }

  .el-button {
    padding: 0 32px;
    height: 40px;
    font-size: 14px;
    background-color: #ff6b00;
    border-color: #ff6b00;
  }
}

.coupon-list {
  padding: 16px;

  .coupon-item {
    display: flex;
    padding: 16px;
    margin-bottom: 12px;
    background-color: #fff5f0;
    border: 1px solid #ffe0cc;
    border-radius: 8px;
    cursor: pointer;

    &.selected {
      border-color: #ff6b00;
    }

    .coupon-left {
      width: 80px;
      text-align: center;
      border-right: 1px dashed #ffcca8;
      padding-right: 16px;

      .coupon-amount {
        color: #ff6b00;

        .symbol {
          font-size: 12px;
        }

        .value {
          font-size: 24px;
          font-weight: 600;
        }
      }

      .coupon-condition {
        font-size: 12px;
        color: #999;
        margin-top: 4px;
      }
    }

    .coupon-right {
      flex: 1;
      padding-left: 16px;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .coupon-name {
        font-size: 14px;
        color: #333;
        font-weight: 500;
      }

      .coupon-expire {
        font-size: 12px;
        color: #999;
        margin-top: 8px;
      }
    }
  }
}
</style>
