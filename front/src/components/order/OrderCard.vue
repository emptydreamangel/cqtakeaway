<template>
  <div class="order-card" @click="handleClick">
    <div class="order-header">
      <div class="shop-info">
        <el-image
          :src="order.shop?.logo || defaultLogo"
          class="shop-logo"
          fit="cover"
        />
        <span class="shop-name">{{ order.shop?.name || "商家" }}</span>
      </div>
      <el-tag :type="getStatusType(order.status)" size="small">
        {{ getStatusText(order.status) }}
      </el-tag>
    </div>

    <div class="order-items">
      <div v-for="item in displayItems" :key="item.id" class="order-item">
        <el-image
          :src="item.productImage || defaultImage"
          class="item-image"
          fit="cover"
        />
        <div class="item-info">
          <div class="item-name">{{ item.productName }}</div>
          <div v-if="item.specName" class="item-spec">{{ item.specName }}</div>
        </div>
        <div class="item-count">x{{ item.quantity }}</div>
      </div>
      <div v-if="order.items && order.items.length > 2" class="more-items">
        共{{ order.items.length }}件商品
      </div>
    </div>

    <div class="order-footer">
      <div class="order-total">
        实付
        <span class="price">¥{{ order.payAmount?.toFixed(2) || "0.00" }}</span>
      </div>
      <div class="order-actions">
        <el-button
          v-if="order.status === 0"
          type="primary"
          size="small"
          @click.stop="handlePay"
        >
          去支付
        </el-button>
        <el-button
          v-if="order.status === 4"
          size="small"
          @click.stop="handleReview"
        >
          评价
        </el-button>
        <el-button
          v-if="order.status === 4"
          size="small"
          @click.stop="handleReorder"
        >
          再来一单
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRouter } from "vue-router";
import type { Order } from "@/types";
import { getOrderStatusText, getOrderStatusType } from "@/utils";

interface Props {
  order: Order;
}

const props = defineProps<Props>();
const emit = defineEmits(["pay", "review", "reorder"]);
const router = useRouter();

const defaultLogo =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI0MCIgaGVpZ2h0PSI0MCI+PHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiBmaWxsPSIjZWVlIi8+PC9zdmc+";
const defaultImage =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI1MCIgaGVpZ2h0PSI1MCI+PHJlY3Qgd2lkdGg9IjUwIiBoZWlnaHQ9IjUwIiBmaWxsPSIjZWVlIi8+PC9zdmc+";

const displayItems = computed(() => {
  return (props.order.items || []).slice(0, 2);
});

const getStatusText = (status: number) => getOrderStatusText(status);
const getStatusType = (status: number) => getOrderStatusType(status) as any;

const handleClick = () => {
  router.push(`/order/${props.order.id}`);
};

const handlePay = () => {
  emit("pay", props.order);
  router.push(`/pay/${props.order.id}`);
};

const handleReview = () => {
  emit("review", props.order);
};

const handleReorder = () => {
  emit("reorder", props.order);
};
</script>

<style lang="scss" scoped>
.order-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
  cursor: pointer;

  .order-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
    padding-bottom: 12px;
    border-bottom: 1px solid #f5f5f5;

    .shop-info {
      display: flex;
      align-items: center;
      gap: 8px;

      .shop-logo {
        width: 24px;
        height: 24px;
        border-radius: 4px;
      }

      .shop-name {
        font-size: 14px;
        font-weight: 500;
        color: #333;
      }
    }
  }

  .order-items {
    margin-bottom: 12px;

    .order-item {
      display: flex;
      align-items: center;
      padding: 8px 0;

      .item-image {
        width: 50px;
        height: 50px;
        border-radius: 4px;
        flex-shrink: 0;
      }

      .item-info {
        flex: 1;
        margin-left: 12px;
        min-width: 0;

        .item-name {
          font-size: 14px;
          color: #333;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .item-spec {
          font-size: 12px;
          color: #999;
          margin-top: 4px;
        }
      }

      .item-count {
        font-size: 14px;
        color: #666;
        flex-shrink: 0;
      }
    }

    .more-items {
      font-size: 12px;
      color: #999;
      text-align: center;
      padding-top: 8px;
    }
  }

  .order-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-top: 12px;
    border-top: 1px solid #f5f5f5;

    .order-total {
      font-size: 14px;
      color: #666;

      .price {
        font-size: 16px;
        font-weight: 500;
        color: #333;
      }
    }

    .order-actions {
      display: flex;
      gap: 8px;
    }
  }
}
</style>
