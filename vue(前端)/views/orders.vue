<template>
  <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      :ellipsis="false"
      @select="handleSelect">
    <el-menu-item index="1">全部订单</el-menu-item>
    <el-menu-item index="2">待出餐</el-menu-item>
    <el-menu-item index="3">已出餐</el-menu-item>
    <el-menu-item index="4">已送达</el-menu-item>
    <el-menu-item index="5">已取消</el-menu-item>
  </el-menu>
  <div class="main">
    <div v-for="(item,index) in wzlist" :key="index" v-show="kg" class="splist">
      <div class="left1"><img :src="item.photo" alt="加载图片失败" class="pho1"></div>
      <div class="right1">
        <div class="li1">订单名称 : {{ item.name }}</div>
        <div class="li2">用户地址 : {{ item.address }}</div>
        <div class="li6">订单编号 : {{ item.uuid }}</div>
        <div class="li4">下单时间 : {{ item.time }}</div>
        <div class="li3">用户备注 : {{ item.msg }}</div>
        <div class="li5">
          <div class="lli1">实付款 : {{ item.sum }}元</div>
          <div class="lli2">
            <button class="btn1" :data-id="item.uuid" @click="cancel" v-show="item.status===0">取消订单</button>
            <button class="btn2" :data-id="item.uuid" @click="cc" v-show="item.status===0">出餐</button>
            <div class="ddzt" :data-id="item.uuid" v-show="item.status===1">已出餐</div>
            <div class="ddzt" :data-id="item.uuid" v-show="item.status===3">已完成</div>
            <div class="ddzt" :data-id="item.uuid" v-show="item.status===5">已取消</div>
            <div class="ddzt" :data-id="item.uuid" v-show="item.status===4">用户已评价</div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="content-area" v-show="!kg">
    <el-empty description="暂无订单数据"/>
  </div>
  <audio src="/1.mp3" ref="aud" preload="auto" hidden></audio>
  <audio src="/2.mp3" ref="aud2" preload="auto" hidden></audio>
</template>
<script setup>
import {inject, ref} from 'vue'
import base from "@/res/request.js";
import {ElMessage, ElNotification} from "element-plus";
let wzlist = ref([])
let kg = ref(true)
const activeIndex = ref('1')
const handleSelect = (key) => {
  switch (key) {
    case '1':
      wzlist.value = list1.value
      break;
    case '2':
      wzlist.value = list1.value.filter(item => item.status === 0)
      break;
    case '3':
      wzlist.value = list1.value.filter(item => item.status === 1)
      break;
    case '4':
      wzlist.value = list1.value.filter(item => item.status === 3 || item.status === 4)
      break;
    case '5':
      wzlist.value = list1.value.filter(item => item.status === 5)
      break;
  }
}
const socket = inject('socket')
const socket2 = inject('socket2')
const aud = ref(null)
const audioplay1 = () => {
  const audio = aud.value
  if (audio) {
    audio.currentTime = 0
    audio.play().catch(err => {
      console.log(err)
    })
  }
}
const aud2 = ref(null)
const audioplay2 = () => {
  const audio = aud2.value
  if (audio) {
    audio.currentTime = 0
    audio.play().catch(err => {
      console.log(err)
    })
  }
}
socket2.onmessage = (event) => {
  if (event.data === "ping") {
    socket2.send("pong")
  } else {
    ElNotification({
      title: "收到一条新通知",
      message: event.data,
      duration: 30000,
      position: 'bottom-right',
    })
  }
};
socket.onmessage = (event) => {
  if (event.data === "ping") {
    socket.send("pong")
  } else {
    const data = JSON.parse(event.data)
    const [[key, value]] = Object.entries(data);
    if (key === "newdd") {
      newdd(value)
    } else if (key === "tk") {
      tk(value)
    } else if (key === "cd") {
      cd(value)
    } else if (key === "pj") {
      pj(value)
    } else {
      console.log("未知消息:" + data)
    }
  }
}
socket.onclose = () => {
  location.reload()
}
socket2.onclose = () => {
  location.reload()
}
const newdd = (uuid) => {
  audioplay1()
  shuaxin()
  ElNotification({
    title: '新订单',
    message: '您有新订单了,订单号为:' + uuid,
    duration: 20000,
    position: 'bottom-right',
  })
}
const tk = (uuid) => {
  shuaxin()
  ElNotification({
    title: '收到退款',
    message: '有用户退款了,订单号为:' + uuid,
    duration: 20000,
    position: 'bottom-right',
  })
}
const cd = (uuid) => {
  audioplay2()
  ElNotification({
    title: '有用户催单',
    message: '有用户催单了,订单号为:' + uuid,
    duration: 20000,
    position: 'bottom-right',
  })
}
const pj = (pj) => {
  ElNotification({
    title: '收到用户评价',
    message: pj,
    duration: 20000,
    position: 'bottom-right',
  })
}
const list1 = ref([])
base.get("/order/allorders").then(res => {
  if (res.data.status === 200) {
    list1.value = res.data.data
    wzlist.value = res.data.data
    if (list1.value.length === 0) {
      kg.value = false;
    } else {
      kg.value = true;
    }
  } else {
    list1.value = [];
    kg.value = false;
    ElMessage.error('获取订单列表失败')
  }
}).catch(err => {
  kg.value = false;
  console.log(err)
})
const cc = (e) => {
  base.put("/order/out", {}, {
    params: {
      uuid: e.currentTarget.dataset.id
    }
  }).then(res => {
    if (res.data.status === 200) {
      shuaxin()
      ElMessage.success('出餐成功')
    } else {
      ElMessage.error('出餐失败')
    }
  })
}
const cancel = (e) => {
  if (confirm("确定取消该订单吗?")) {
    base.put("/order/cancel", {}, {
      params: {
        uuid: e.currentTarget.dataset.id
      }
    }).then(res => {
      if (res.data.status === 200) {
        shuaxin()
        ElMessage.success('取消订单成功')
      } else {
        ElMessage.error('取消订单失败')
      }
    })
  }
}
const shuaxin = () => {
  base.get("/order/allorders").then(res => {
    base.get("/order/allorders").then(res => {
      if (res.data.status === 200) {
        list1.value = res.data.data
        wzlist.value = res.data.data
      } else {
        kg.value = false;
        ElMessage.error("获取订单列表失败")
      }
    }).catch(err => {
      kg.value = false;
      ElMessage.error("获取订单列表失败")
    })
  })
}

</script>
<style scoped lang="less">
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

.ddzt {
  height: 100%;
  line-height: 75px;
  font-weight: 700;
  text-align: right;
  margin-right: 20px;
  margin-top: 10px;
}

.btn1 {
  width: 100px;
  height: 80%;
  float: right;
  margin-top: 10px;
  background-color: rgba(255, 0, 0, 0.6);
  border: 1px solid rgba(0, 0, 0, 0.65);
  border-radius: 5px;
  color: white;
  margin-right: 10px;
}

.btn2 {
  width: 100px;
  height: 80%;
  float: right;
  margin-top: 10px;
  margin-right: 10px;
  background-color: rgba(0, 128, 21, 0.7);
  border: 1px solid rgba(0, 0, 0, 0.65);
  border-radius: 5px;
  color: white;
}

.pho1 {
  width: 100%;
  height: 100%;
}

.lli1 {
  width: 30%;
  height: 100%;
  font-size: 20px;
  line-height: 90px;
  vertical-align: top;
  display: inline-block;
}

.lli2 {
  width: 70%;
  height: 100%;
  vertical-align: top;
  display: inline-block;
}

.el-menu-demo {
  background-color: rgba(135, 206, 235, 0.7);
  width: 100%;
  height: 50px;
}

.splist {
  width: 50%;
  height: 200px;
  border-right: 1px solid rgba(0, 0, 0, 0.65);
  border-bottom: 1px solid rgba(0, 0, 0, 0.65);
  border-top: 1px solid rgba(0, 0, 0, 0.65);
  margin-bottom: 2px;
}

.li1 {
  height: 20%
}

.li2 {
  height: 10%
}

.li3 {
  height: 20%
}

.li4 {
  height: 10%
}

.li5 {
  height: 30%
}

.li6 {
  height: 10%
}

.left1 {
  width: 30%;
  height: 100%;
  display: inline-block
}

.right1 {
  width: 70%;
  height: 100%;
  display: inline-block
}

.el-menu-item {
  color: rgba(0, 0, 0, 0.65);
  width: 20%
}

.main {
  width: 100%;
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  margin-top: 1px;
  overflow-y: auto; /* 只保留垂直滚动 */
  max-height: 620px; /* 最大高度限制，超出才滚动 */
  padding-bottom: 10px; /* 防止最后一个卡片被底部遮挡 */
}

.content-area {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
