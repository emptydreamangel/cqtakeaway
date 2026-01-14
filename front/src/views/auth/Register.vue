<template>
  <div class="register-page">
    <NavBar title="注册" />

    <div class="register-form">
      <el-form
        ref="formRef"
        :model="form"
        :rules="registerRules"
        size="large"
        label-position="top"
      >
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="form.phone"
            placeholder="请输入手机号"
            maxlength="11"
          />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input
            v-model="form.nickname"
            placeholder="请输入昵称"
            maxlength="20"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码（6-20位，包含字母和数字）"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="submit-btn"
            :loading="loading"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>

      <div class="register-footer">
        <span>已有账号？</span>
        <router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, type FormInstance, type FormRules } from "element-plus";
import NavBar from "@/components/common/NavBar.vue";
import { useUserStore } from "@/stores/user";
import { registerRules as baseRules } from "@/utils/validate";

const router = useRouter();
const userStore = useUserStore();

const formRef = ref<FormInstance>();
const loading = ref(false);

const form = reactive({
  phone: "",
  nickname: "",
  password: "",
  confirmPassword: "",
});

// 验证确认密码
const validateConfirmPassword = (_rule: any, value: string, callback: any) => {
  if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const registerRules: FormRules = {
  ...baseRules,
  confirmPassword: [
    { required: true, message: "请再次输入密码", trigger: "blur" },
    { validator: validateConfirmPassword, trigger: "blur" },
  ],
};

const handleRegister = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (!valid) return;

    loading.value = true;
    try {
      await userStore.register({
        phone: form.phone,
        nickname: form.nickname,
        password: form.password,
      });
      ElMessage.success("注册成功，请登录");
      router.push("/login");
    } catch (error: any) {
      ElMessage.error(error?.response?.data?.message || "注册失败");
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style lang="scss" scoped>
.register-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.register-form {
  background-color: #fff;
  margin: 16px;
  border-radius: 12px;
  padding: 24px;

  :deep(.el-form-item__label) {
    font-weight: 500;
  }

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
    margin-top: 16px;

    &:hover {
      background-color: #ff8533;
      border-color: #ff8533;
    }
  }
}

.register-footer {
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
