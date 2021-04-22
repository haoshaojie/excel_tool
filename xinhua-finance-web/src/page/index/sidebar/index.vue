<template>
  <div class="avue-sidebar">
    <div class="top-bar__left">
      <div class="avue-breadcrumb"
          :class="[{ 'avue-breadcrumb--active': isCollapse }]"
          v-if="showCollapse">
        <i class="el-icon-s-unfold" @click="setCollapse"></i>
      </div>
    </div>
    <logo v-if="false"></logo>
    <el-scrollbar style="height:100%">
      <div v-if="validatenull(menu)"
           class="avue-sidebar--tip">{{$t('menuTip')}}</div>
      <el-menu unique-opened
               :default-active="nowTagValue"
               mode="vertical"
               :show-timeout="200"
               :collapse="keyCollapse">
        <sidebar-item :menu="menu"
                      :screen="screen"
                      first
                      :props="website.menu.props"
                      :collapse="keyCollapse"></sidebar-item>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters,mapState } from "vuex";
import logo from "../logo";
import sidebarItem from "./sidebarItem";
export default {
  name: "sidebar",
  components: { sidebarItem, logo },
  data() {
    return {};
  },
  created() {
    this.$store.dispatch("GetMenu").then(data => {
      if (data.length === 0) return;
      this.$router.$avueRouter.formatRoutes(data, true);
    });
  },
  computed: {
    ...mapGetters(["website", "menu", "tag", "keyCollapse", "screen","isCollapse"]),
    ...mapState({
        showCollapse: state => state.common.showCollapse
      }),
    nowTagValue: function() {
      return this.$router.$avueRouter.getValue(this.$route);
    }
  },
  mounted() {},
  methods: {
    setCollapse() {
      this.$store.commit("SET_COLLAPSE");
    },
  }
};
</script>
<style lang="scss" scoped>
</style>

