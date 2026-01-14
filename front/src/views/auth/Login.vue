<template>
  <div class="login-page">
    <div class="login-header">
      <div class="logo">
        <el-icon><Shop /></el-icon>
      </div>
      <h1 class="title">积天外卖</h1>
      <p class="subtitle">美食，快速送达</p>
    </div>

    <div class="login-form">
      <el-form ref="formRef" :model="form" :rules="loginRules" size="large">
        <el-form-item prop="phone">
          <el-input
            v-model="form.phone"
            placeholder="请输入手机号"
            :prefix-icon="Phone"
            maxlength="11"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="submit-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span>还没有账号？</span>
        <router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage, type FormInstance } from "element-plus";
import { Shop, Phone, Lock } from "@element-plus/icons-vue";
import { useUserStore } from "@/stores/user";
import { loginRules } from "@/utils/validate";

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const formRef = ref<FormInstance>();
const loading = ref(false);

const form = reactive({
  phone: "",
  password: "",
});

const handleLogin = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (!valid) return;

    loading.value = true;
    try {
      await userStore.login(form.phone, form.password);
      ElMessage.success("登录成功");

      // 跳转到之前的页面或首页
      const redirect = route.query.redirect as string;
      router.push(redirect || "/home");
    } catch (error: any) {
      ElMessage.error(
        error?.response?.data?.message || "登录失败，请检查账号密码"
      );
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #ff6b00 0%, #ff8533 100%);
  padding: 60px 24px 24px;
}

.login-header {
  text-align: center;
  margin-bottom: 48px;

  .logo {
    width: 80px;
    height: 80px;
    margin: 0 auto 16px;
    background-color: #fff;
    border-radius: 20px;
    display: flex;
    align-items: center;
    justify-content: center;

    .el-icon {
      font-size: 48px;
      color: #ff6b00;
    }
  }

  .title {
    font-size: 28px;
    font-weight: 600;
    color: #fff;
    margin-bottom: 8px;
  }

  .subtitle {
    font-size: 14px;
    color: rgba(255, 255, 255, 0.8);
  }
}

.login-form {
  background-color: #fff;
  border-radius: 16px;
  padding: 32px 24px;

  :deep(.el-input__wrapper) {
    border-radius: 8px;
  }

  .submit-btn {
    width: 100%;
    height: 48px;
    border-radius: 24px;
    font-size: 16px;
    background-color: #ff6b00;
    border-color: #ff6b00;

    &:hover {
      background-color: #ff8533;
      border-color: #ff8533;
    }
  }
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: #666;

  a {
    color: #ff6b00;
    text-decoration: none;
    margin-left: 4px;

    &:hover {
      text-decoration: underline;
    }
  }
}
</style>
