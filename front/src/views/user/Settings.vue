<template>
  <div class="settings-page">
    <NavBar title="设置" />

    <div class="settings-list">
      <div class="settings-group">
        <div class="settings-item" @click="handleChangePassword">
          <span class="item-label">修改密码</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="settings-item">
          <span class="item-label">消息通知</span>
          <el-switch v-model="notificationEnabled" />
        </div>
      </div>

      <div class="settings-group">
        <div class="settings-item" @click="handleClearCache">
          <span class="item-label">清除缓存</span>
          <span class="item-value">{{ cacheSize }}</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="settings-item">
          <span class="item-label">当前版本</span>
          <span class="item-value">1.0.0</span>
        </div>
      </div>

      <div class="settings-group">
        <div class="settings-item" @click="handleAbout">
          <span class="item-label">关于我们</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="settings-item" @click="handlePrivacy">
          <span class="item-label">隐私政策</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="settings-item" @click="handleAgreement">
          <span class="item-label">用户协议</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="showPasswordDialog" title="修改密码" width="90%">
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-position="top"
      >
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button
          type="primary"
          :loading="updating"
          @click="handleSubmitPassword"
          >确定</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import {
  ElMessage,
  ElMessageBox,
  type FormInstance,
  type FormRules,
} from "element-plus";
import { ArrowRight } from "@element-plus/icons-vue";
import { userApi } from "@/api";
import { useUserStore } from "@/stores/user";
import NavBar from "@/components/common/NavBar.vue";

const userStore = useUserStore();

const notificationEnabled = ref(true);
const cacheSize = ref("2.3MB");
const showPasswordDialog = ref(false);
const updating = ref(false);
const passwordFormRef = ref<FormInstance>();

const passwordForm = reactive({
  newPassword: "",
  confirmPassword: "",
});

const validateConfirmPassword = (_rule: any, value: string, callback: any) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const passwordRules: FormRules = {
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度为6-20位", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请确认密码", trigger: "blur" },
    { validator: validateConfirmPassword, trigger: "blur" },
  ],
};

// 修改密码
const handleChangePassword = () => {
  passwordForm.newPassword = "";
  passwordForm.confirmPassword = "";
  showPasswordDialog.value = true;
};

// 提交修改密码
const handleSubmitPassword = async () => {
  if (!passwordFormRef.value || !userStore.userId) return;

  await passwordFormRef.value.validate(async (valid) => {
    if (!valid) return;

    updating.value = true;
    try {
      await userApi.updatePassword(userStore.userId!, passwordForm.newPassword);
      ElMessage.success("密码修改成功");
      showPasswordDialog.value = false;
    } catch (error: any) {
      ElMessage.error(error?.response?.data?.message || "修改失败");
    } finally {
      updating.value = false;
    }
  });
};

// 清除缓存
const handleClearCache = async () => {
  try {
    await ElMessageBox.confirm("确定要清除缓存吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
    });

    // 清除本地存储（保留登录信息）
    const token = localStorage.getItem("cq_takeaway_token");
    const user = localStorage.getItem("cq_takeaway_user");
    localStorage.clear();
    if (token) localStorage.setItem("cq_takeaway_token", token);
    if (user) localStorage.setItem("cq_takeaway_user", user);

    cacheSize.value = "0KB";
    ElMessage.success("缓存已清除");
  } catch {
    // 取消
  }
};

// 关于我们
const handleAbout = () => {
  ElMessageBox.alert(
    "积天外卖 v1.0.0\n\n一款便捷的外卖点餐应用，为您提供优质的餐饮服务。",
    "关于我们",
    { confirmButtonText: "确定" }
  );
};

// 隐私政策
const handlePrivacy = () => {
  ElMessage.info("隐私政策页面开发中");
};

// 用户协议
const handleAgreement = () => {
  ElMessage.info("用户协议页面开发中");
};
</script>

<style lang="scss" scoped>
.settings-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.settings-list {
  padding: 12px;

  .settings-group {
    background-color: #fff;
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 12px;

    .settings-item {
      display: flex;
      align-items: center;
      padding: 16px;
      border-bottom: 1px solid #f5f5f5;
      cursor: pointer;

      &:last-child {
        border-bottom: none;
      }

      .item-label {
        flex: 1;
        font-size: 14px;
        color: #333;
      }

      .item-value {
        font-size: 14px;
        color: #999;
        margin-right: 8px;
      }

      .el-icon {
        font-size: 16px;
        color: #999;
      }
    }
  }
}

:deep(.el-dialog) {
  .el-button--primary {
    background-color: #ff6b00;
    border-color: #ff6b00;
  }
}
</style>
