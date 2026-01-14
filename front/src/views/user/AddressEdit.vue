<template>
  <div class="address-edit-page">
    <NavBar :title="isEdit ? '编辑地址' : '新增地址'" />

    <div class="address-form">
      <el-form
        ref="formRef"
        :model="form"
        :rules="addressRules"
        label-position="top"
        size="large"
      >
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
        </el-form-item>

        <el-form-item label="手机号" prop="contactPhone">
          <el-input
            v-model="form.contactPhone"
            placeholder="请输入手机号"
            maxlength="11"
          />
        </el-form-item>

        <el-form-item label="所在地区" prop="province">
          <div class="region-select">
            <el-select
              v-model="form.province"
              placeholder="省份"
              @change="handleProvinceChange"
            >
              <el-option
                v-for="p in provinces"
                :key="p"
                :label="p"
                :value="p"
              />
            </el-select>
            <el-select
              v-model="form.city"
              placeholder="城市"
              @change="handleCityChange"
            >
              <el-option v-for="c in cities" :key="c" :label="c" :value="c" />
            </el-select>
            <el-select v-model="form.district" placeholder="区县">
              <el-option
                v-for="d in districts"
                :key="d"
                :label="d"
                :value="d"
              />
            </el-select>
          </div>
        </el-form-item>

        <el-form-item label="详细地址" prop="detailAddress">
          <el-input
            v-model="form.detailAddress"
            type="textarea"
            :rows="2"
            placeholder="请输入详细地址，如楼栋、门牌号等"
          />
        </el-form-item>

        <el-form-item>
          <el-checkbox v-model="form.isDefault">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>
    </div>

    <div class="form-footer">
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        保存
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, type FormInstance } from "element-plus";
import { addressApi } from "@/api";
import { useUserStore } from "@/stores/user";
import { addressRules } from "@/utils/validate";
import NavBar from "@/components/common/NavBar.vue";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const addressId = computed(() =>
  route.params.id ? Number(route.params.id) : null
);
const isEdit = computed(() => !!addressId.value);

const formRef = ref<FormInstance>();
const submitting = ref(false);

const form = reactive({
  contactName: "",
  contactPhone: "",
  province: "",
  city: "",
  district: "",
  detailAddress: "",
  isDefault: false,
});

// 模拟省市区数据
const provinces = [
  "北京市",
  "上海市",
  "重庆市",
  "广东省",
  "四川省",
  "浙江省",
  "江苏省",
];
const cityMap: Record<string, string[]> = {
  北京市: ["北京市"],
  上海市: ["上海市"],
  重庆市: ["重庆市"],
  广东省: ["广州市", "深圳市", "东莞市", "佛山市"],
  四川省: ["成都市", "绵阳市", "德阳市", "南充市"],
  浙江省: ["杭州市", "宁波市", "温州市", "嘉兴市"],
  江苏省: ["南京市", "苏州市", "无锡市", "常州市"],
};
const districtMap: Record<string, string[]> = {
  北京市: ["东城区", "西城区", "朝阳区", "海淀区", "丰台区"],
  上海市: ["黄浦区", "徐汇区", "静安区", "浦东新区", "虹口区"],
  重庆市: ["渝中区", "江北区", "南岸区", "渝北区", "沙坪坝区"],
  广州市: ["天河区", "越秀区", "海珠区", "白云区", "番禺区"],
  深圳市: ["南山区", "福田区", "罗湖区", "宝安区", "龙岗区"],
  成都市: ["锦江区", "青羊区", "金牛区", "武侯区", "成华区"],
  杭州市: ["上城区", "下城区", "西湖区", "拱墅区", "滨江区"],
};

const cities = ref<string[]>([]);
const districts = ref<string[]>([]);

const handleProvinceChange = (val: string) => {
  form.city = "";
  form.district = "";
  cities.value = cityMap[val] || [];
  districts.value = [];
};

const handleCityChange = (val: string) => {
  form.district = "";
  districts.value = districtMap[val] || [];
};

// 获取地址详情
const fetchAddress = async () => {
  if (!addressId.value) return;

  try {
    const addr = await addressApi.getAddressById(addressId.value);
    form.contactName = addr.contactName;
    form.contactPhone = addr.contactPhone;
    form.province = addr.province;
    form.city = addr.city;
    form.district = addr.district;
    form.detailAddress = addr.detailAddress;
    form.isDefault = addr.isDefault === 1;

    // 初始化城市和区县选项
    cities.value = cityMap[form.province] || [];
    districts.value = districtMap[form.city] || [];
  } catch (error) {
    console.error("获取地址失败:", error);
    ElMessage.error("获取地址失败");
  }
};

// 保存
const handleSubmit = async () => {
  if (!formRef.value || !userStore.userId) return;

  await formRef.value.validate(async (valid) => {
    if (!valid) return;

    submitting.value = true;
    try {
      const data = {
        userId: userStore.userId!,
        contactName: form.contactName,
        contactPhone: form.contactPhone,
        province: form.province,
        city: form.city,
        district: form.district,
        detailAddress: form.detailAddress,
        isDefault: form.isDefault ? 1 : 0,
      };

      if (isEdit.value) {
        await addressApi.updateAddress(addressId.value!, data);
      } else {
        await addressApi.addAddress(data);
      }

      ElMessage.success("保存成功");
      router.back();
    } catch (error: any) {
      ElMessage.error(error?.response?.data?.message || "保存失败");
    } finally {
      submitting.value = false;
    }
  });
};

onMounted(() => {
  if (isEdit.value) {
    fetchAddress();
  }
});
</script>

<style lang="scss" scoped>
.address-edit-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 80px;
}

.address-form {
  background-color: #fff;
  padding: 16px;
  margin: 12px;
  border-radius: 12px;

  :deep(.el-form-item__label) {
    font-weight: 500;
  }

  .region-select {
    display: flex;
    gap: 8px;

    .el-select {
      flex: 1;
    }
  }
}

.form-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background-color: #fff;
  border-top: 1px solid #eee;

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
