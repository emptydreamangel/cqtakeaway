<template>
  <div class="search-page">
    <!-- 搜索栏 -->
    <div class="search-header">
      <div class="search-input">
        <el-icon><Search /></el-icon>
        <input
          v-model="keyword"
          type="text"
          placeholder="搜索商家或商品"
          @keyup.enter="handleSearch"
          ref="inputRef"
        />
        <el-icon v-if="keyword" class="clear-icon" @click="clearKeyword"
          ><Close
        /></el-icon>
      </div>
      <span class="cancel-btn" @click="goBack">取消</span>
    </div>

    <!-- 搜索历史 -->
    <div v-if="!searched" class="search-history">
      <div class="history-header">
        <span class="title">搜索历史</span>
        <el-icon class="clear-all" @click="clearHistory"><Delete /></el-icon>
      </div>
      <div class="history-tags">
        <span
          v-for="item in searchHistory"
          :key="item"
          class="history-tag"
          @click="handleHistoryClick(item)"
        >
          {{ item }}
        </span>
        <span v-if="searchHistory.length === 0" class="no-history"
          >暂无搜索历史</span
        >
      </div>
    </div>

    <!-- 搜索结果 -->
    <div v-else class="search-results">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="商家" name="shop">
          <div class="result-list" v-loading="loading">
            <ShopCard v-for="shop in shops" :key="shop.id" :shop="shop" />
            <Empty
              v-if="!loading && shops.length === 0"
              description="未找到相关商家"
            />
          </div>
        </el-tab-pane>
        <el-tab-pane label="商品" name="product">
          <div class="result-list" v-loading="loading">
            <div
              v-for="product in products"
              :key="product.id"
              class="product-item"
              @click="goProduct(product)"
            >
              <el-image
                :src="product.image || defaultImage"
                class="product-image"
                fit="cover"
              />
              <div class="product-info">
                <div class="product-name">{{ product.name }}</div>
                <div class="product-price">¥{{ product.price }}</div>
              </div>
            </div>
            <Empty
              v-if="!loading && products.length === 0"
              description="未找到相关商品"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import { Search, Close, Delete } from "@element-plus/icons-vue";
import { shopApi, productApi } from "@/api";
import type { Shop, Product } from "@/types";
import { getStorage, setStorage } from "@/utils/storage";
import ShopCard from "@/components/shop/ShopCard.vue";
import Empty from "@/components/common/Empty.vue";

const router = useRouter();

const HISTORY_KEY = "search_history";

const inputRef = ref<HTMLInputElement | null>(null);
const keyword = ref("");
const searched = ref(false);
const loading = ref(false);
const activeTab = ref("shop");
const shops = ref<Shop[]>([]);
const products = ref<Product[]>([]);
const searchHistory = ref<string[]>([]);

const defaultImage =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCI+PHJlY3Qgd2lkdGg9IjYwIiBoZWlnaHQ9IjYwIiBmaWxsPSIjZWVlIi8+PC9zdmc+";

// 加载搜索历史
const loadHistory = () => {
  searchHistory.value = getStorage<string[]>(HISTORY_KEY) || [];
};

// 保存搜索历史
const saveHistory = (kw: string) => {
  const history = searchHistory.value.filter((h) => h !== kw);
  history.unshift(kw);
  searchHistory.value = history.slice(0, 10); // 最多保存10条
  setStorage(HISTORY_KEY, searchHistory.value);
};

// 清除搜索历史
const clearHistory = () => {
  searchHistory.value = [];
  setStorage(HISTORY_KEY, []);
};

// 搜索
const handleSearch = async () => {
  if (!keyword.value.trim()) return;

  searched.value = true;
  loading.value = true;
  saveHistory(keyword.value.trim());

  try {
    const [shopRes, productRes] = await Promise.all([
      shopApi.searchShops(keyword.value),
      productApi.searchProducts(keyword.value),
    ]);
    shops.value = shopRes;
    products.value = productRes;
  } catch (error) {
    console.error("搜索失败:", error);
  } finally {
    loading.value = false;
  }
};

// 点击历史记录
const handleHistoryClick = (kw: string) => {
  keyword.value = kw;
  handleSearch();
};

// 清空关键词
const clearKeyword = () => {
  keyword.value = "";
  searched.value = false;
  shops.value = [];
  products.value = [];
};

// 跳转商品详情（跳转到商家页面）
const goProduct = (product: Product) => {
  router.push(`/shop/${product.shopId}`);
};

// 返回
const goBack = () => {
  router.back();
};

onMounted(() => {
  loadHistory();
  nextTick(() => {
    inputRef.value?.focus();
  });
});
</script>

<style lang="scss" scoped>
.search-page {
  min-height: 100vh;
  background-color: #fff;
}

.search-header {
  display: flex;
  align-items: center;
  padding: 12px;
  gap: 12px;

  .search-input {
    flex: 1;
    display: flex;
    align-items: center;
    height: 36px;
    padding: 0 12px;
    background-color: #f5f5f5;
    border-radius: 18px;
    gap: 8px;

    .el-icon {
      font-size: 16px;
      color: #999;
    }

    input {
      flex: 1;
      border: none;
      background: none;
      outline: none;
      font-size: 14px;
      color: #333;

      &::placeholder {
        color: #999;
      }
    }

    .clear-icon {
      cursor: pointer;
    }
  }

  .cancel-btn {
    font-size: 14px;
    color: #666;
    cursor: pointer;
  }
}

.search-history {
  padding: 16px;

  .history-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;

    .title {
      font-size: 14px;
      font-weight: 500;
      color: #333;
    }

    .clear-all {
      font-size: 18px;
      color: #999;
      cursor: pointer;
    }
  }

  .history-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;

    .history-tag {
      padding: 6px 12px;
      font-size: 13px;
      color: #666;
      background-color: #f5f5f5;
      border-radius: 16px;
      cursor: pointer;

      &:hover {
        background-color: #eee;
      }
    }

    .no-history {
      font-size: 14px;
      color: #999;
    }
  }
}

.search-results {
  padding: 0 12px;

  :deep(.el-tabs__item) {
    font-size: 14px;

    &.is-active {
      color: #ff6b00;
    }
  }

  :deep(.el-tabs__active-bar) {
    background-color: #ff6b00;
  }

  .result-list {
    padding: 12px 0;
  }

  .product-item {
    display: flex;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;

    .product-image {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      flex-shrink: 0;
    }

    .product-info {
      flex: 1;
      margin-left: 12px;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .product-name {
        font-size: 14px;
        color: #333;
        margin-bottom: 8px;
      }

      .product-price {
        font-size: 14px;
        color: #ff6b00;
      }
    }
  }
}
</style>
