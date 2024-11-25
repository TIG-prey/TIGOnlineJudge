<template>
  <Editor
    :value="value"
    :mode="mode"
    :plugins="plugins"
    :locale="zhHans"
    @change="handleChange"
    :gemoji="gemoji"
  >
    <!--    <Viewer
          class="viewer"
          :tabindex="2"
          :sanitize="23"
          :value="value"
          :plugins="plugins"
          :locale="zhHans"
        ></Viewer>-->
  </Editor>
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer } from "@bytemd/vue-next";
import { withDefaults, defineProps } from "vue";
import zhHans from "bytemd/locales/zh_Hans.json";
import gemoji from "@bytemd/plugin-gemoji";

/**
 * 定义组件属性类型，外层传入数据
 */
interface Props {
  value: string;
  mode?: string;
  handleChange: (v: string) => void;
}

const plugins = [
  gfm(),
  highlight(),
  // Add more plugins here
];
/**
 * 属性默认值
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  mode: () => "split",
  handleChange: (v: string) => {
    console.log(v);
    // return "";
  },
});
</script>

<style>
.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child {
  display: none;
}

/*.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:nth-child(2) {
  display: none;
}*/
</style>
