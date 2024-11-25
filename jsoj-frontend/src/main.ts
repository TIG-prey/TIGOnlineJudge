import { createApp } from "vue";
import App from "./App.vue";
import "@arco-design/web-vue/dist/arco.css";
import ArcoVue from "@arco-design/web-vue";
import router from "./router";
import store from "./store";
import "@/plugins/axios";
import "@/access/index";
import "bytemd/dist/index.css";
// import "@betemd/plugin-gemoji";

createApp(App).use(ArcoVue).use(store).use(router).mount("#app");
