import { StoreOptions } from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";
import { UserControllerService } from "../../generated";

export default {
  // 使用命名空间
  namespace: true,
  state: () => ({
    loginUser: {
      userName: "未登录",
      // userRole: null,
    },
  }),
  actions: {
    async getLoginUser({ commit, state }, payload) {
      // 从远程获取登录信息;
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 0) {
        commit("updateUser", res.data);
        console.log("从远程获取的登录信息为：", res.data);
      } else {
        commit("updateUser", {
          ...state.loginUser,
          userRole: ACCESS_ENUM.NOT_LOGIN,
        });
        // console.log("getLoginUser执行了");
      }
    },
  },
  mutations: {
    updateUser(state, payload) {
      // console.log("更新前的loginUser", state.loginUser);
      // console.log("mutations的updateUser执行的参数payload：", payload);
      state.loginUser = payload;
      // console.log(
      //   "mutations的updateUser执行，更新后的loginUser:",
      //   state.loginUser
      // );
    },
  },
} as StoreOptions<any>;
