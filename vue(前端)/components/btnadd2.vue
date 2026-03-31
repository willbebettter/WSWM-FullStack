<script setup>
import {ref} from 'vue'
import base from "@/res/request.js";
import {ElMessage} from "element-plus";

const idlist = ["拼好饭", "热销组合", "超值套餐", "疯狂星期四"]
const emit = defineEmits(['shuaxin'])
const beforeUpload = function (file) {
  if (file.type !== "image/jpeg" && file.type !== "image/png" && file.type !== "image/jpg" && file.type !== "image/webp") {
    ElMessage.error('请上传图片文件')
    return false
  }
  return true
}
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'
const qr = function () {
  if (!tupian.value || tupian.value.length === 0) {
    ElMessage.error('请选择套餐图片');
    return;
  }
  if (form.value.mix.length < 2 || form.value.mix.length > 10) {
    ElMessage.error('请检查套餐包含菜品数')
    return;
  }
  if (form.value.type === '') {
    ElMessage.error('请选择套餐种类')
  } else {
    const ffr = form.value.mix.join()
    const form2 = new FormData()
    form2.append('mix_name', form.value.name)
    form2.append('mix_text', form.value.text)
    form2.append('mix_price', parseInt(form.value.price))
    form2.append('type_name', form.value.type)
    form2.append('list', ffr)
    form2.append('mix_photo', tupian.value[0].raw)
    base.post("/home/se2mixgood", {'mix_name': form.value.name}, {headers: {"Content-Type": "application/x-www-form-urlencoded"}}).then(function (res) {
      if (res.data.status === 200) {
        base.post("/home/addmixgood",
            form2, {headers: {"Content-Type": "multipart/form-data"}}
        ).then(function (res) {
          if (res.data.status === 200) {
            dialogFormVisible.value = false;
            ElMessage.success('添加套餐成功')
            emit('shuaxin')
          } else {
            ElMessage.error('添加失败,请稍后再试')
            dialogFormVisible.value = false;
          }
        }).catch((err) => {
          dialogFormVisible.value = false;
          console.error(err)
          ElMessage.error('添加异常,请稍后再试')
        })
      } else {
        ElMessage.error('套餐名称已存在')
      }
    }).catch((err) => {
      console.error(err)
      ElMessage.error('添加异常,请稍后再试')
    })
  }
}
const formck1 = ref(null)
const formck = () => {
  formck1.value.validate((value) => {
    if (value) {
      qr()
    } else {
      ElMessage.error('请检查输入信息')
    }
  })
}
const sj = function (file, filelist) {
  if (Array.isArray(filelist)) {
    tupian.value = filelist;
  } else {
    console.log('不是数组类型');
  }
}
const form = ref({
  name: '',
  text: '',
  price: '',
  type: '',
  mix: []
})
let tupian = ref([])
const rule = {
  name: [
    {required: true, message: '请输入套餐名称', trigger: 'blur'},
    {min: 1, max: 10, message: '套餐名称长度应在1~10个字符间', trigger: 'blur'},
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
    {required: true, message: '请选择套餐种类', trigger: 'blur'},
  ],
  mix: [{required: true, message: '请选择套餐菜品', trigger: 'blur'}, {
    min: 2, max: 10, message: '请至少选择两个个菜品', trigger: 'change'

  }]
}
const qk = function () {
  form.value.name = ''
  form.value.text = ''
  form.value.price = ''
  form.value.type = ''
  form.value.mix = []
  tupian.value = []
}
const sel = ref(null)
const xz = function () {
  if (form.value.type === '') {
    sel.value.classList.add('wrong')
    sel.value.placeholder = '请选择套餐种类'
    xs.value = true
  } else {
    xs.value = false
    sel.value.classList.remove('wrong')
  }
}
const options = ref([])
const xs = ref(false)
base.get('/home/getgoodsname').then(function (res) {
  if (res.data.status === 200) {
    res.data.data.map((item) => {
      options.value.push({label: item, value: item})
    })
  } else {
    console.log('获取所有菜品名称失败')
  }
})
</script>
<template>
  <el-button type="primary" class="glan" @click="dialogFormVisible = true">+</el-button>
  <el-dialog v-model="dialogFormVisible" title="添加套餐" width="900" align-center :close-on-click-modal="true"
             @close="qk">
    <el-form :model="form" :rules="rule" :inline-message="true" ref="formck1">
      <el-form-item label="套餐名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="套餐原料(选填)" :label-width="formLabelWidth" prop="text">
        <el-input v-model="form.text" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="套餐售价(每份)" :label-width="formLabelWidth" prop="price">
        <el-input v-model="form.price" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="套餐种类" :label-width="formLabelWidth" prop="type">
        <select style="width: 250px" v-model="form.type" class="se" @blur="xz" ref="sel">
          <option v-for="item in idlist" :key="item" :label="item" :value="item"></option>
        </select>
        <div class="cpxz" v-show="xs">请选择套餐类型</div>
      </el-form-item>
      <el-form-item label="套餐菜品" :label-width="formLabelWidth" prop="mix">
        <el-select
            v-model="form.mix"
            multiple
            collapse-tags
            collapse-tags-tooltip
            :max-collapse-tags="4"
            placeholder="请选择菜品组合"
            style="width: 240px">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="套餐图片" :label-width="formLabelWidth">
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
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="qr">
          添加
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped lang="less">
.glan {
  position: fixed;
  right: 30px;
  bottom: 15px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  font-size: 30px;
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