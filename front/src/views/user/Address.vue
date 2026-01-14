<template>
  <div class="address-page">
    <NavBar title="收货地址" />

    <div class="address-list" v-loading="loading">
      <div
        v-for="addr in addresses"
        :key="addr.id"
        class="address-item"
        @click="handleSelect(addr)"
      >
        <div class="address-content">
          <div class="address-header">
            <span class="name">{{ addr.contactName }}</span>
            <span class="phone">{{ addr.contactPhone }}</span>
            <el-tag v-if="addr.isDefault === 1" size="small" type="warning"
              >默认</el-tag
            >
          </div>
          <div class="address-detail">
            {{ addr.province }}{{ addr.city }}{{ addr.district
            }}{{ addr.detailAddress }}
          </div>
        </div>
        <div class="address-actions">
          <el-button text @click.stop="handleEdit(addr)">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button text type="danger" @click.stop="handleDelete(addr)">
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </div>
      </div>

      <Empty
        v-if="!loading && addresses.length === 0"
        description="暂无收货地址"
      />
    </div>

    <div class="add-btn">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增收货地址
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Edit, Delete, Plus } from "@element-plus/icons-vue";
import { addressApi } from "@/api";
import type { UserAddress } from "@/types";
import { useUserStore } from "@/stores/user";
import NavBar from "@/components/common/NavBar.vue";
import Empty from "@/components/common/Empty.vue";

const router = useRouter();
const userStore = useUserStore();

const addresses = ref<UserAddress[]>([]);
const loading = ref(false);

// 获取地址列表
const fetchAddresses = async () => {
  if (!userStore.userId) return;

  loading.value = true;
  try {
    addresses.value = await addressApi.getAddressesByUser(userStore.userId);
  } catch (error) {
    console.error("获取地址失败:", error);
  } finally {
    loading.value = false;
  }
};

// 选择地址（从订单确认页面来的情况）
const handleSelect = (_addr: UserAddress) => {
  // 可以通过事件传递选中的地址
  // 这里暂时只做展示
};

// 添加地址
const handleAdd = () => {
  router.push("/address/edit");
};

// 编辑地址
const handleEdit = (addr: UserAddress) => {
  router.push(`/address/edit/${addr.id}`);
};

// 删除地址
const handleDelete = async (addr: UserAddress) => {
  try {
    await ElMessageBox.confirm("确定要删除该地址吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });

    await addressApi.deleteAddress(addr.id);
    ElMessage.success("删除成功");
    fetchAddresses();
  } catch {
    // 取消
  }
};

onMounted(() => {
  fetchAddresses();
});
</script>

<style lang="scss" scoped>
.address-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 80px;
}

.address-list {
  padding: 12px;

  .address-item {
    background-color: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;

    .address-content {
      .address-header {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 8px;

        .name {
          font-size: 16px;
          font-weight: 500;
          color: #333;
        }

        .phone {
          font-size: 14px;
          color: #666;
        }
      }

      .address-detail {
        font-size: 14px;
        color: #666;
        line-height: 1.4;
      }
    }

    .address-actions {
      display: flex;
      justify-content: flex-end;
      gap: 8px;
      margin-top: 12px;
      padding-top: 12px;
      border-top: 1px solid #f5f5f5;
    }
  }
}

.add-btn {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  max-width: 480px;
  padding: 16px;
  background-color: #fff;
  border-top: 1px solid #eee;

  @media (min-width: 768px) {
    left: 50%;
    right: auto;
    transform: translateX(-50%);
  }

  .el-button {
    width: 100%;
    height: 44px;
    font-size: 14px;
    background-color: #ff6b00;
    border-color: #ff6b00;
    border-radius: 22px;
  }
}
</style>
