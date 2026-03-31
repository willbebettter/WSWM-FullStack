# WSWM (Wa Sai Wai Mai) - Full-stack Takeout Management System

WSWM is a complete, end-to-end takeout (food delivery) solution designed for campus or small-scale environments. It features a customer-facing WeChat Mini Program, a merchant-side management dashboard, and a robust Spring Boot backend.

## 🌟 Key Features

### 📱 Customer Side (WeChat Mini Program)
- **Menu Browsing:** Explore various categories (Main course, Drinks, Snacks, Set meals).
- **Ordering System:** Add items to cart, manage quantities, and place orders.
- **Real-time Status:** View merchant's business status (Open/Closed) synced in real-time.
- **Reviews & Feedback:** Post comments and rate dishes.
- **Personal Center:** Manage delivery addresses and view order history.

### 💻 Merchant Side (Web Dashboard)
- **Business Control:** Toggle "Open/Closed" status with instant sync to all users.
- **Goods Management:** Full CRUD for dishes and set meals (Mix-goods).
- **Order Management:** View incoming orders, mark as "Out for Delivery," or cancel orders.
- **Data Analytics:** Visualized sales statistics and revenue reports using ECharts.
- **Account System:** Manage merchant credentials and profile photos.

### ⚙️ Backend (RESTful API)
- **Real-time Sync:** Powered by WebSocket for instant order notifications and status updates.
- **Security:** JWT-based authentication for both merchants and customers.
- **Performance:** Redis caching for optimized data retrieval.
- **Logging:** AOP-based execution timing and logging for system monitoring.

---

## 🛠 Tech Stack

### Backend
- **Framework:** Spring Boot (v3/v4)
- **ORM:** MyBatis
- **Database:** MySQL 8.0
- **Cache:** Redis (Jedis)
- **Communication:** WebSocket (Real-time notifications)
- **Security:** JWT (JSON Web Token)
- **Utilities:** Lombok, Fastjson2, Slf4j (AOP Logging)

### Web Admin Dashboard
- **Framework:** Vue 3 (Vite)
- **UI Component:** Element Plus
- **State Management:** Pinia (with PersistedState)
- **Routing:** Vue Router
- **Visualization:** ECharts

### WeChat Mini Program
- **Platform:** Native WeChat Mini Program
- **Styling:** Less
- **Architecture:** Component-based UI

---

## 📂 Project Structure

```text
.
├── wswm-project/          # Spring Boot Backend
│   ├── src/main/java/     # Controller-Service-Mapper Architecture
│   └── src/main/resources/ # MySQL/Redis/WeChat Configurations
├── wswmqd/                # Vue.js Admin Dashboard (Frontend)
│   ├── src/views/         # Pages: Orders, Goods, Wages, Analytics
│   └── src/websocket/     # Real-time connection logic
└── miniprogram-1/         # WeChat Mini Program (Frontend)
    ├── pages/             # Pages: Index, Pay, Order, Profile
    └── images/            # UI Assets
```

---

## 🚀 Getting Started

### Prerequisites
- JDK 17+
- MySQL 8.0
- Redis
- Node.js (for Web Admin)
- WeChat DevTools (for Mini Program)

### Backend Setup
1. Configure your database and Redis in `wswm-project/src/main/resources/application.yml`.
2. (Optional) Update WeChat AppID/Secret for mini-program features.
3. Run `WswmApplication.java`.

### Web Admin Setup
1. Navigate to `wswmqd/`.
2. Install dependencies: `npm install`.
3. Start development server: `npm run dev`.

### Mini Program Setup
1. Open `miniprogram-1/` in WeChat DevTools.
2. Ensure the server URL matches your backend address.

---

## 📝 License
This project is for educational purposes.

---
*Created with ❤️ by a developer who loves "Wa Sai" food.*
