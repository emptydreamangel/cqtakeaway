<template>
  <div class="cart-page">
    <NavBar title="购物车" />

    <div class="cart-content" v-loading="loading">
      <!-- 购物车列表 -->
      <div v-if="cartStore.cartItems.length > 0" class="cart-list">
        <div
          v-for="item in cartStore.cartItems"
          :key="item.id"
          class="cart-item"
        >
          <el-checkbox
            :model-value="item.selected === 1"
            @change="handleToggleSelect(item.id)"
          />
          <el-image
            :src="item.product?.image || defaultImage"
            class="item-image"
            fit="cover"
          />
          <div class="item-info">
            <div class="item-name">{{ item.product?.name }}</div>
            <div v-if="item.spec" class="item-spec">{{ item.spec.name }}</div>
            <div class="item-price">
              ¥{{ item.spec?.price || item.product?.price }}
            </div>
          </div>
          <div class="item-actions">
            <el-button
              size="small"
              circle
              @click="handleQuantityChange(item.id, item.quantity - 1)"
            >
              <el-icon><Minus /></el-icon>
            </el-button>
            <span class="quantity">{{ item.quantity }}</span>
            <el-button
              size="small"
              circle
              type="primary"
              @click="handleQuantityChange(item.id, item.quantity + 1)"
            >
              <el-icon><Plus /></el-icon>
            </el-button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <Empty v-else description="购物车是空的">
        <template #default>
          <el-button type="primary" @click="goHome">去点餐</el-button>
        </template>
      </Empty>
    </div>

    <!-- 底部结算栏 -->
    <div v-if="cartStore.cartItems.length > 0" class="cart-footer">
      <div class="footer-left">
        <el-checkbox
          :model-value="cartStore.isAllSelected"
          @change="handleToggleAll"
        >
          全选
        </el-checkbox>
        <el-button type="danger" text @click="handleClear">清空</el-button>
      </div>
      <div class="footer-right">
        <div class="total-info">
          <span class="label">合计：</span>
          <span class="price">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
        </div>
        <el-button
          type="primary"
          :disabled="cartStore.selectedCount === 0"
          @click="handleCheckout"
        >
          去结算({{ cartStore.selectedCount }})
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Minus, Plus } from "@element-plus/icons-vue";
import { useCartStore } from "@/stores/cart";
import NavBar from "@/components/common/NavBar.vue";
import Empty from "@/components/common/Empty.vue";

const router = useRouter();
const cartStore = useCartStore();

const defaultImage =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCI+PHJlY3Qgd2lkdGg9IjYwIiBoZWlnaHQ9IjYwIiBmaWxsPSIjZWVlIi8+PC9zdmc+";

const loading = cartStore.loading;

// 切换选中
const handleToggleSelect = (id: number) => {
  cartStore.toggleSelect(id);
};

// 全选/取消全选
const handleToggleAll = () => {
  cartStore.toggleSelectAll();
};

// 修改数量
const handleQuantityChange = async (id: number, quantity: number) => {
  try {
    await cartStore.updateQuantity(id, quantity);
  } catch (error) {
    ElMessage.error("操作失败");
  }
};

// 清空购物车
const handleClear = async () => {
  try {
    await ElMessageBox.confirm("确定要清空购物车吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    await cartStore.clearCart();
    ElMessage.success("已清空购物车");
  } catch {
    // 取消
  }
};

// 去结算
const handleCheckout = () => {
  router.push("/order/confirm");
};

// 去首页
const goHome = () => {
  router.push("/home");
};

onMounted(() => {
  cartStore.fetchCart();
});
</script>

<style lang="scss" scoped>
.cart-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 70px;
}

.cart-content {
  padding: 12px;
}

.cart-list {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;

  .cart-item {
    display: flex;
    align-items: center;
    padding: 12px;
    border-bottom: 1px solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .el-checkbox {
      margin-right: 12px;
    }

    .item-image {
      width: 60px;
      height: 60px;
      border-radius: 6px;
      flex-shrink: 0;
    }

    .item-info {
      flex: 1;
      margin-left: 12px;
      min-width: 0;

      .item-name {
        font-size: 14px;
        color: #333;
        margin-bottom: 4px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .item-spec {
        font-size: 12px;
        color: #999;
        margin-bottom: 4px;
      }

      .item-price {
        font-size: 14px;
        color: #ff6b00;
      }
    }

    .item-actions {
      display: flex;
      align-items: center;
      gap: 8px;
      flex-shrink: 0;

      .quantity {
        min-width: 24px;
        text-align: center;
        font-size: 14px;
        color: #333;
      }

      .el-button--primary {
        background-color: #ff6b00;
        border-color: #ff6b00;
      }
    }
  }
}

.cart-footer {
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

  .footer-left {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .footer-right {
    display: flex;
    align-items: center;
    gap: 12px;

    .total-info {
      .label {
        font-size: 14px;
        color: #666;
      }

      .price {
        font-size: 18px;
        font-weight: 500;
        color: #ff6b00;
      }
    }

    .el-button {
      padding: 0 24px;
      height: 40px;
      background-color: #ff6b00;
      border-color: #ff6b00;

      &:disabled {
        background-color: #ccc;
        border-color: #ccc;
      }
    }
  }
}
</style>
