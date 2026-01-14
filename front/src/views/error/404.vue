<template>
  <div class="not-found-page">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <div class="error-message">页面不存在</div>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";

const router = useRouter();

const goHome = () => {
  router.push("/home");
};
</script>

<style lang="scss" scoped>
.not-found-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}

.not-found-content {
  text-align: center;

  .error-code {
    font-size: 80px;
    font-weight: 600;
    color: #ff6b00;
    margin-bottom: 16px;
  }

  .error-message {
    font-size: 18px;
    color: #666;
    margin-bottom: 32px;
  }

  .el-button {
    background-color: #ff6b00;
    border-color: #ff6b00;
  }
}
</style>
