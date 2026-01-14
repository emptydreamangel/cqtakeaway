import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type { CartItem } from "@/types";
import { cartApi } from "@/api/cart";
import { useUserStore } from "./user";

export const useCartStore = defineStore("cart", () => {
  // 状态
  const cartItems = ref<CartItem[]>([]);
  const loading = ref(false);

  // 当前选中的商家ID
  const currentShopId = ref<number | null>(null);

  // 计算属性
  const totalCount = computed(() => {
    return cartItems.value.reduce((sum, item) => sum + item.quantity, 0);
  });

  const selectedItems = computed(() => {
    return cartItems.value.filter((item) => item.selected === 1);
  });

  const selectedCount = computed(() => {
    return selectedItems.value.reduce((sum, item) => sum + item.quantity, 0);
  });

  const totalPrice = computed(() => {
    return selectedItems.value.reduce((sum, item) => {
      const price = item.spec?.price || item.product?.price || 0;
      return sum + price * item.quantity;
    }, 0);
  });

  const isAllSelected = computed(() => {
    if (cartItems.value.length === 0) return false;
    return cartItems.value.every((item) => item.selected === 1);
  });

  // 获取购物车列表
  async function fetchCart() {
    const userStore = useUserStore();
    if (!userStore.userId) return;

    loading.value = true;
    try {
      const res = await cartApi.getCart(userStore.userId);
      cartItems.value = res || [];
    } finally {
      loading.value = false;
    }
  }

  // 获取指定商家的购物车
  async function fetchCartByShop(shopId: number) {
    const userStore = useUserStore();
    if (!userStore.userId) return;

    loading.value = true;
    try {
      const res = await cartApi.getCartByShop(userStore.userId, shopId);
      cartItems.value = res || [];
      currentShopId.value = shopId;
    } finally {
      loading.value = false;
    }
  }

  // 添加到购物车
  async function addToCart(params: {
    shopId: number;
    productId: number;
    specId?: number;
    quantity: number;
  }) {
    const userStore = useUserStore();
    if (!userStore.userId) return;

    const res = await cartApi.addToCart({
      userId: userStore.userId,
      ...params,
    });

    // 重新获取购物车
    if (currentShopId.value) {
      await fetchCartByShop(currentShopId.value);
    } else {
      await fetchCart();
    }

    return res;
  }

  // 更新购物车数量
  async function updateQuantity(id: number, quantity: number) {
    if (quantity <= 0) {
      return removeItem(id);
    }

    await cartApi.updateQuantity(id, { quantity });

    const item = cartItems.value.find((i) => i.id === id);
    if (item) {
      item.quantity = quantity;
    }
  }

  // 删除购物车项
  async function removeItem(id: number) {
    await cartApi.removeFromCart(id);
    cartItems.value = cartItems.value.filter((item) => item.id !== id);
  }

  // 清空购物车
  async function clearCart() {
    const userStore = useUserStore();
    if (!userStore.userId) return;

    await cartApi.clearCart(userStore.userId);
    cartItems.value = [];
  }

  // 切换选中状态（本地操作）
  function toggleSelect(id: number) {
    const item = cartItems.value.find((i) => i.id === id);
    if (item) {
      item.selected = item.selected === 1 ? 0 : 1;
    }
  }

  // 全选/取消全选（本地操作）
  function toggleSelectAll() {
    const newSelected = isAllSelected.value ? 0 : 1;
    cartItems.value.forEach((item) => {
      item.selected = newSelected;
    });
  }

  // 重置购物车状态
  function resetCart() {
    cartItems.value = [];
    currentShopId.value = null;
  }

  return {
    cartItems,
    loading,
    currentShopId,
    totalCount,
    selectedItems,
    selectedCount,
    totalPrice,
    isAllSelected,
    fetchCart,
    fetchCartByShop,
    addToCart,
    updateQuantity,
    removeItem,
    clearCart,
    toggleSelect,
    toggleSelectAll,
    resetCart,
  };
});
