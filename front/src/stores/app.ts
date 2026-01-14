import { defineStore } from "pinia";
import { ref } from "vue";

export const useAppStore = defineStore("app", () => {
  // 全局loading状态
  const loading = ref(false);

  // 缓存的视图
  const cachedViews = ref<string[]>(["Home"]);

  // 当前位置信息
  const location = ref({
    province: "",
    city: "",
    district: "",
    address: "",
  });

  // 设置loading
  function setLoading(value: boolean) {
    loading.value = value;
  }

  // 添加缓存视图
  function addCachedView(viewName: string) {
    if (!cachedViews.value.includes(viewName)) {
      cachedViews.value.push(viewName);
    }
  }

  // 移除缓存视图
  function removeCachedView(viewName: string) {
    const index = cachedViews.value.indexOf(viewName);
    if (index > -1) {
      cachedViews.value.splice(index, 1);
    }
  }

  // 清空缓存视图
  function clearCachedViews() {
    cachedViews.value = [];
  }

  // 设置位置信息
  function setLocation(locationInfo: typeof location.value) {
    location.value = locationInfo;
  }

  return {
    loading,
    cachedViews,
    location,
    setLoading,
    addCachedView,
    removeCachedView,
    clearCachedViews,
    setLocation,
  };
});
