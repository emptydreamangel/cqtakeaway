<template>
  <div class="cart-bar" v-if="totalCount > 0">
    <div class="cart-bar-left" @click="handleCartClick">
      <div class="cart-icon">
        <el-icon><ShoppingCart /></el-icon>
        <span class="cart-badge">{{ totalCount }}</span>
      </div>
      <div class="cart-info">
        <div class="cart-total">¥{{ totalPrice.toFixed(2) }}</div>
        <div class="cart-delivery">另需配送费¥{{ deliveryFee.toFixed(2) }}</div>
      </div>
    </div>
    <div class="cart-bar-right">
      <el-button type="primary" :disabled="!canSubmit" @click="handleSubmit">
        {{ submitText }}
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { ShoppingCart } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import { useCartStore } from "@/stores/cart";

interface Props {
  minPrice?: number;
  deliveryFee?: number;
}

const props = withDefaults(defineProps<Props>(), {
  minPrice: 0,
  deliveryFee: 0,
});

const emit = defineEmits(["cartClick"]);

const cartStore = useCartStore();
const router = useRouter();

const totalCount = computed(() => cartStore.selectedCount);
const totalPrice = computed(() => cartStore.totalPrice);

const canSubmit = computed(() => totalPrice.value >= props.minPrice);

const submitText = computed(() => {
  if (totalPrice.value < props.minPrice) {
    const diff = props.minPrice - totalPrice.value;
    return `还差¥${diff.toFixed(2)}起送`;
  }
  return "去结算";
});

const handleCartClick = () => {
  emit("cartClick");
};

const handleSubmit = () => {
  if (canSubmit.value) {
    router.push("/order/confirm");
  }
};
</script>

<style lang="scss" scoped>
.cart-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50px;
  display: flex;
  align-items: center;
  background-color: #3d3d3d;
  padding: 0 12px;
  z-index: 1000;

  &-left {
    flex: 1;
    display: flex;
    align-items: center;
    cursor: pointer;

    .cart-icon {
      position: relative;
      width: 50px;
      height: 50px;
      background-color: #3d3d3d;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: -20px;
      border: 4px solid #2d2d2d;

      .el-icon {
        font-size: 24px;
        color: #fff;
      }

      .cart-badge {
        position: absolute;
        top: 0;
        right: 0;
        min-width: 18px;
        height: 18px;
        line-height: 18px;
        padding: 0 6px;
        font-size: 12px;
        color: #fff;
        background-color: #ff6b00;
        border-radius: 9px;
        text-align: center;
      }
    }

    .cart-info {
      margin-left: 12px;

      .cart-total {
        font-size: 18px;
        font-weight: 500;
        color: #fff;
      }

      .cart-delivery {
        font-size: 12px;
        color: #999;
      }
    }
  }

  &-right {
    .el-button {
      padding: 0 24px;
      height: 36px;
      font-size: 14px;
      background-color: #ff6b00;
      border-color: #ff6b00;

      &:disabled {
        background-color: #666;
        border-color: #666;
      }
    }
  }
}
</style>
