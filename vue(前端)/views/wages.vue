<template>
  <div class="container">
    <div class="top">
      <div class="top1">今日收入: {{ todaytotal }}元</div>
      <div class="top1">今日完成订单数: {{ count }}</div>
      <div class="top1">总收入: {{ total }}元</div>
    </div>
    <div class="main">
      <div class="echarts" ref="ech"></div>
    </div>
  </div>
  <audio src="/1.mp3" ref="aud" preload="auto" hidden></audio>
  <audio src="/2.mp3" ref="aud2" preload="auto" hidden></audio>
</template>
<script setup>
// 使用动态导入的方式加载echarts，减小初始chunk大小
let echarts = null;
let ec = null;

// 动态导入echarts相关模块
const loadECharts = async () => {
  if (!echarts) {
    try {
      // 简单直接地动态导入ECharts
      const echartsModule = await import('echarts');
      echarts = echartsModule.default || echartsModule;
    } catch (error) {
      console.error('ECharts加载失败:', error);
      throw error;
    }
  }
  return echarts;
};
import {inject, onMounted, onUnmounted, ref} from "vue";
import base from "@/res/request.js";
import {ElNotification} from "element-plus";

let da1 = ref([])
let da2 = ref([])
const ech = ref(null)
const todaytotal = ref()
const count = ref()
const total = ref()
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

// 获取销售数据
const fetchSalesData = async () => {
  try {
    // 获取今日收入和订单数
    const yyeRes = await base.get("/order/yye");
    if (yyeRes.data.status === 200) {
      todaytotal.value = yyeRes.data.data.todaytotal;
      count.value = yyeRes.data.data.counts;
      total.value = yyeRes.data.data.sumtotal;
    }
    
    // 获取销售额数据
    const ysaleRes = await base.get("/order/ysale");
    if (ysaleRes.data.status === 200) {
      da2.value = ysaleRes.data.data;
    }
    
    // 获取订单数数据
    const ysale2Res = await base.get("/order/ysale2");
    if (ysale2Res.data.status === 200) {
      da1.value = ysale2Res.data.data;
      
      // 更新图表数据
      if (ec) {
        ec.setOption({
          series: [
            {
              name: '订单数',
              type: 'line',
              data: da1.value
            },
            {
              name: '收入',
              type: 'line',
              data: da2.value
            }
          ]
        });
      }
    }
  } catch (error) {
    console.error('数据获取失败:', error);
  }
};

// 刷新数据的函数
const shuaxin = () => {
  fetchSalesData();
};

// ECharts配置
const ecconfig = {
  title: {
    text: '营业统计',
    subtext: '订单统计',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['订单数', '收入']
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '订单数',
      type: 'line',
      data: []
    },
    {
      name: '收入',
      type: 'line',
      data: []
    }
  ]
};
onMounted(async () => {
  try {
    // 动态加载echarts并初始化
    await loadECharts();
    ec = echarts.init(ech.value);
    ec.setOption(ecconfig);
    
    // 获取数据
    await fetchSalesData();
    
    // 监听窗口大小变化，自适应图表
    window.addEventListener('resize', handleResize);
  } catch (error) {
    console.error('初始化失败:', error);
  }
});

// 窗口大小变化时调整图表大小
const handleResize = () => {
  if (ec) {
    ec.resize()
  }
}

// 组件卸载时销毁echarts实例，避免内存泄漏
onUnmounted(() => {
  if (ec) {
    ec.dispose()
    ec = null
  }
  window.removeEventListener('resize', handleResize)
})
</script>
<style scoped>
.container {
  width: 100%;
}

.echarts {
  width: 100%;
  height: 100%;
}

.top1 {
  width: 33.33%;
  height: 80px;
  text-align: center;
  line-height: 80px;
  display: inline-block;
  font-weight: 700;
  font-size: 20px;
}

.top {
  width: 100%;
  height: 80px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.5);
  background-color: rgba(0, 0, 0, 0.3);
}

.main {
  width: 100%;
  height: 593px;
}
</style>