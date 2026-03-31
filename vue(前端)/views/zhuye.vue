<template>
  <div class="h-6"/>
  <el-menu
      class="el-menu-demo"
      mode="horizontal"
      background-color="rgba(0,0,0,0.7)"
      text-color="#fff"
      active-text-color="#ffd04b"
      :default-active="$route.path"
      router>
    <el-menu-item index="/home/goods">商品管理</el-menu-item>
    <el-menu-item index="/home/mixgoods">套餐设置</el-menu-item>
    <el-menu-item index="/home/orders">订单详情</el-menu-item>
    <el-menu-item index="/home/wages">营销统计</el-menu-item>
    <el-menu-item index="/home/account">账号管理</el-menu-item>
    <a class="bt1" @click="zt">{{ yingye }}</a>
    <a class="bt2" @click="logout">退出登录</a>
    <span class="yh">
      <span class="tprq"><img :src="tup" alt="图片加载失败" class="tx"></span><b class="yhna">{{ usr1 }}</b>
    </span>
  </el-menu>
  <router-view></router-view>
</template>
<script setup>
import {inject, onMounted, ref} from 'vue'
import {ElMessage} from "element-plus";
import router from "@/router/index.js"
import {pi} from "@/store"
import {jwtDecode} from "jwt-decode";
import base from "@/res/request.js";

const tup = ref('')
const usr1 = ref('')
let yingye = ref("未营业")
const store = pi()
const logout = () => {
  if (confirm("确定要退出吗？")) {
    ElMessage.success('已成功登出')
    store.tokenclear()
    router.push("/")
  }
}
const getjwt = () => jwtDecode(store.token)
const zt = () => {
  if (yingye.value === "营业中") {
    if (confirm("确定停止营业吗?")) {
      base.post('/order/salestatus', {}, {
        params: {
          status: 0
        }
      }).then(res => {
        if (res.data.status === 200) {
          yingye.value = "未营业"
          ElMessage.success('切换为未营业')
        } else {
          ElMessage.error('异常,请稍后再试')
        }
      }).catch(() => {
        ElMessage.error('异常,请稍后再试')
      })
    }
  } else {
    if (confirm("确定开始营业吗?")) {
      base.post('/order/salestatus', {}, {
        params: {
          status: 1
        }
      }).then(res => {
        if (res.data.status === 200) {
          yingye.value = "营业中"
          ElMessage.success('切换为营业中')
        } else {
          console.log(res.data)
          ElMessage.error('异常,请稍后再试')
        }
      }).catch(() => {
        ElMessage.error('异常,请稍后再试')
      })
    }
  }
}
const yh = () => {
  usr1.value = getjwt().usr;
  tup.value = getjwt().photo;
}
onMounted(() => {
  if (!getjwt() || getjwt() === '') {
    ElMessage.error('请检查登录状态')
    location.href('/login')
  }
  yh()
})

const socket3 = inject("socket3")
socket3.onmessage = (e) => {
  let data = e.data;
  if (typeof data === 'string') {
    if (data === "ping") {
      socket3.send("pong");
      return; // 提前结束
    }
    else if (data === "web") {
      // 根据当前营业状态发送请求
      const status = yingye.value === "营业中" ? 1 : 0;
      base.post('/order/salestatus', {}, {
        params: { status }
      }).then(res => {
        if (res.data.status !== 200) {
          console.log('状态同步失败:', res.data);
        }
      }).catch(err => {
        console.error('请求异常:', err);
      });
      return;
    }
    try {
      const jsonData = JSON.parse(data);
    } catch (error) {
      console.log('收到未知文本消息，已忽略:', data);
    }

  }
  // 如果是 Blob 或 ArrayBuffer，通常也是二进制数据，不需要 JSON.parse
  else {
    console.log('收到非文本消息:', data);
  }
};
socket3.onclose = () => {
  location.reload()
}
</script>
<style lang="less" scoped>
* {
  margin: 0;
  padding: 0;
}

.yhna {
  font-size: 15px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  width: 100px;
  display: inline-block;
  color: #f0f0f0;
  cursor: default;
}

.tprq {
  width: 50px;
  height: 60px;
  display: inline-block;
  padding-top: 10px;
}

.yh {
  position: absolute;
  right: 420px;
  width: 300px;

  height: 60px;
  text-align: center;
  line-height: 60px;
  color: #000000;
  font-size: 15px;
  vertical-align: center;
}

.tx {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.el-menu-item {
  width: 160px;
}

.bt1 {
  font-size: 15px;
  position: absolute;
  right: 280px;
  color: #fff;
  height: 60px;
  line-height: 60px;
  cursor: pointer;
  text-align: center;

  &:hover {
    color: rgb(255, 0, 0);
  }
}

.bt2 {
  font-size: 15px;
  position: absolute;
  right: 20px;
  color: #fff;
  height: 60px;
  line-height: 60px;
  text-align: center;
  background-color: rgba(255, 0, 0, 0.7);
  width: 150px;
  cursor: pointer;
}
</style>
