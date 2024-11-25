import router from "@/router";
import store from "@/store";
import ACCESS_ENUM from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";

router.beforeEach(async (to, from, next) => {
  console.log("登录用户信息：", store.state.user.loginUser);
  let loginUser = store.state.user.loginUser;
  // 如果之前没登录过，自动登录
  if (!loginUser || !loginUser.userRole) {
    console.log("没有登录，将自动登录");
    // 加await是为了等用户登录成功后，再执行后续代码
    await store.dispatch("getLoginUser");
    loginUser = store.state.user.loginUser;
  }
  // console.log(loginUser);
  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN;
  // 要跳转的页面必须登录
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    // 如果没登录，跳转到登录页面
    if (!loginUser || !loginUser.userRole) {
      console.log("执行了第一个if");
      // console.log("loginUser中的参数：", loginUser);
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    // 如果已经登录了，但是权限不足，那么跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      console.log("执行了第二个if");
      // console.log(!checkAccess(loginUser, needAccess));
      next("/noAuth");
      return;
    }
  }
  next();
});
