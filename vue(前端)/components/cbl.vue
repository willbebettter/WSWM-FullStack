<template>
  <el-row class="tac">
    <el-col :span="24">
      <el-menu
          active-text-color="#ffd04b"
          background-color="#545c64"
          class="el-menu-vertical-demo"
          default-active="1"
          text-color="#fff"
      >
        <el-menu-item index="1" class="eltop" @click="dj('全部菜品')">
          <slot name="ic1"></slot>
          <slot name="cl1"></slot>
        </el-menu-item>
        <el-menu-item index="2"  @click="dj('主食类')">
          <slot name="ic2"></slot>
          <slot name="cl2"></slot>
        </el-menu-item>
        <el-menu-item index="3"  @click="dj('饮品类')">
          <slot name="ic3"></slot>
          <slot name="cl3"></slot>
        </el-menu-item>
        <el-menu-item index="4"  @click="dj('炸串小吃')">
          <slot name="ic4"></slot>
          <slot name="cl4"></slot>
        </el-menu-item>
        <el-menu-item index="5"  @click="dj('菜品')">
          <slot name="ic5"></slot>
          <slot name="cl5"></slot>
        </el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
</template>
<script setup>
const emit=defineEmits(['zdj'])
const dj=(name3)=>{
  emit('zdj',name3)
}
</script>
<style scoped>
* {
  margin: 0;
  padding: 0;
}

.el-menu {
  width: 160px;
  height: 675px
}

.el-menu-item {
  height: 20%;
  text-align: center;
}

.eltop {
  margin-top: 0
}
</style>
