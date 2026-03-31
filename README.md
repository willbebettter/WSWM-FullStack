# WSWM (Wa Sai Wai Mai) - 全栈外卖管理系统

WSWM 是一个完整端到端的全栈外卖（美食配送）解决方案。本项目包含了面向用户的**微信小程序**、面向商家的**后台管理系统**以及强大的 **Spring Boot 后端服务**。

## 🚀 项目结构

```text
├── java(后端)/          # Spring Boot 后端服务
├── vue(前端)/           # Vue 3 后台管理系统
└── wechat(微信小程序端)/ # 微信小程序客户端
```

---

## 🛠️ 技术栈

### 后端 (Backend)
- **核心框架**: Spring Boot
- **权限校验**: JWT (JSON Web Token)
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **实时通信**: WebSocket (用于订单实时通知)
- **其他**: Spring Cache, Spring Scheduling, AOP, Mybatis

### 前端管理端 (Frontend Admin)
- **框架**: Vue 3
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router
- **网络请求**: Axios
- **实时通信**: WebSocket

### 微信小程序 (Mini Program)
- **框架**: 原生微信小程序
- **样式**: Less
- **功能**: 微信登录、地址管理、在线下单、订单追踪

---

## 📦 快速开始

### 1. 环境准备
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+
- 微信开发者工具

### 2. 后端部署 `java(后端)`
1. 创建 MySQL 数据库 `project-waimai`。
2. 修改 `java/resources/application.yml` 中的数据库配置（用户名、密码）。
3. (可选) 在 `application.yml` 中配置你自己的微信 `appid` 和 `secret`。
4. 启动 Redis 服务。
5. 运行 `WswmApplication.java`。

### 3. 后台管理系统 `vue(前端)`
1. 进入目录：`cd vue(前端)`
2. 安装依赖：`npm install`
3. 启动项目：`npm run dev`
4. 访问地址：通常为 `http://localhost:5173`

### 4. 微信小程序 `wechat(微信小程序端)`
1. 使用微信开发者工具打开 `wechat(微信小程序端)` 文件夹。
2. 修改 `utils/util.js` 或相关配置中的服务器请求地址（指向你的后端 IP/域名）。
3. 详情 -> 本地设置 -> 勾选 "不校验合法域名"。

---

## ✨ 核心功能

- **用户侧 (小程序)**: 浏览商品、添加购物车、收货地址管理、下单支付、查看订单状态。
- **商家侧 (Web)**: 商品管理 (CRUD)、分类管理、订单处理、实时订单提醒、销售统计。
- **系统特性**: 
  - 采用 JWT 进行前后端分离的身份验证。
  - 使用 WebSocket 实现订单从用户下单到商家接单的实时推送。
  - 集成了 Redis 缓存以提高响应速度。

---

## 📝 备注
本项目为手写的全栈项目，适合作为学习 Spring Boot + Vue 3 + 小程序全栈开发的参考案例。
