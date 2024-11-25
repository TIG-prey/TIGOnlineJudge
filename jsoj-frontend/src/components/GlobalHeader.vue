<template>
  <a-row id="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <div>
        <a-menu
          mode="horizontal"
          :selected-keys="selectedKeys"
          @menu-item-click="doMenuClick"
        >
          <!--Logo区域-->
          <a-menu-item
            key="0"
            :style="{ padding: 0, marginRight: '38px' }"
            disabled
          >
            <!--png展示-->
            <div class="title-bar">
              <img class="logo" src="../assets/cubic.png" alt="" />
              <div class="title">Cubic OJ</div>
            </div>
          </a-menu-item>
          <!--路由渲染-->
          <a-menu-item v-for="item in visibleRoutes" :key="item.path"
            >{{ item.name }}
          </a-menu-item>
        </a-menu>
      </div>
    </a-col>
    <!--导航栏右侧个人登录信息-->
    <a-col flex="100px">
      <div>{{ store.state.user?.loginUser?.userName ?? "未登录" }}</div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routers";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";

const router = useRouter();
// 默认主页
const selectedKeys = ref(["/"]);
// 路由跳转后，更新选中的菜单项
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});
// 过滤只需要展示的元素数组
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限展示菜单
    if (
      !checkAccess(store.state.user.loginUser, item?.meta?.access as string)
    ) {
      return false;
    }
    return true;
  });
});
//路由跳转
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const store = useStore();
console.log("GlobalHeader查看store:", store.state.user);
// console.log("GlobalHeader查看store:", store.state.user.loginUser);
// setTimeout(() => {
//   store.dispatch("getLoginUser", {
//     userName: "JS管理员",
//     userRole: ACCESS_ENUM.ADMIN,
//   });
// }, 3000);
</script>

<style>
#globalHeader {
  box-sizing: border-box;
  width: 100%;
  padding: 20px;
  background-color: var(--color-neutral-2);
}

#globalHeader .title-bar {
  display: flex;
  align-items: center;
}

.logo {
  height: 48px;
}

.title {
  color: #444;
  margin-left: 16px;
}
</style>
