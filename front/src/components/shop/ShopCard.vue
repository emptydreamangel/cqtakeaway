<template>
  <div class="shop-card" @click="handleClick">
    <div class="shop-card-logo">
      <el-image :src="shop.logo || defaultLogo" fit="cover" />
    </div>
    <div class="shop-card-content">
      <div class="shop-card-header">
        <span class="shop-name">{{ shop.name }}</span>
        <el-tag v-if="shop.isAuthenticated === 1" size="small" type="success"
          >认证</el-tag
        >
      </div>
      <div class="shop-card-info">
        <span class="rating">
          <el-icon><Star /></el-icon>
          {{ shop.rating?.toFixed(1) || "5.0" }}
        </span>
        <span class="sales">月售{{ shop.monthlySales || 0 }}</span>
        <span class="delivery-time">{{ shop.deliveryTime || 30 }}分钟</span>
      </div>
      <div class="shop-card-tags">
        <span class="min-price">¥{{ shop.minPrice || 0 }}起送</span>
        <span class="delivery-fee">配送费¥{{ shop.deliveryFee || 0 }}</span>
      </div>
      <div v-if="shop.notice" class="shop-card-notice">
        <el-icon><BellFilled /></el-icon>
        <span>{{ shop.notice }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Star, BellFilled } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import type { Shop } from "@/types";

interface Props {
  shop: Shop;
}

const props = defineProps<Props>();
const router = useRouter();

const defaultLogo =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIj48cmVjdCB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgZmlsbD0iI2VlZSIvPjx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBkb21pbmFudC1iYXNlbGluZT0ibWlkZGxlIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBmaWxsPSIjOTk5IiBmb250LXNpemU9IjE0Ij7llYblrrY8L3RleHQ+PC9zdmc+";

const handleClick = () => {
  router.push(`/shop/${props.shop.id}`);
};
</script>

<style lang="scss" scoped>
.shop-card {
  display: flex;
  padding: 12px;
  background-color: #fff;
  border-radius: 8px;
  cursor: pointer;
  transition: box-shadow 0.3s;

  &:hover {
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }

  &-logo {
    width: 80px;
    height: 80px;
    flex-shrink: 0;
    border-radius: 8px;
    overflow: hidden;
    margin-right: 12px;

    .el-image {
      width: 100%;
      height: 100%;
    }
  }

  &-content {
    flex: 1;
    min-width: 0;
  }

  &-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 6px;

    .shop-name {
      font-size: 16px;
      font-weight: 500;
      color: #333;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  &-info {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 12px;
    color: #666;
    margin-bottom: 6px;

    .rating {
      display: flex;
      align-items: center;
      gap: 2px;
      color: #ff6b00;

      .el-icon {
        font-size: 14px;
      }
    }
  }

  &-tags {
    display: flex;
    gap: 8px;
    font-size: 12px;
    color: #999;
    margin-bottom: 6px;
  }

  &-notice {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #999;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;

    .el-icon {
      color: #ff6b00;
      font-size: 14px;
    }
  }
}
</style>
