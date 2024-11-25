<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <!--头部区域-->
      <a-layout-header class="header">
        <a-space>
          <img src="../assets/cubic.png" class="logo" />
          <div>Cubic OJ</div>
        </a-space>
        <!--<GlobalHeader />-->
      </a-layout-header>
      <!--内容展示区-->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <!--底部footer区域-->
      <a-layout-footer class="footer">
        <a href="https://github.com/TIG-prey" target="_blank">
          程序员 JianShang 出品
        </a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>
<script>
import { defineComponent } from "vue";
import GlobalHeader from "@/components/GlobalHeader.vue";

export default defineComponent({
  components: {},
});
</script>

<style scoped>
#userLayout {
  text-align: center;
}

#userLayout .logo {
  width: 64px;
  height: 64px;
}

#userLayout .header {
  margin-top: 16px;
}

#userLayout .content {
  margin-bottom: 16px;
  padding: 20px;
  min-height: 50vh;
}

#userLayout .footer {
  background: #efefef;
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
