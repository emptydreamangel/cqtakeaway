import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from "vue-router";
import { useUserStore } from "@/stores/user";

// 布局组件
const Layout = () => import("@/views/layout/index.vue");

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: "/",
    component: Layout,
    redirect: "/home",
    children: [
      {
        path: "home",
        name: "Home",
        component: () => import("@/views/home/index.vue"),
        meta: { title: "首页", keepAlive: true },
      },
      {
        path: "orders",
        name: "Orders",
        component: () => import("@/views/order/List.vue"),
        meta: { title: "订单", requireAuth: true },
      },
      {
        path: "user",
        name: "User",
        component: () => import("@/views/user/Profile.vue"),
        meta: { title: "我的", requireAuth: true },
      },
    ],
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/auth/Login.vue"),
    meta: { title: "登录" },
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("@/views/auth/Register.vue"),
    meta: { title: "注册" },
  },
  {
    path: "/shop",
    name: "ShopList",
    component: () => import("@/views/shop/List.vue"),
    meta: { title: "商家列表" },
  },
  {
    path: "/shop/:id",
    name: "ShopDetail",
    component: () => import("@/views/shop/Detail.vue"),
    meta: { title: "商家详情" },
  },
  {
    path: "/search",
    name: "Search",
    component: () => import("@/views/shop/Search.vue"),
    meta: { title: "搜索" },
  },
  {
    path: "/cart",
    name: "Cart",
    component: () => import("@/views/cart/index.vue"),
    meta: { title: "购物车", requireAuth: true },
  },
  {
    path: "/order/confirm",
    name: "OrderConfirm",
    component: () => import("@/views/order/Confirm.vue"),
    meta: { title: "确认订单", requireAuth: true },
  },
  {
    path: "/order/:id",
    name: "OrderDetail",
    component: () => import("@/views/order/Detail.vue"),
    meta: { title: "订单详情", requireAuth: true },
  },
  {
    path: "/pay/:orderId",
    name: "Pay",
    component: () => import("@/views/order/Pay.vue"),
    meta: { title: "支付订单", requireAuth: true },
  },
  {
    path: "/address",
    name: "Address",
    component: () => import("@/views/user/Address.vue"),
    meta: { title: "地址管理", requireAuth: true },
  },
  {
    path: "/address/edit/:id?",
    name: "AddressEdit",
    component: () => import("@/views/user/AddressEdit.vue"),
    meta: { title: "编辑地址", requireAuth: true },
  },
  {
    path: "/favorites",
    name: "Favorites",
    component: () => import("@/views/user/Favorites.vue"),
    meta: { title: "我的收藏", requireAuth: true },
  },
  {
    path: "/coupons",
    name: "Coupons",
    component: () => import("@/views/user/Coupons.vue"),
    meta: { title: "我的优惠券", requireAuth: true },
  },
  {
    path: "/settings",
    name: "Settings",
    component: () => import("@/views/user/Settings.vue"),
    meta: { title: "设置", requireAuth: true },
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("@/views/error/404.vue"),
    meta: { title: "页面不存在" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(_to, _from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    }
    return { top: 0 };
  },
});

// 路由守卫
router.beforeEach((to, _from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title || "积天外卖"} - 积天外卖`;

  // 检查是否需要登录
  if (to.meta.requireAuth) {
    const userStore = useUserStore();
    if (!userStore.isLoggedIn) {
      next({
        path: "/login",
        query: { redirect: to.fullPath },
      });
      return;
    }
  }

  next();
});

export default router;
