<template>
  <div class="avue-logo">
    <transition name="fade" v-if="false">
      <span v-if="keyCollapse"
            class="avue-logo_subtitle"
            key="0">
        <img src="/img/logo-xhcj.png"
             style="width: 140px;text-align: center;margin-left: 10px;"/>
      </span>
    </transition>
    <transition-group name="fade">
      <template>
        <span class="avue-logo_title" key="1">
          <div>
            <img :src="website.logo">
          </div>
        </span>
      </template>
    </transition-group>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "logo",
  data() {
    return {};
  },
  created() {
  },
  computed: {
    ...mapGetters(["website", "keyCollapse"])
  },
  methods: {}
};
</script>

<style lang="scss">
.fade-leave-active {
  transition: opacity 0.2s;
}
.fade-enter-active {
  transition: opacity 2.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.avue-logo {
  position: fixed;
  top:0.4rem;
  left:1.81rem;
  width: 21rem;
  background-color: #20222a;
  font-size: 20px;
  overflow: hidden;
  color: rgba(255, 255, 255, 0.8);
  z-index: 1024;
  &_title {
    display: block;
    text-align: center;
    font-weight: 300;
    font-size: 16px;
    div {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      img {
        vertical-align: middle;
        width: 100%;
        height: auto;
      }
    }
  }
  &_subtitle {
    padding-top: 10px;
    display: block;
    text-align: center;
    font-size: 18px;
    font-weight: bold;
    color: #fff;
  }
}
</style>
