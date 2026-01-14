<template>
  <div class="tabbar">
    <div
      v-for="item in tabs"
      :key="item.path"
      class="tabbar-item"
      :class="{ active: isActive(item.path) }"
      @click="handleTabClick(item.path)"
    >
      <el-icon class="tabbar-icon">
        <component :is="item.icon" />
      </el-icon>
      <span class="tabbar-text">{{ item.title }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from "vue-router";
import { HomeFilled, Document, User } from "@element-plus/icons-vue";

const router = useRouter();
const route = useRoute();

const tabs = [
  { path: "/home", title: "首页", icon: HomeFilled },
  { path: "/orders", title: "订单", icon: Document },
  { path: "/user", title: "我的", icon: User },
];

const isActive = (path: string) => {
  return route.path === path;
};

const handleTabClick = (path: string) => {
  router.push(path);
};
</script>

<style lang="scss" scoped>
.tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  display: flex;
  background-color: #fff;
  border-top: 1px solid #eee;
  z-index: 1000;

  &-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s;

    &.active {
      .tabbar-icon,
      .tabbar-text {
        color: #ff6b00;
      }
    }
  }

  &-icon {
    font-size: 22px;
    color: #999;
    margin-bottom: 4px;
  }

  &-text {
    font-size: 12px;
    color: #999;
  }
}
</style>
