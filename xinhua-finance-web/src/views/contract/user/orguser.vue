<template>
  <basic-container>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="机构用户管理" name="first">
      <mechanism-user   />
    </el-tab-pane>
    <el-tab-pane label="个人注册用户" name="second">
      <personal-user></personal-user>
    </el-tab-pane>
  </el-tabs>
  </basic-container>
</template>

<script>
  import mechanismUser from "./mechanismUser";
  import personalUser from "./personal/personalUser";
  export default {
    components: {
      mechanismUser,personalUser
    },
    data() {
      return{
        activeName: 'first',
      }
    },
    computed: {
    },
    methods: {
    }
  };
</script>

<style>
</style>
