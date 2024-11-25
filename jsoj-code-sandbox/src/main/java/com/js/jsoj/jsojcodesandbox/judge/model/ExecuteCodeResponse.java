package com.js.jsoj.jsojcodesandbox.judge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码沙箱响应信息
 *
 * @author JianShang
 * @version 1.0.0
 * @date 2024-10-24 10:40:45
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ExecuteCodeResponse {

    /**
     * 输出用例
     */
    private List<String> outputList;

    /**
     * 执行的信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
