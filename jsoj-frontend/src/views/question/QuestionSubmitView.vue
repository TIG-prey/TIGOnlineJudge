<template>
  <div id="questionSubmitView">
    <a-form :model="searchParams" layout="inline" style="margin-left: 300px">
      <a-form-item field="questionId" label="题号" tooltip="请输入题目Id">
        <a-input
          v-model="searchParams.questionId"
          placeholder="请输入搜索题号"
        />
      </a-form-item>
      <a-form-item
        field="submitLanguage"
        label="编程语言："
        style="min-width: 240px"
      >
        <a-select v-model="searchParams.language" placeholder="选择编程语言">
          <a-option>java</a-option>
          <a-option>cpp</a-option>
          <a-option>go</a-option>
          <a-option>python</a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="outline" shape="round" status="normal" @click="doSubmit"
          >搜索
        </a-button>
      </a-form-item>
      <a-form-item>
        <a-button
          type="primary"
          shape="round"
          status="success"
          @click="loadData"
          >刷新
        </a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
      column-resizable
      wrapper
      :ref="tableRef"
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
        showJumper: true,
        showPageSize: true,
      }"
      @page-change="onPageChange"
      @pageSizeChange="onPageSizeChange"
    >
      <template #judgeInfo="{ record }">
        <a-space wrap>
          <a-tag
            size="medium"
            v-for="(info, index) of record.judgeInfo"
            :key="index"
            color="orange"
          >
            {{
              `${
                index === "message"
                  ? "结果"
                  : index === "time"
                  ? "耗时"
                  : "消耗内存"
              }`
            }}
            {{ "：" + info }}
          </a-tag>
        </a-space>
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #questionId="{ record }">
        <a-link status="success" @click="toQuestionPage(record)"
          >{{ record.questionId }}
        </a-link>
      </template>
      <template #status="{ record }">
        <!--判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）-->
        <a-tag v-if="record.status === 0" color="cyan">待判题</a-tag>
        <a-tag v-if="record.status === 1" color="green">判题中</a-tag>
        <a-tag v-if="record.status === 2" color="blue">成功</a-tag>
        <a-tag v-if="record.status === 3" color="red">失败</a-tag>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import {
  Question,
  QuestionControllerService,
  QuestionSubmitQueryRequest,
} from "../../../generated";
import moment from "moment";

const tableRef = ref();
const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionSubmitQueryRequest>({
  title: "",
  tags: [],
  pageSize: 10,
  current: 1,
});

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost(
    searchParams.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("加载失败，" + res.message);
  }
};

/**
 * 监听 searchParams 变量，改变时触发页面的重新加载
 */
watchEffect(() => {
  loadData();
});

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const columns = [
  {
    title: "提交号",
    dataIndex: "id",
    align: "center",
  },
  {
    title: "题号",
    dataIndex: "questionId",
    align: "center",
  },
  {
    title: "提交者",
    dataIndex: "userId",
    align: "center",
  },
  {
    title: "判题信息",
    slotName: "judgeInfo",
    align: "center",
  },
  {
    title: "编程语言",
    dataIndex: "language",
    align: "center",
  },
  {
    title: "提交状态",
    slotName: "status",
    align: "center",
  },
  {
    title: "创建时间",
    slotName: "createTime",
    align: "center",
  },
];

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

/**
 * 分页大小
 * @param size
 */
const onPageSizeChange = (size: number) => {
  searchParams.value = {
    ...searchParams.value,
    pageSize: size,
  };
};

const router = useRouter();

/**
 * 跳转到做题页面
 * @param question
 */
const toQuestionPage = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};
/*
 * 确认搜索,重新加载数据
 * */
const doSubmit = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
</script>

<style scoped>
#questionSubmitView {
  max-width: 1350px;
  margin: 0 auto;
}
</style>
