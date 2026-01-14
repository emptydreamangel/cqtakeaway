import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type { User } from "@/types";
import {
  setToken,
  removeToken,
  setUserInfo,
  removeUserInfo,
} from "@/utils/storage";
import { userApi } from "@/api/user";
import router from "@/router";

export const useUserStore = defineStore(
  "user",
  () => {
    // 状态
    const user = ref<User | null>(null);
    const token = ref<string>("");

    // 计算属性
    const isLoggedIn = computed(() => !!token.value && !!user.value);
    const userId = computed(() => user.value?.id);
    const nickname = computed(() => user.value?.nickname || "未登录");
    const avatar = computed(() => user.value?.avatar || "");

    // 登录
    async function login(phone: string, password: string) {
      const res = await userApi.login({ phone, password });
      // 假设后端返回用户信息，实际可能返回token
      if (res) {
        user.value = res;
        // 使用用户ID作为简单token（实际应用中应使用后端返回的JWT token）
        token.value = String(res.id);
        setToken(token.value);
        setUserInfo(res);
      }
      return res;
    }

    // 注册
    async function register(params: {
      phone: string;
      password: string;
      nickname: string;
    }) {
      const res = await userApi.register(params);
      return res;
    }

    // 获取用户信息
    async function getUserInfo() {
      if (!userId.value) return null;
      const res = await userApi.getUserById(userId.value);
      if (res) {
        user.value = res;
        setUserInfo(res);
      }
      return res;
    }

    // 更新用户信息
    async function updateUserInfo(data: Partial<User>) {
      if (!userId.value) return null;
      const res = await userApi.updateUser(userId.value, data);
      if (res) {
        user.value = { ...user.value, ...data } as User;
        setUserInfo(user.value);
      }
      return res;
    }

    // 退出登录
    function logout() {
      user.value = null;
      token.value = "";
      removeToken();
      removeUserInfo();
      router.push("/login");
    }

    // 设置用户信息（从存储恢复）
    function setUser(userData: User, userToken: string) {
      user.value = userData;
      token.value = userToken;
    }

    return {
      user,
      token,
      isLoggedIn,
      userId,
      nickname,
      avatar,
      login,
      register,
      getUserInfo,
      updateUserInfo,
      logout,
      setUser,
    };
  },
  {
    persist: {
      key: "user-store",
      paths: ["user", "token"],
    },
  }
);
