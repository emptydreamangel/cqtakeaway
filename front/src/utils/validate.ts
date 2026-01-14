import type { FormRules } from "element-plus";

// 手机号验证
export const phoneRegex = /^1[3-9]\d{9}$/;

export function validatePhone(phone: string): boolean {
  return phoneRegex.test(phone);
}

// 密码验证（6-20位，包含字母和数字）
export const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,20}$/;

export function validatePassword(password: string): boolean {
  return passwordRegex.test(password);
}

// 表单验证规则
export const loginRules: FormRules = {
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: phoneRegex, message: "请输入正确的手机号", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度为6-20位", trigger: "blur" },
  ],
};

export const registerRules: FormRules = {
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: phoneRegex, message: "请输入正确的手机号", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      pattern: passwordRegex,
      message: "密码需要6-20位，包含字母和数字",
      trigger: "blur",
    },
  ],
  nickname: [
    { required: true, message: "请输入昵称", trigger: "blur" },
    { min: 2, max: 20, message: "昵称长度为2-20位", trigger: "blur" },
  ],
};

export const addressRules: FormRules = {
  contactName: [
    { required: true, message: "请输入联系人姓名", trigger: "blur" },
    { min: 2, max: 20, message: "姓名长度为2-20位", trigger: "blur" },
  ],
  contactPhone: [
    { required: true, message: "请输入联系电话", trigger: "blur" },
    { pattern: phoneRegex, message: "请输入正确的手机号", trigger: "blur" },
  ],
  province: [{ required: true, message: "请选择省份", trigger: "change" }],
  city: [{ required: true, message: "请选择城市", trigger: "change" }],
  district: [{ required: true, message: "请选择区县", trigger: "change" }],
  detailAddress: [
    { required: true, message: "请输入详细地址", trigger: "blur" },
    { min: 5, max: 100, message: "详细地址长度为5-100位", trigger: "blur" },
  ],
};
