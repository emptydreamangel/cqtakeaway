# 积天外卖 (CQ Takeaway)

一个基于 Vue 3 + Spring Boot 的外卖点餐系统，支持用户浏览商家、点餐下单、地址管理等功能。

## 技术栈

### 前端 (front/)

- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite 5
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP 客户端**: Axios
- **CSS 预处理器**: Sass

### 后端 (jitian/)

- **框架**: Spring Boot 3.2.1
- **语言**: Java 21
- **ORM**: MyBatis-Plus
- **数据库**: PostgreSQL
- **安全**: Spring Security
- **构建工具**: Maven

## 项目结构

```
cqtakeaway/
├── front/                # 前端项目
│   ├── src/
│   │   ├── api/          # API 接口
│   │   ├── components/   # 公共组件
│   │   ├── views/        # 页面视图
│   │   ├── stores/       # Pinia 状态管理
│   │   ├── types/        # TypeScript 类型定义
│   │   └── utils/        # 工具函数
│   └── package.json
├── jitian/               # 后端项目
│   ├── src/main/java/com/blue/jitian/
│   │   ├── Controller/   # 控制器
│   │   ├── Service/      # 业务逻辑
│   │   ├── Entity/       # 实体类
│   │   ├── Mapper/       # MyBatis 映射
│   │   └── Config/       # 配置类
│   └── pom.xml
└── API接口文档.md         # API 文档
```

## 功能模块

- **用户模块**: 注册、登录、个人信息管理
- **商家模块**: 商家列表、商家详情、分类筛选
- **商品模块**: 商品浏览、规格选择
- **购物车**: 添加商品、修改数量、结算
- **订单模块**: 下单、订单列表、订单详情
- **地址管理**: 新增、编辑、删除收货地址
- **评价模块**: 订单评价、查看评价

## 快速开始

### 环境要求

- Node.js 18+
- Java 21+
- PostgreSQL 14+
- Maven 3.8+

### 数据库配置

1. 创建 PostgreSQL 数据库：

```sql
CREATE DATABASE jitian;
```

2. 修改后端配置文件 `jitian/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jitian
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 启动后端

```bash
cd jitian
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 启动前端

```bash
cd front
npm install
npm run dev
```

前端服务将在 http://localhost:3000 启动

## API 文档

详细的 API 接口文档请参考 [API 接口文档.md](./API接口文档.md)

## 主要页面

| 页面     | 路由      | 描述                   |
| -------- | --------- | ---------------------- |
| 首页     | /home     | 商家分类和推荐商家列表 |
| 登录     | /login    | 用户登录               |
| 注册     | /register | 用户注册               |
| 商家详情 | /shop/:id | 商家信息和商品列表     |
| 购物车   | /cart     | 购物车商品管理         |
| 订单列表 | /orders   | 用户订单列表           |
| 地址管理 | /address  | 收货地址管理           |
| 个人中心 | /user     | 用户个人信息           |

## 开发说明

### 前端开发

```bash
# 安装依赖
npm install

# 开发模式
npm run dev

# 构建生产版本
npm run build

# 代码检查
npm run lint
```

### 后端开发

```bash
# 编译项目
mvn compile

# 运行测试
mvn test

# 打包
mvn package
```

## License

MIT
