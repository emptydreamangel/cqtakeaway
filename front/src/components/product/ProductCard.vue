<template>
  <div class="product-card" @click="handleClick">
    <div class="product-card-image">
      <el-image :src="product.image || defaultImage" fit="cover" />
    </div>
    <div class="product-card-content">
      <div class="product-name">{{ product.name }}</div>
      <div v-if="product.description" class="product-desc">
        {{ product.description }}
      </div>
      <div class="product-sales">月售{{ product.sales || 0 }}</div>
      <div class="product-footer">
        <div class="product-price">
          <span class="current">¥{{ product.price }}</span>
          <span v-if="product.originalPrice" class="original"
            >¥{{ product.originalPrice }}</span
          >
        </div>
        <el-button
          v-if="showAddBtn"
          type="primary"
          size="small"
          circle
          @click.stop="handleAdd"
        >
          <el-icon><Plus /></el-icon>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Plus } from "@element-plus/icons-vue";
import type { Product } from "@/types";

interface Props {
  product: Product;
  showAddBtn?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  showAddBtn: true,
});

const emit = defineEmits(["click", "add"]);

const defaultImage =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIj48cmVjdCB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgZmlsbD0iI2VlZSIvPjx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBkb21pbmFudC1iYXNlbGluZT0ibWlkZGxlIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBmaWxsPSIjOTk5IiBmb250LXNpemU9IjE0Ij7llYblk4E8L3RleHQ+PC9zdmc+";

const handleClick = () => {
  emit("click", props.product);
};

const handleAdd = () => {
  emit("add", props.product);
};
</script>

<style lang="scss" scoped>
.product-card {
  display: flex;
  padding: 12px;
  background-color: #fff;
  cursor: pointer;

  &-image {
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
    display: flex;
    flex-direction: column;
  }

  .product-name {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    margin-bottom: 4px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .product-desc {
    font-size: 12px;
    color: #999;
    margin-bottom: 4px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .product-sales {
    font-size: 12px;
    color: #999;
    margin-bottom: 8px;
  }

  .product-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: auto;
  }

  .product-price {
    .current {
      font-size: 16px;
      font-weight: 500;
      color: #ff6b00;
    }

    .original {
      font-size: 12px;
      color: #999;
      text-decoration: line-through;
      margin-left: 4px;
    }
  }
}
</style>
