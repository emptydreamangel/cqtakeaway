<template>
  <div class="favorites-page">
    <NavBar title="我的收藏" />

    <el-tabs v-model="activeTab" class="favorites-tabs">
      <el-tab-pane label="收藏商家" name="shop">
        <div class="favorites-list" v-loading="loading">
          <ShopCard
            v-for="fav in shopFavorites"
            :key="fav.id"
            :shop="(fav as any).shop"
          />
          <Empty
            v-if="!loading && shopFavorites.length === 0"
            description="暂无收藏商家"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="收藏商品" name="product">
        <div class="favorites-list" v-loading="loading">
          <div
            v-for="fav in productFavorites"
            :key="fav.id"
            class="product-item"
            @click="goProduct(fav)"
          >
            <el-image
              :src="(fav as any).product?.image || defaultImage"
              class="product-image"
              fit="cover"
            />
            <div class="product-info">
              <div class="product-name">{{ (fav as any).product?.name }}</div>
              <div class="product-price">
                ¥{{ (fav as any).product?.price }}
              </div>
            </div>
            <el-button type="danger" text @click.stop="handleRemove(fav)">
              取消收藏
            </el-button>
          </div>
          <Empty
            v-if="!loading && productFavorites.length === 0"
            description="暂无收藏商品"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { favoriteApi } from "@/api";
import type { UserFavorite } from "@/types";
import { useUserStore } from "@/stores/user";
import NavBar from "@/components/common/NavBar.vue";
import ShopCard from "@/components/shop/ShopCard.vue";
import Empty from "@/components/common/Empty.vue";

const router = useRouter();
const userStore = useUserStore();

const defaultImage =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCI+PHJlY3Qgd2lkdGg9IjYwIiBoZWlnaHQ9IjYwIiBmaWxsPSIjZWVlIi8+PC9zdmc+";

const activeTab = ref("shop");
const loading = ref(false);
const favorites = ref<UserFavorite[]>([]);

const shopFavorites = computed(() =>
  favorites.value.filter((f) => f.targetType === 1)
);
const productFavorites = computed(() =>
  favorites.value.filter((f) => f.targetType === 2)
);

// 获取收藏列表
const fetchFavorites = async () => {
  if (!userStore.userId) return;

  loading.value = true;
  try {
    if (activeTab.value === "shop") {
      favorites.value = await favoriteApi.getFavoriteShops(userStore.userId);
    } else {
      favorites.value = await favoriteApi.getFavoriteProducts(userStore.userId);
    }
  } catch (error) {
    console.error("获取收藏失败:", error);
  } finally {
    loading.value = false;
  }
};

// 去商品页面
const goProduct = (fav: UserFavorite) => {
  router.push(`/shop/${(fav as any).product?.shopId}`);
};

// 取消收藏
const handleRemove = async (fav: UserFavorite) => {
  try {
    await favoriteApi.removeFavorite(fav.id);
    ElMessage.success("已取消收藏");
    fetchFavorites();
  } catch (error) {
    ElMessage.error("操作失败");
  }
};

watch(activeTab, () => {
  fetchFavorites();
});

onMounted(() => {
  fetchFavorites();
});
</script>

<style lang="scss" scoped>
.favorites-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.favorites-tabs {
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

.favorites-list {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .product-item {
    display: flex;
    align-items: center;
    padding: 12px;
    background-color: #fff;
    border-radius: 8px;
    cursor: pointer;

    .product-image {
      width: 60px;
      height: 60px;
      border-radius: 6px;
      flex-shrink: 0;
    }

    .product-info {
      flex: 1;
      margin-left: 12px;

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
