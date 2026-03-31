import {createApp} from 'vue'
import App from '@/App.vue'
import router from '@/router/index.js'
import {pinia} from '@/store/index.js'
// 导入Element Plus CSS样式
import 'element-plus/dist/index.css'
// 按需导入项目中实际使用的Element Plus图标
import { Memo, Bowl, List, Tickets, Burger, DishDot, Food, IceDrink, Fries, ForkSpoon } from '@element-plus/icons-vue'
import socket from "@/websocket/socket.js";
import socket2 from "@/websocket/socket2.js";
import socket3 from "@/websocket/socket3.js";
const app = createApp(App)
// 注册使用的图标
app.component('Memo', Memo)
app.component('Bowl', Bowl)
app.component('List', List)
app.component('Tickets', Tickets)
app.component('Burger', Burger)
app.component('DishDot', DishDot)
app.component('Food', Food)
app.component('IceDrink', IceDrink)
app.component('Fries', Fries)
app.component('ForkSpoon', ForkSpoon)
app.use(router)
app.use(pinia)
app.config.globalProperties.$socket =socket
app.config.globalProperties.$socket2 =socket2
app.config.globalProperties.$socket3 =socket3
app.provide('socket', socket)
app.provide('socket2', socket2)
app.provide('socket3', socket3)
app.mount('#app')