<template>
  <div class="layout">
    <main class="layout-main">
      <router-view />
    </main>
    <TabBar />
  </div>
</template>

<script setup lang="ts">
import TabBar from "@/components/common/TabBar.vue";
</script>

<style lang="scss" scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;

  &-main {
    flex: 1;
    padding-bottom: 60px;
    overflow-y: auto;
  }
}
</style>
