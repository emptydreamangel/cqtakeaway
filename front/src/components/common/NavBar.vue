<template>
  <div class="navbar">
    <div class="navbar-left" @click="handleBack">
      <el-icon v-if="showBack"><ArrowLeft /></el-icon>
    </div>
    <div class="navbar-title">{{ title }}</div>
    <div class="navbar-right">
      <slot name="right"></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ArrowLeft } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

interface Props {
  title?: string;
  showBack?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  title: "",
  showBack: true,
});

const router = useRouter();

const handleBack = () => {
  if (props.showBack) {
    router.back();
  }
};
</script>

<style lang="scss" scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 44px;
  padding: 0 12px;
  background-color: #fff;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 100;

  &-left {
    width: 44px;
    display: flex;
    align-items: center;
    cursor: pointer;

    .el-icon {
      font-size: 20px;
      color: #333;
    }
  }

  &-title {
    flex: 1;
    text-align: center;
    font-size: 16px;
    font-weight: 500;
    color: #333;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &-right {
    width: 44px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }
}
</style>
