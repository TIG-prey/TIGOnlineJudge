<template>
  <div class="home">
    <CodeEditor />
    <MdEditor :value="value" :handle-change="onChange" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import CodeEditor from "@/components/CodeEditor.vue"; // @ is an alias to /src

const value = ref("");

const onChange = (v: string) => {
  value.value = v;
};
</script>
