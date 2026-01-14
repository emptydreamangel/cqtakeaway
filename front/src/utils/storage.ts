const TOKEN_KEY = "cq_takeaway_token";
const USER_KEY = "cq_takeaway_user";

// Token管理
export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY);
}

export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token);
}

export function removeToken(): void {
  localStorage.removeItem(TOKEN_KEY);
}

// 用户信息管理
export function getUserInfo<T>(): T | null {
  const userStr = localStorage.getItem(USER_KEY);
  if (userStr) {
    try {
      return JSON.parse(userStr) as T;
    } catch {
      return null;
    }
  }
  return null;
}

export function setUserInfo<T>(user: T): void {
  localStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function removeUserInfo(): void {
  localStorage.removeItem(USER_KEY);
}

// 清除所有存储
export function clearStorage(): void {
  removeToken();
  removeUserInfo();
}

// 通用存储方法
export function getStorage<T>(key: string): T | null {
  const value = localStorage.getItem(key);
  if (value) {
    try {
      return JSON.parse(value) as T;
    } catch {
      return value as unknown as T;
    }
  }
  return null;
}

export function setStorage(key: string, value: any): void {
  if (typeof value === "string") {
    localStorage.setItem(key, value);
  } else {
    localStorage.setItem(key, JSON.stringify(value));
  }
}

export function removeStorage(key: string): void {
  localStorage.removeItem(key);
}
