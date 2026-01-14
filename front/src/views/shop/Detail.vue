<template>
  <div class="shop-detail-page">
    <!-- 商家头部 -->
    <div class="shop-header">
      <div
        class="shop-banner"
        :style="{ backgroundImage: `url(${shop?.banner || defaultBanner})` }"
      >
        <div class="shop-banner-mask"></div>
        <div class="back-btn" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
        </div>
      </div>
      <div class="shop-info">
        <el-image
          :src="shop?.logo || defaultLogo"
          class="shop-logo"
          fit="cover"
        />
        <div class="shop-meta">
          <h1 class="shop-name">{{ shop?.name }}</h1>
          <div class="shop-stats">
            <span class="rating">
              <el-icon><Star /></el-icon>
              {{ shop?.rating?.toFixed(1) || "5.0" }}
            </span>
            <span>月售{{ shop?.monthlySales || 0 }}</span>
            <span>{{ shop?.deliveryTime || 30 }}分钟</span>
          </div>
          <div class="shop-tags">
            <span>起送¥{{ shop?.minPrice || 0 }}</span>
            <span>配送费¥{{ shop?.deliveryFee || 0 }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 商品区域 -->
    <div class="product-section">
      <!-- 左侧分类 -->
      <div class="category-sidebar">
        <div
          v-for="(cat, index) in productCategories"
          :key="cat.id"
          class="category-item"
          :class="{ active: currentCategoryIndex === index }"
          @click="handleCategoryClick(index)"
        >
          {{ cat.name }}
        </div>
      </div>

      <!-- 右侧商品列表 -->
      <div class="product-list" ref="productListRef">
        <div
          v-for="(cat, catIndex) in productCategories"
          :key="cat.id"
          class="category-section"
          :ref="(el) => setCategoryRef(el, catIndex)"
        >
          <div class="category-title">{{ cat.name }}</div>
          <ProductCard
            v-for="product in getProductsByCategory(cat.id)"
            :key="product.id"
            :product="product"
            @add="handleAddToCart"
          />
        </div>
        <Empty v-if="products.length === 0" description="暂无商品" />
      </div>
    </div>

    <!-- 购物车栏 -->
    <CartBar
      :min-price="shop?.minPrice || 0"
      :delivery-fee="shop?.deliveryFee || 0"
      @cart-click="showCartPopup = true"
    />

    <!-- 购物车弹窗 -->
    <el-drawer
      v-model="showCartPopup"
      title="购物车"
      direction="btt"
      size="60%"
    >
      <div class="cart-popup">
        <div v-if="cartStore.cartItems.length > 0" class="cart-list">
          <div
            v-for="item in cartStore.cartItems"
            :key="item.id"
            class="cart-item"
          >
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
        <Empty v-else description="购物车是空的" />
        <div v-if="cartStore.cartItems.length > 0" class="cart-footer">
          <el-button type="danger" text @click="handleClearCart"
            >清空购物车</el-button
          >
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { ArrowLeft, Star, Plus, Minus } from "@element-plus/icons-vue";
import { shopApi, productApi, productCategoryApi } from "@/api";
import type { Shop, Product, ProductCategory } from "@/types";
import { useCartStore } from "@/stores/cart";
import { useUserStore } from "@/stores/user";
import ProductCard from "@/components/product/ProductCard.vue";
import CartBar from "@/components/cart/CartBar.vue";
import Empty from "@/components/common/Empty.vue";

const route = useRoute();
const router = useRouter();
const cartStore = useCartStore();
const userStore = useUserStore();

const shopId = computed(() => Number(route.params.id));

const shop = ref<Shop | null>(null);
const productCategories = ref<ProductCategory[]>([]);
const products = ref<Product[]>([]);
const currentCategoryIndex = ref(0);
const showCartPopup = ref(false);
const categoryRefs = ref<(Element | null)[]>([]);

const defaultBanner =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI0MDAiIGhlaWdodD0iMTUwIj48cmVjdCB3aWR0aD0iNDAwIiBoZWlnaHQ9IjE1MCIgZmlsbD0iI2ZmNmIwMCIvPjwvc3ZnPg==";
const defaultLogo =
  "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCI+PHJlY3Qgd2lkdGg9IjYwIiBoZWlnaHQ9IjYwIiBmaWxsPSIjZWVlIi8+PC9zdmc+";

// 设置分类ref
const setCategoryRef = (el: any, index: number) => {
  categoryRefs.value[index] = el;
};

// 获取分类下的商品
const getProductsByCategory = (categoryId: number) => {
  return products.value.filter((p) => p.categoryId === categoryId);
};

// 点击分类
const handleCategoryClick = (index: number) => {
  currentCategoryIndex.value = index;
  const el = categoryRefs.value[index];
  if (el) {
    el.scrollIntoView({ behavior: "smooth", block: "start" });
  }
};

// 添加到购物车
const handleAddToCart = async (product: Product) => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning("请先登录");
    router.push("/login");
    return;
  }

  try {
    await cartStore.addToCart({
      shopId: shopId.value,
      productId: product.id,
      quantity: 1,
    });
    ElMessage.success("已添加到购物车");
  } catch (error) {
    ElMessage.error("添加失败");
  }
};

// 修改购物车数量
const handleQuantityChange = async (id: number, quantity: number) => {
  try {
    await cartStore.updateQuantity(id, quantity);
  } catch (error) {
    ElMessage.error("操作失败");
  }
};

// 清空购物车
const handleClearCart = async () => {
  try {
    await ElMessageBox.confirm("确定要清空购物车吗？", "提示", {
      type: "warning",
    });
    await cartStore.clearCart();
    showCartPopup.value = false;
    ElMessage.success("已清空");
  } catch {
    // 取消
  }
};

// 返回
const goBack = () => {
  router.back();
};

// 获取商家信息
const fetchShopInfo = async () => {
  try {
    shop.value = await shopApi.getShopById(shopId.value);
  } catch (error) {
    console.error("获取商家信息失败:", error);
  }
};

// 获取商品分类
const fetchCategories = async () => {
  try {
    productCategories.value = await productCategoryApi.getCategoriesByShop(
      shopId.value
    );
  } catch (error) {
    console.error("获取商品分类失败:", error);
  }
};

// 获取商品列表
const fetchProducts = async () => {
  try {
    products.value = await productApi.getProductsByShop(shopId.value);
  } catch (error) {
    console.error("获取商品失败:", error);
  }
};

onMounted(async () => {
  await Promise.all([fetchShopInfo(), fetchCategories(), fetchProducts()]);

  // 获取购物车
  if (userStore.isLoggedIn) {
    cartStore.fetchCartByShop(shopId.value);
  }
});
</script>

<style lang="scss" scoped>
.shop-detail-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 60px;
}

.shop-header {
  .shop-banner {
    height: 150px;
    background-size: cover;
    background-position: center;
    position: relative;

    .shop-banner-mask {
      position: absolute;
      inset: 0;
      background: linear-gradient(
        180deg,
        rgba(0, 0, 0, 0.3) 0%,
        rgba(0, 0, 0, 0) 100%
      );
    }

    .back-btn {
      position: absolute;
      top: 12px;
      left: 12px;
      width: 36px;
      height: 36px;
      background-color: rgba(0, 0, 0, 0.3);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;

      .el-icon {
        font-size: 20px;
        color: #fff;
      }
    }
  }

  .shop-info {
    display: flex;
    padding: 0 12px 16px;
    margin-top: -30px;
    position: relative;
    background-color: #fff;

    .shop-logo {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      flex-shrink: 0;
      border: 2px solid #fff;
    }

    .shop-meta {
      flex: 1;
      margin-left: 12px;
      padding-top: 6px;

      .shop-name {
        font-size: 18px;
        font-weight: 600;
        color: #333;
        margin-bottom: 8px;
      }

      .shop-stats {
        display: flex;
        gap: 12px;
        font-size: 12px;
        color: #666;
        margin-bottom: 6px;

        .rating {
          display: flex;
          align-items: center;
          gap: 2px;
          color: #ff6b00;
        }
      }

      .shop-tags {
        display: flex;
        gap: 8px;
        font-size: 12px;
        color: #999;
      }
    }
  }
}

.product-section {
  display: flex;
  background-color: #fff;
  margin-top: 12px;

  .category-sidebar {
    width: 80px;
    flex-shrink: 0;
    background-color: #f5f5f5;
    max-height: calc(100vh - 280px);
    overflow-y: auto;

    .category-item {
      padding: 16px 8px;
      font-size: 13px;
      color: #666;
      text-align: center;
      cursor: pointer;
      border-left: 3px solid transparent;

      &.active {
        background-color: #fff;
        color: #ff6b00;
        font-weight: 500;
        border-left-color: #ff6b00;
      }
    }
  }

  .product-list {
    flex: 1;
    max-height: calc(100vh - 280px);
    overflow-y: auto;

    .category-section {
      .category-title {
        padding: 12px;
        font-size: 14px;
        font-weight: 500;
        color: #333;
        background-color: #fafafa;
        position: sticky;
        top: 0;
        z-index: 1;
      }
    }
  }
}

.cart-popup {
  height: 100%;
  display: flex;
  flex-direction: column;

  .cart-list {
    flex: 1;
    overflow-y: auto;
    padding: 0 16px;

    .cart-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 12px 0;
      border-bottom: 1px solid #f5f5f5;

      .item-info {
        .item-name {
          font-size: 14px;
          color: #333;
        }

        .item-spec {
          font-size: 12px;
          color: #999;
          margin-top: 4px;
        }

        .item-price {
          font-size: 14px;
          color: #ff6b00;
          margin-top: 4px;
        }
      }

      .item-actions {
        display: flex;
        align-items: center;
        gap: 12px;

        .quantity {
          font-size: 14px;
          color: #333;
          min-width: 24px;
          text-align: center;
        }
      }
    }
  }

  .cart-footer {
    padding: 12px 16px;
    border-top: 1px solid #f5f5f5;
    text-align: right;
  }
}
</style>
