<template>
  <div class="shop-list-page">
    <NavBar title="商家列表" />

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div
        v-for="item in filterOptions"
        :key="item.value"
        class="filter-item"
        :class="{ active: currentFilter === item.value }"
        @click="handleFilter(item.value)"
      >
        {{ item.label }}
      </div>
    </div>

    <!-- 分类选择 -->
    <div v-if="categories.length > 0" class="category-bar">
      <div class="category-scroll">
        <div
          class="category-tag"
          :class="{ active: !currentCategory }"
          @click="handleCategoryChange(null)"
        >
          全部
        </div>
        <div
          v-for="cat in categories"
          :key="cat.id"
          class="category-tag"
          :class="{ active: currentCategory === cat.id }"
          @click="handleCategoryChange(cat.id)"
        >
          {{ cat.name }}
        </div>
      </div>
    </div>

    <!-- 商家列表 -->
    <div class="shop-list" v-loading="loading">
      <ShopCard v-for="shop in shops" :key="shop.id" :shop="shop" />
      <Empty v-if="!loading && shops.length === 0" description="暂无商家" />
    </div>

    <!-- 加载更多 -->
    <div v-if="hasMore && !loading" class="load-more" @click="loadMore">
      加载更多
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { shopApi, shopCategoryApi } from "@/api";
import type { Shop, ShopCategory } from "@/types";
import NavBar from "@/components/common/NavBar.vue";
import ShopCard from "@/components/shop/ShopCard.vue";
import Empty from "@/components/common/Empty.vue";

const route = useRoute();

const filterOptions = [
  { label: "综合排序", value: "default" },
  { label: "销量最高", value: "sales" },
  { label: "评分最高", value: "rating" },
];

const categories = ref<ShopCategory[]>([]);
const shops = ref<Shop[]>([]);
const loading = ref(false);
const currentFilter = ref("default");
const currentCategory = ref<number | null>(null);
const pageNum = ref(1);
const pageSize = ref(10);
const hasMore = ref(true);

// 获取分类列表
const fetchCategories = async () => {
  try {
    const res = await shopCategoryApi.getAllCategories();
    categories.value = res;
  } catch (error) {
    console.error("获取分类失败:", error);
  }
};

// 获取商家列表
const fetchShops = async (reset = false) => {
  if (reset) {
    pageNum.value = 1;
    shops.value = [];
  }

  loading.value = true;
  try {
    let res: Shop[] = [];

    if (currentFilter.value === "sales") {
      res = await shopApi.getTopSalesShops(50);
    } else if (currentFilter.value === "rating") {
      res = await shopApi.getTopRatingShops(50);
    } else if (currentCategory.value) {
      res = await shopApi.getShopsByCategory(currentCategory.value);
    } else {
      res = await shopApi.getBusinessShops();
    }

    // 分页处理
    const start = (pageNum.value - 1) * pageSize.value;
    const end = start + pageSize.value;
    const pageData = res.slice(start, end);

    if (reset) {
      shops.value = pageData;
    } else {
      shops.value.push(...pageData);
    }

    hasMore.value = end < res.length;
  } catch (error) {
    console.error("获取商家失败:", error);
  } finally {
    loading.value = false;
  }
};

// 筛选
const handleFilter = (filter: string) => {
  currentFilter.value = filter;
  fetchShops(true);
};

// 分类变化
const handleCategoryChange = (categoryId: number | null) => {
  currentCategory.value = categoryId;
  currentFilter.value = "default";
  fetchShops(true);
};

// 加载更多
const loadMore = () => {
  pageNum.value++;
  fetchShops();
};

// 监听路由参数
watch(
  () => route.query.categoryId,
  (val) => {
    if (val) {
      currentCategory.value = Number(val);
      fetchShops(true);
    }
  },
  { immediate: true }
);

onMounted(() => {
  fetchCategories();
  if (!route.query.categoryId) {
    fetchShops(true);
  }
});
</script>

<style lang="scss" scoped>
.shop-list-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.filter-bar {
  display: flex;
  background-color: #fff;
  padding: 12px;
  gap: 24px;

  .filter-item {
    font-size: 14px;
    color: #666;
    cursor: pointer;
    transition: color 0.3s;

    &.active {
      color: #ff6b00;
      font-weight: 500;
    }
  }
}

.category-bar {
  background-color: #fff;
  padding: 12px;
  border-top: 1px solid #f5f5f5;

  .category-scroll {
    display: flex;
    gap: 8px;
    overflow-x: auto;

    &::-webkit-scrollbar {
      display: none;
    }
  }

  .category-tag {
    flex-shrink: 0;
    padding: 6px 16px;
    font-size: 13px;
    color: #666;
    background-color: #f5f5f5;
    border-radius: 16px;
    cursor: pointer;
    transition: all 0.3s;

    &.active {
      color: #fff;
      background-color: #ff6b00;
    }
  }
}

.shop-list {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.load-more {
  text-align: center;
  padding: 16px;
  color: #666;
  font-size: 14px;
  cursor: pointer;

  &:hover {
    color: #ff6b00;
  }
}
</style>
