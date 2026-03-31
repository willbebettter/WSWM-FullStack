import {createRouter, createWebHistory} from "vue-router"
import {pi} from "@/store/index.js";
import {ElMessage} from "element-plus";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: () => import('@/views/login.vue'),
        }, {
            namespaced: true,
            path: '/home', redirect: '/home/goods',
            component: () => import('@/views/zhuye.vue'), children: [{
                path: '/home/goods',
                component: () => import('@/views/goods.vue')
            }, {path: '/home/mixgoods', component: () => import('@/views/mixgoods.vue')}, {
                path: '/home/orders', component: () => import('@/views/orders.vue')
            }, {path: '/home/wages', component: () => import('@/views/wages.vue')}, {
                path: '/home/account', component: () => import('@/views/account.vue')
            }]
        }, {path: '/:pathMatch(.*)*', component: () => import('@/views/404.vue')}
    ]
})
router.beforeEach((to, from, next) => {
    if (to.path.includes("/home")) {
        // 创建store实例
        const store = pi()
        if (store.token) {
            next();
        } else {
            next("/");
            ElMessage.error("登录状态失效,请重新登录")
        }
    } else {
        next();
    }
})
export default router;