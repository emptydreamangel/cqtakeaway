<template>
  <div class="empty">
    <el-empty :image-size="120" :description="description">
      <template v-if="showButton">
        <el-button type="primary" @click="$emit('action')">{{
          buttonText
        }}</el-button>
      </template>
    </el-empty>
  </div>
</template>

<script setup lang="ts">
interface Props {
  description?: string;
  showButton?: boolean;
  buttonText?: string;
}

withDefaults(defineProps<Props>(), {
  description: "暂无数据",
  showButton: false,
  buttonText: "去看看",
});

defineEmits(["action"]);
</script>

<style lang="scss" scoped>
.empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  background-color: #fff;
}
</style>
