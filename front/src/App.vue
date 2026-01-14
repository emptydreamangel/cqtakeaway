<template>
  <div id="app-container">
    <router-view v-slot="{ Component }">
      <keep-alive :include="cachedViews">
        <component :is="Component" />
      </keep-alive>
    </router-view>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useAppStore } from "@/stores/app";

const appStore = useAppStore();
const cachedViews = computed(() => appStore.cachedViews);
</script>

<style lang="scss">
#app-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f5f5;
}
</style>
