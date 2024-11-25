package com.js.jsoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.js.jsoj.model.dto.question.JudgeCase;
import com.js.jsoj.model.dto.question.JudgeConfig;
import com.js.jsoj.judge.codesandbox.model.JudgeInfo;
import com.js.jsoj.model.entity.Question;
import com.js.jsoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * 判题策略默认实现
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-24 04:12:05
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    /**
     * 判题
     *
     * @param judgeContext
     * @return {@link JudgeInfo }
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        // 从 上下文 中获取信息
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        // 题目的输入
        List<String> inputList = judgeContext.getInputList();
        // 沙箱的输出
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();
        // 定义返回结果
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        // 5.根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        //  先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            return getJudgeInfo(judgeInfoResponse, judgeInfoMessageEnum);
        }
        // 5.1 判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            // 获取题目的输出输入示例
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                return getJudgeInfo(judgeInfoResponse, judgeInfoMessageEnum);
            }
        }
        // 5.2 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        if (memory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            return getJudgeInfo(judgeInfoResponse, judgeInfoMessageEnum);
        }
        if (time > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            return getJudgeInfo(judgeInfoResponse, judgeInfoMessageEnum);
        }
        return getJudgeInfo(judgeInfoResponse, judgeInfoMessageEnum);
    }

    /**
     * 封装返回结果 方法
     *
     * @param judgeInfoResponse    返回的结果
     * @param judgeInfoMessageEnum 对应的枚举信息
     * @return {@link JudgeInfo }
     */
    private static JudgeInfo getJudgeInfo(JudgeInfo judgeInfoResponse, JudgeInfoMessageEnum judgeInfoMessageEnum) {
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
