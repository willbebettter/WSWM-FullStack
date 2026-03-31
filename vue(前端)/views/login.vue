<script setup>
import {ref} from "vue";
import base from "@/res/request.js"
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {pi} from "@/store/index.js"

// 创建store实例
const store = pi()

let zhuangtai = ref(true)
const qh = function () {
  dljy.value.usr = ""
  dljy.value.pwd = ""
  zhuangtai.value = false
  qk()
}

const qh1 = function () {
  zhuangtai.value = true
  zcjy.value.usr = ""
  zcjy.value.pwd = ""
  zcjy.value.repwd = ""
  zcjy.value.phone = ""
  zcjy.value.check = ""
  qk()
}
let dljy = ref({
  usr: "",
  pwd: "",
})
let zcjy = ref({
  usr: "",
  pwd: "",
  repwd: "",
  phone: "",
  check: "",
})
let gd1 = {
  paddingTop: "230px",
}
let gd2 = {
  paddingTop: "100px",
}
const formcheck = ref(null)
const formcheck2 = ref(null)
let tim = 60;
let js = null;
let jsq1 = ref(null)
const qk = function () {
  if (js !== null) {
    clearTimeout(js)
    tim = 60
    js = null;
    if (jsq1.value) {
      jsq1.value.text = "| 发送验证码"
      jsq1.value.style.removeProperty("pointer-events")
      jsq1.value.style.color = "rgba(0, 0, 0, 0.58)"
    }
  }
}
const fsyzm = (e) => {
  e.target.text = `(60s)后重新发送`
  e.target.style.pointerEvents = "none";
  e.target.style.color = "rgba(0, 0, 0, 0.45)"
  js = setTimeout(function () {
    tim--
    e.target.text = `(${tim}s)后重新发送`
    if (tim <= 1) {
      tim = 60;
      e.target.text = "重新发送"
      e.target.style.remove("pointer-events")
    }
  }, 1000)
}
const form1 = {
  usr: [{required: true, message: "用户名不能为空", trigger: "blur"},
    {max: 20, min: 2, message: "用户名长度不能超过20个字符或少于2个字符", trigger: "blur"},
    {pattern: "^[a-zA-Z]+$|^[\u4e00-\u9fa5]+$", message: "用户名格式不正确", trigger: "blur"}],
  pwd: [{required: true, message: "密码不能为空", trigger: "blur"},
    {max: 20, min: 6, message: "密码长度不能超过20个字符或少于6个字符", trigger: "blur"}, {
      pattern: "^[a-zA-Z0-9_-]{0,20}$", message: "密码格式不正确", trigger: "blur"
    }],
  repwd: [{required: true, message: "密码不能为空", trigger: "blur"},
    {max: 20, min: 6, message: "密码长度不能超过20个字符或少于6个字符", trigger: "blur"}, {
      pattern: "^[a-zA-Z0-9_-]{0,20}$", message: "密码格式不正确", trigger: "blur"
    }, {
      validator: function (rule, value, callback) {
        if (value !== zcjy.value.pwd) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      }
    }],
  phone: [{required: true, message: "手机号不能为空", trigger: "blur"}, {
    max: 11, min: 11, message: "手机号长度为11位", trigger: "blur"
  }, {pattern: "^[1][3-9][0-9]{9}$", message: "手机号格式不正确", trigger: "blur"}],
  check: [{required: true, message: "请输入验证码", trigger: "blur"}, {
    pattern: "^[0-9]{6}$",
    message: "验证码错误",
    trigger: "blur"
  }]
}
const zc = function () {
  formcheck2.value.validate((value) => {
    if (!value) {
      ElMessage.error('请检查输入信息')
    }
    if (value) {
      base.post("/register", {
        usr: zcjy.value.usr.trim(),
        pwd: zcjy.value.pwd.trim(),
        phonenumber: zcjy.value.phone,
        checkcode: zcjy.value.check
      }).then(function (res) {
        if (res.data.status === 200) {
          ElMessage.success('注册成功,请返回重新登录')
          zcjy.value.usr = ""
          zcjy.value.pwd = ""
          zcjy.value.repwd = ""
          zcjy.value.phone = ""
          zcjy.value.check = ""
          zhuangtai.value = true
        } else {
          console.log(res.data)
          ElMessage.error('注册失败,请检查输入内容是否有误')
        }
      }).catch((err) => {
        console.error(err)
        ElMessage.error('注册异常,请稍后再试')
      })
    }
  })
}
const dl = function () {
  formcheck.value.validate((value) => {
    if (!value) {
      ElMessage.error('请检查输入信息')
    }
    if (value) {
      base.post("/login", {
        usr: dljy.value.usr.trim(),
        pwd: dljy.value.pwd.trim()
      }).then(function (res) {
        if (res.data.status === 200) {
          store.tokenset(res.data.data)
          ElMessage.success('登录成功')
          zcjy.value.usr = ""
          zcjy.value.pwd = ""
          zcjy.value.repwd = ""
          zcjy.value.phone = ""
          zcjy.value.check = ""
          zhuangtai.value = true
          router.push("/home")
        } else {
          console.log(res.data)
          ElMessage.error('登录失败,请检查用户名或密码')
        }
      }).catch((err) => {
            console.error(err)
            ElMessage.error('登录异常,请稍后再试')
          }
      )
    }
  })
}
</script>
<template>
    <span class="sp1"><b class="top1">哇塞外卖</b>
      <b class="top2">管理端</b>
    </span>
  <span class="sp2">
    <el-form v-show="zhuangtai" :rules="form1" :model="dljy" ref="formcheck" :inline-message="true" :style="gd1">
      <el-form-item label="账号:" prop="usr">
        <el-input placeholder="请输入用户名" v-model="dljy.usr"></el-input>
      </el-form-item>
      <el-form-item label="密码:" prop="pwd">
        <el-input placeholder="请输入密码" v-model="dljy.pwd" type="password" @keyup.enter="dl"></el-input>
      </el-form-item>
      <div id="zcrq"><a class="zc" @click="qh">还没有账号,立即注册</a></div>
      <el-button type="primary" plain @keyup.enter="dl" @click="dl">登录</el-button>
    </el-form>
    <el-form v-show="!zhuangtai" :rules="form1" :model="zcjy" ref="formcheck2" :inline-message="true" :style="gd2">
      <el-form-item label="账号:" prop="usr">
        <el-input placeholder="请输入用户名" v-model="zcjy.usr"></el-input>
      </el-form-item>
      <el-form-item label="密码:" prop="pwd">
        <el-input placeholder="请输入密码" v-model="zcjy.pwd" type="password"></el-input>
      </el-form-item>
      <el-form-item label="确认密码:" prop="repwd">
        <el-input v-model="zcjy.repwd" type="password" placeholder="请再次输入密码"></el-input>
      </el-form-item>
      <el-form-item label="手机号:" prop="phone">
        <el-input placeholder="请输入手机号" v-model="zcjy.phone"></el-input>
      </el-form-item>
      <el-form-item label="验证码:" prop="check">
        <el-input placeholder="请输入收到的验证码" v-model="zcjy.check" class="yzm"></el-input>
        <a class="zi1" @click="fsyzm" ref="jsq1">| 发送验证码</a>
      </el-form-item>
      <div id="zcrq"><a class="zc" @click="qh1">已有账号,返回登录</a></div>
      <el-button type="primary" plain @click="zc">注册</el-button>
    </el-form>
   </span>
</template>

<style scoped lang="less">

.top1 {
  font-family: 'Courier New', Courier, monospace;
  font-size: 80px;
  text-align: center;
  padding-top: 260px;
  color: rgb(240, 240, 240);
  display: block;
}

.sp1 {
  width: 45%;
  height: 100%;
  display: inline-block;
  background-color: rgba(0, 0, 0, 0.91);
  text-align: center;
  font-weight: lighter;
}

.top2 {
  font-size: 30px;
  margin-top: 30px;
  color: #997404;
}

.el-input {
  width: 400px;
}

.el-form-item {
  padding-left: 200px;
  margin-top: 50px
}

button {
  position: relative;
  left: 330px;
  width: 200px;
  background-color: cornflowerblue;
  color: rgba(0, 0, 0, 0.52);
  height: 40px;
  margin-top: 50px;
  font-size: 20px;
}

.zc {
  font-size: 10px;
  color: rgba(0, 0, 0, 0.41);
  float: right;
  margin-right: 150px;

  &:hover {
    color: rgba(255, 0, 0, 0.66);
    cursor: pointer;
  }
}

.gd {
  background-color: rgba(0, 0, 0, 0.35)
}

.yzm {
  width: 270px
}

.zi1 {
  position: absolute;
  right: 310px;
  color: rgba(0, 0, 0, 0.58);
  font-size: 14px;
  cursor: default;
}

.sp2 {
  width: 55%;
  height: 100%;
  display: inline-block
}
</style>