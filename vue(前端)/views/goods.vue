<script setup>
import Cbl from "@/components/cbl.vue";
import Btnadd from "@/components/btnadd.vue";
import base from "@/res/request.js"
import {inject, ref} from "vue";
import {ElMessage, ElNotification} from "element-plus";
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
let kg = ref(true)
let list1 = ref([]);
let list2 = ref();
base.get("/home/getgoods").then((res) => {
  if (res.data.status === 200) {
    list1.value = res.data.data
    if (list1.value.length === 0) {
      kg.value = false;
    } else {
      kg.value = true;
    }
  } else {
    list1.value = [];
    kg.value = false;
    console.log('获取菜品列表失败')
  }
}).catch((err) => {
  kg.value = false;
  console.error(err)
})
const shuaxin = () => {
  base.get("/home/getgoods").then((res) => {
    if (res.data.status === 200) {
      list1.value = res.data.data
      if (list1.value.length === 0) {
        kg.value = false;
      } else {
        kg.value = true;
      }
    } else {
      list1.value = [];
      kg.value = false;
      console.log('获取菜品列表失败')
    }
  }).catch((err) => {
    kg.value = false;
    console.error(err)
  })
}
const del1 = function (e) {
  const id = parseInt(e.currentTarget.dataset.id)
  base.get("/home/glgood", {params: {'id': id}}).then(function (res) {
    if (res.data.status === 200) {
      base.delete("/home/delgood", {params: {'id': id}}
      ).then((res) => {
        if (res.data.status === 200) {
          ElMessage.success('删除成功')
          shuaxin()
        } else {
          ElMessage.error('删除失败,请稍后再试')
        }
      }).catch((err) => {
        console.error(err)
        ElMessage.error('删除异常,请稍后再试')
      })
    } else {
      ElMessage.error('该菜品已经被套餐绑定,请取消套餐的关联再尝试')
    }
  }).catch((err) => {
    console.error(err)
    ElMessage.error('删除异常,请稍后再试')
  })
}
const idlist = ["主食类", "饮品类", "炸串小吃", "菜品"]
let dialogFormVisible = ref(false)
const chg1 = function (e) {
  dialogFormVisible.value = true
  id2.value = e.currentTarget.dataset.id
  base.get("/home/getgood",
      {
        params: {'id': parseInt(e.currentTarget.dataset.id)}
      }).then(function (res) {
    if (res.data.status === 200) {
      list2.value = res.data.data
      form.value.name = list2.value.good_name
      form.value.text = list2.value.good_text
      form.value.price = list2.value.good_price
      form.value.type = list2.value.type_name
      // 将后端返回的图片路径转换为el-upload组件所需的文件对象格式
      if (list2.value.good_photo) {
        // 从路径中提取文件名
        const fileName = list2.value.good_photo.substring(list2.value.good_photo.lastIndexOf('/') + 1);
        tupian.value = [
          {
            name: fileName,
            url: list2.value.good_photo,
            uid: Date.now(), // 生成唯一标识
            status: 'success'
          }
        ];
      } else {
        tupian.value = [];
      }
    } else {
      ElMessage.error('未查询到此条结果记录,请稍后再试')
    }
  }).catch((err) => {
    console.error(err)
    ElMessage.error('获取异常,请稍后再试')
  })
}
const qh = function (e) {
  if (e.currentTarget.innerText === "在售") {
    e.currentTarget.innerText = "停售"
    ElMessage.success('修改成功,状态变更为停售')
  } else if (e.currentTarget.innerText === "停售") {
    e.currentTarget.innerText = "在售"
    ElMessage.success('修改成功,状态变更为在售')
  }
}
let id2 = ref()
const formLabelWidth = '140px'
const qk = function () {
  form.value.name = ''
  form.value.text = ''
  form.value.price = ''
  form.value.type = ''
  tupian.value = []
}
const form = ref({
  name: '',
  text: '',
  price: '',
  type: ''
})
let tupian = ref([])
const rule = {
  name: [
    {required: true, message: '请输入菜品名称', trigger: 'blur'},
    {min: 1, max: 10, message: '菜品名称长度应在1~10个字符间', trigger: 'blur'},
    {pattern: "^[\u4e00-\u9fa5a-zA-Z0-9]{0,10}$", message: "请正确输入菜品名称", trigger: "blur"}
  ],
  text: [
    {min: 0, max: 50, message: '字数要求小于50个', trigger: 'blur'},
  ],
  price: [
    {required: true, message: '请输入售价', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (parseInt(value) <= 999999 && parseInt(value) > 0) {
          if (/^(0|[1-9][0-9]{0,5})(\.[0-9]{1,2})?$/.test(value)) {
            callback();
          } else {
            callback(new Error('小数最多两位'))
          }
        } else {
          callback(new Error('请输入正确的1~6位数的价格'))
        }
      }, trigger: 'blur'
    }
  ],
  type: [
    {required: true, message: '请选择菜品种类', trigger: 'blur'},
  ]
}
const beforeUpload = function (file) {
  if (file.type !== "image/jpeg" && file.type !== "image/png" && file.type !== "image/jpg" && file.type !== "image/webp") {
    ElMessage.error('请上传图片文件')
    return false
  }
  return true
}
const sj = function (file, filelist) {
  if (Array.isArray(filelist)) {
    tupian.value = filelist;
  } else {
    console.log('不是数组类型');
  }
}
const qr = function () {
  const form2 = new FormData()
  form2.append('id', id2.value)
  form2.append('good_name', form.value.name)
  form2.append('good_text', form.value.text)
  form2.append('type_name', form.value.type)
  form2.append('good_price', parseInt(form.value.price))
  if (!tupian.value || tupian.value.length === 0) {
    ElMessage.error('请选择菜品图片');
  }
  if (tupian.value[0].raw) {
    form2.append('good_photo', tupian.value[0].raw)
  }
  base.post("/home/segood", {
    'id': id2.value,
    'good_name': form.value.name
  }, {headers: {"Content-Type": "application/x-www-form-urlencoded"}}).then(function (res) {
    if (res.data.status === 200) {
      base.post("/home/changegood",
          form2, {headers: {"Content-Type": "multipart/form-data"}}
      ).then(function (res) {
        if (res.data.status === 200) {
          dialogFormVisible.value = false;
          ElMessage.success('修改菜品成功')
          shuaxin()
        } else {
          ElMessage.error('修改失败,请稍后再试')
          dialogFormVisible.value = false;
        }
      }).catch((err) => {
        dialogFormVisible.value = false;
        console.error(err)
        ElMessage.error('修改异常,请稍后再试')
      })
    } else {
      ElMessage.error('名称已存在,请设置其他名称')
    }
  }).catch((err) => {
    console.error(err)
    ElMessage.error('修改异常,请稍后再试')
  })
}
const zdj = (name3) => {
  if (name3 === "全部菜品") {
    shuaxin()
  } else {
    base.get("/home/fenleigood", {params: {'name': name3}}).then(function (res) {
      if (res.data.status === 200) {
        list1.value = res.data.data
        if (list1.value.length === 0) {
          kg.value = false;
        } else {
          kg.value = true;
        }
      } else {
        list1.value = [];
        kg.value = false;
        console.log('获取菜品分类列表失败')
      }
    }).catch((err) => {
      kg.value = false;
      console.error(err)
    })
  }
}
const sel = ref(null)
const xz = function () {
  if (form.value.type === '') {
    sel.value.classList.add('wrong')
    sel.value.placeholder = '请选择菜品种类'
    xs.value = true
  } else {
    xs.value = false
    sel.value.classList.remove('wrong')
  }
}
const xs = ref(false)
</script>
<template>
  <el-dialog v-model="dialogFormVisible" title="修改菜品信息" width="900" align-center :close-on-click-modal="true"
             @close="qk">
    <el-form :model="form" :rules="rule" :inline-message="true" ref="formck1">
      <el-form-item label="菜品名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="菜品原料(选填)" :label-width="formLabelWidth" prop="text">
        <el-input v-model="form.text" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="菜品售价(每份)" :label-width="formLabelWidth" prop="price">
        <el-input v-model="form.price" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="菜品种类" :label-width="formLabelWidth" prop="type">
        <select style="width: 250px" v-model="form.type" class="se" @blur="xz" ref="sel">
          <option v-for="item in idlist" :key="item" :label="item" :value="item"></option>
        </select>
        <div class="cpxz" v-show="xs">请选择菜品类型</div>
      </el-form-item>
      <el-form-item label="菜品图片" :label-width="formLabelWidth">
        <el-upload
            list-type="picture-card"
            :before-upload="beforeUpload"
            :auto-upload="false"
            :drag="true"
            :limit="1"
            :file-list="tupian"
            @change="sj">
          <el-button>点击上传</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">取消</el-button>
      <el-button type="primary" @click="qr">修改</el-button>
    </template>
  </el-dialog>
  <div class="container">
    <cbl @zdj="zdj">
      <template #cl1>全部菜品</template>
      <template #cl2>主食类</template>
      <template #cl3>饮品类</template>
      <template #cl4>炸串小吃</template>
      <template #cl5>菜品</template>
      <template #ic1>
        <el-icon>
          <DishDot/>
        </el-icon>
      </template>
      <template #ic2>
        <el-icon>
          <Food/>
        </el-icon>
      </template>
      <template #ic3>
        <el-icon>
          <IceDrink/>
        </el-icon>
      </template>
      <template #ic4>
        <el-icon>
          <Fries/>
        </el-icon>
      </template>
      <template #ic5>
        <el-icon>
          <ForkSpoon/>
        </el-icon>
      </template>
    </cbl>
    <div class="dhz" v-show="kg">
      <div class="xhz" v-for="item in list1" :key="item.id" v-show="list1!=[]">
        <img :src="item.good_photo" alt="图片加载失败" class="tp">
        <div class="ms">
          <div><span class="na1">{{ item.good_name }}</span><span class="xl">销量:{{ item.good_sale }}</span></div>
          <div class="xxhz2">
            <span class="xx1">原料:{{ item.good_text }}</span>
            <div class="xx2">
              <div class="price">{{ item.good_price }}元/份</div>
              <div class="btrq">
                <el-button @click="del1" :data-id="item.id">删除</el-button>
                <el-button @click="chg1" :data-id="item.id">编辑</el-button>
                <el-button @click="qh">在售</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="content-area" v-show="!kg">
      <el-empty description="暂无商品数据"/>
    </div>
    <btnadd @shuaxin="shuaxin"></btnadd>
  </div>
  <audio src="/1.mp3" ref="aud" preload="auto" hidden></audio>
  <audio src="/2.mp3" ref="aud2" preload="auto" hidden></audio>
</template>
<style scoped lang="less">
.container {
  display: flex;
  height: 100vh;
}

.content-area {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.xhz {
  border: 1px solid rgba(0, 0, 0, 0.35);
  height: 250px;
  width: 19%;
  margin-bottom: 5px;
  margin-right: 3px;
}

.dhz {
  width: 89%;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
  overflow: auto;
  padding-bottom: 80px;
}

.tp {
  width: 100%;
  height: 60%;
}

.ms {
  height: 40%;
  width: 100%;
}

.na1 {
  text-align: left;
  display: inline-block;
  width: 60%;
  font-weight: bold
}

.xl {
  text-align: right;
  display: inline-block;
  width: 40%;
  font-size: 12px;
}

.xxhz2 {
  display: table;
  width: 100%;
}

.xx1 {
  display: table-cell;
  width: 100%;
  font-size: 13px;
  padding-top: 10px;
  padding-right: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  white-space: normal;
  line-height: 1.5;
  box-sizing: border-box;
  vertical-align: top;
}

.xx2 {
  display: table-cell;
  width: 50%;
  vertical-align: top;
  text-align: center;
  padding-top: 10px;
}

.price {
  text-align: right;
  margin-bottom: 10px;
  margin-right: 10px;
}

.btrq {
  height: 30px;

  .el-button {
    width: 35px;
    height: 30px;
    padding: 0;
    margin: 0 3px;
    float: left;
  }
}

.se {
  outline: none;
  height: 30px;
  background: none;
  border: 1px #409eff solid;
  border-radius: 3px;
}

.wrong {
  border: red 1px solid !important;
}

.cpxz {
  color: rgba(255, 0, 0, 0.79);
  font-size: 12px;
  margin-left: 20px;
}
</style>