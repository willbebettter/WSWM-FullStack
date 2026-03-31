<template>
  <el-header height="140px">
    <el-row>
      <el-col :span="15"><img :src="tp" alt="图片加载失败">
        <b>{{ na }}</b>
        <el-button class="changename" type="default" @click="dialogFormVisible = true">修改名称</el-button>
      </el-col>
      <el-col :span="9">
        <el-upload
            v-model="files"
            :auto-upload="false"
            :limit="1"
            :file-list="tupian"
            @change="sj"
            class="tup">
          <el-button type="info" class="dj">点击上传图片</el-button>
        </el-upload>
        <el-button class="ml-3 bt2" type="warning" @click="shangchuan">
          上传新头像
        </el-button>
      </el-col>
    </el-row>
  </el-header>
  <el-main>
    <el-row>
      <el-col :span="5" class="pl" v-for="(item,index) in gkpl" :key="index">
        <el-col class="yhpho" :span="3"><img :src="item.photo" alt="加载图片失败" class="tu2"></el-col>
        <el-col class="yhname" :span="15">{{ item.name }}</el-col>
        <el-col class="yhhf" :span="6">
          <button class="ph3" @click="hf(item.name)">发消息</button>
        </el-col>
      </el-col>
    </el-row>
  </el-main>
  <el-dialog v-model="dialogFormVisible" title="修改姓名" width="500" align-center>
    <el-form :model="form" :rules="rules" :inline-message="true">
      <el-form-item label="请输入要改为的新姓名" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="sub">修改</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">
          取消
        </el-button>
      </div>
    </template>
  </el-dialog>
  <audio src="/1.mp3" ref="aud" preload="auto" hidden></audio>
  <audio src="/2.mp3" ref="aud2" preload="auto" hidden></audio>
</template>
<script setup>
import {ElMessage, ElMessageBox, ElNotification} from "element-plus";
const dialogFormVisible = ref(false)
const formLabelWidth = '180px'
const form = ref({
  name: '',
})
const gkpl = ref([])
const rules = {
  name: [
    {required: true, message: "用户名不能为空", trigger: "blur"},
    {max: 20, min: 2, message: "用户名长度不能超过20个字符或少于2个字符", trigger: "blur"},
    {pattern: "^[a-zA-Z]+$|^[\u4e00-\u9fa5]+$", message: "用户名格式不正确", trigger: "blur"}
  ]
}
const files = ref('')
import {pi} from '@/store/index.js'
import {jwtDecode} from 'jwt-decode'
import {inject, onMounted, ref} from 'vue'
import base from "@/res/request.js";
const hf = (val) => {
  ElMessageBox.prompt('请输入你要发送的消息', '发送', {
    confirmButtonText: '发送',
    cancelButtonText: '取消',
    inputPattern: /\S/, // 只要包含一个非空白字符（\S）就算通过
    inputErrorMessage: '发送内容不能为空',
  }).then(({value}) => {
    const sj = value.trim();
    const x = {
      name: val,
      msg: sj.trim()
    }
    socket2.send(JSON.stringify(x))
    ElMessage.success('发送成功')
  }).catch((err) => {
    console.log(err)
    ElMessage.error('发送失败,请稍后再试')
  })
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
const sub = () => {
  na.value = getjwt().usr
  if (!na.value || na.value === '') {
    ElMessage.error('请检查登录状态')
    location.href('/login')
  }
  if (na.value === form.value.name) {
    ElMessage.error('请勿修改为原用户名')
  }
  base.post("/home/changeusr", {}, {params: {usr: form.value.name, oldusr: na.value}}).then(res => {
    if (res.data.status === 200) {
      ElMessage.success('修改成功')
      store.tokenset(res.data.data)
      na.value = getjwt(store.token).usr
      dialogFormVisible.value = false
    } else {
      ElMessage.error('名称已存在,请使用其他名称')
    }
  }).catch(err => {
    console.error(err)
    ElMessage.error('修改异常,请稍后再试')
  })
}
const na = ref('')
const tp = ref('')
const store = pi()
const getjwt = () => jwtDecode(store.token)
onMounted(() => {
  const x = getjwt()
  na.value = x.usr
  if (!na.value || na.value === '') {
    ElMessage.error('请检查登录状态')
    location.href('/login')
  }
  base.get("/home/getphoto", {params: {usr: na.value}}).then(res => {
    tp.value = res.data.data;
  })
})
const sj = function (file, filelist) {
  if (Array.isArray(filelist)) {
    tupian.value = filelist;
  } else {
    console.log('不是数组类型');
  }
}
let tupian = ref([])
const shangchuan = () => {
  const form2 = new FormData()
  form2.append('usr', na.value)
  if (!tupian.value || tupian.value.length === 0) {
    ElMessage.error('请选择图片');
  }
  if (tupian.value[0].raw) {
    form2.append('photo', tupian.value[0].raw)
  }
  base.post("/home/changephoto",
      form2, {headers: {"Content-Type": "multipart/form-data"}}
  ).then(function (res) {
    if (res.data.status === 200) {
      store.tokenset(res.data.data)
      tp.value = getjwt(store.token).photo;
      ElMessage.success('修改头像成功')
    } else {
      ElMessage.error('修改头像失败,请稍后再试')
    }
  }).catch((err) => {
    console.error(err)
    ElMessage.error('修改头像异常,请稍后再试')
  })
}
base.get("/allappyh").then(res => {
  if (res.data.status === 200) {
    gkpl.value = res.data.data
  } else (ElMessage.error("获取全部用户失败"))
}).catch(err => {
  ElMessage.error("获取全部用户失败")
})
</script>
<style scoped lang="less">
.el-header {
  background-color: rgba(0, 0, 0, 0.8);
}

.ph3 {
  background-color: white;
  border-radius: 10px;
  width: 90%;
  height: 70%;
  border: 1px solid rgba(135, 206, 235, 0.66);
  margin-top: 10%;
}

.yhhf {
  height: 100%;
  display: inline-block;
  width: 100%
}

.tu2 {
  width: 90%;
  height: 90%;
  border-radius: 50%;
  margin: 5% 5%;
}

.pl {
  height: 50px;
  margin-right: 40px;
  background-color: rgba(240, 240, 240, 0.7);
  border: 1px solid rgb(135, 206, 235);
  border-radius: 10px;
  margin-bottom: 10px;
}

img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin: 10px auto;
  display: inline-block;
  vertical-align: middle;
}

b {
  margin-left: 50px;
  font-size: 20px;
  font-family: Monaco, fangsong;
  color: white;
}

.yhpho {
  height: 100%;
  display: inline-block;
  width: 100%
}

.yhname {
  height: 100%;
  display: inline-block;
  line-height: 45px;
  padding-left: 10px;
  width: 100%

}

.dj {
  height: 50px;
  background-color: rgba(0, 0, 0, 0.18);
  color: rgba(255, 255, 255, 0.83);

}

.changename {
  height: 40px;
  margin-left: 100px;
  width: 150px;
  background-color: rgba(0, 0, 0, 0.22);
  color: rgba(255, 255, 255, 0.83);;


}

.el-main {
  height: 534px;
  background: rgba(0, 0, 0, 0.7);
}

.bt2 {
  top: 50px;
  background: none;
  height: 50px;
  width: 120px;
  position: absolute;
  right: 150px;
}

.tup {
  position: absolute;
  right: 350px;
  margin-top: 50px;
}
</style>