<template>
  <div class="home-page">
    <!-- 顶部搜索栏 -->
    <div class="search-header">
      <div class="location">
        <el-icon><Location /></el-icon>
        <span>{{ currentLocation }}</span>
      </div>
      <div class="search-bar" @click="goSearch">
        <el-icon><Search /></el-icon>
        <span>搜索商家或商品</span>
      </div>
    </div>

    <!-- 分类导航 -->
    <div class="category-section">
      <div class="category-grid">
        <div
          v-for="category in categories"
          :key="category.category_id"
          class="category-item"
          @click="handleCategoryClick(category)"
        >
          <div class="category-icon">
            <el-icon><Food /></el-icon>
          </div>
          <span class="category-name">{{ category.category_name }}</span>
        </div>
      </div>
    </div>

    <!-- 推荐商家 -->
    <div class="recommend-section">
      <div class="section-header">
        <span class="section-title">推荐商家</span>
        <span class="section-more" @click="goShopList">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </span>
      </div>
      <div class="shop-list" v-loading="loading">
        <ShopCard v-for="shop in shops" :key="shop.id" :shop="shop" />
        <Empty
          v-if="!loading && shops.length === 0"
          description="暂无推荐商家"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { Location, Search, Food, ArrowRight } from "@element-plus/icons-vue";
import { shopApi, shopCategoryApi } from "@/api";
import type { Shop, ShopCategory } from "@/types";
import ShopCard from "@/components/shop/ShopCard.vue";
import Empty from "@/components/common/Empty.vue";

const router = useRouter();

const currentLocation = ref("定位中...");
const categories = ref<ShopCategory[]>([]);
const shops = ref<Shop[]>([]);
const loading = ref(false);

// 获取商家分类
const fetchCategories = async () => {
  try {
    const res = await shopCategoryApi.getAllCategories();
    categories.value = res.slice(0, 8); // 只显示前8个
  } catch (error) {
    console.error("获取分类失败:", error);
  }
};

// 获取推荐商家
const fetchShops = async () => {
  loading.value = true;
  try {
    const res = await shopApi.getBusinessShops();
    shops.value = res.slice(0, 10); // 只显示前10个
  } catch (error) {
    console.error("获取商家失败:", error);
  } finally {
    loading.value = false;
  }
};

// 去搜索页
const goSearch = () => {
  router.push("/search");
};

// 去商家列表
const goShopList = () => {
  router.push("/shop");
};

// 点击分类
const handleCategoryClick = (category: ShopCategory) => {
  router.push(`/shop?categoryId=${category.category_id}`);
};

onMounted(() => {
  currentLocation.value = "重庆市渝北区";
  fetchCategories();
  fetchShops();
});
</script>

<style lang="scss" scoped>
.home-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.search-header {
  display: flex;
  align-items: center;
  padding: 12px;
  background-color: #ff6b00;

  .location {
    display: flex;
    align-items: center;
    gap: 4px;
    color: #fff;
    font-size: 14px;
    margin-right: 12px;
    flex-shrink: 0;

    .el-icon {
      font-size: 16px;
    }
  }

  .search-bar {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 8px;
    height: 36px;
    padding: 0 12px;
    background-color: #fff;
    border-radius: 18px;
    color: #999;
    font-size: 14px;
    cursor: pointer;
  }
}

.category-section {
  background-color: #fff;
  padding: 16px 12px;
  margin-bottom: 12px;

  .category-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
  }

  .category-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;

    .category-icon {
      width: 48px;
      height: 48px;
      background-color: #fff5f0;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 8px;

      .el-icon {
        font-size: 24px;
        color: #ff6b00;
      }
    }

    .category-name {
      font-size: 12px;
      color: #333;
    }
  }
}

.recommend-section {
  background-color: #fff;
  padding: 16px 12px;

  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;

    .section-title {
      font-size: 16px;
      font-weight: 500;
      color: #333;
    }

    .section-more {
      display: flex;
      align-items: center;
      font-size: 12px;
      color: #999;
      cursor: pointer;

      .el-icon {
        font-size: 14px;
      }
    }
  }

  .shop-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
}
</style>
