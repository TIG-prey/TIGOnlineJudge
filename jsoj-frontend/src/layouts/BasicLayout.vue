<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <!--头部区域-->
      <a-layout-header class="header">
        <GlobalHeader />
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
  components: { GlobalHeader },
});
</script>

<style scoped>
#basicLayout {
}

#basicLayout .header {
  margin-bottom: 16px;
  box-shadow: #eee 1px 1px 5px;
}

#basicLayout .content {
  background: linear-gradient(to right, #bbb, #fff);
  margin-bottom: 16px;
  padding: 20px;
}

#basicLayout .footer {
  background: #efefef;
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
