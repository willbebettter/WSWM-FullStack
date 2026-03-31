import axios from 'axios';
import {ElLoading} from "element-plus";

const load = () => ElLoading.service({
    lock: true,           // 锁定屏幕，禁止滚动
    text: '加载中...',     // 显示的文本
    background: 'rgba(0, 0, 0, 0.5)', // 背景遮罩
    // target: '.main-content' // 也可以指定某个 DOM 元素，不传默认全屏
})

const base = axios.create({
    baseURL: '/api',
    timeout: 5000,
})
base.interceptors.request.use(
    config => {
        load()
        return config;
    }
)
base.interceptors.response.use(
    res => {
        ElLoading.service().close();
        return res;
    }, err => {
        ElLoading.service().close();
        return Promise.reject(err);
    }
)
export default base;