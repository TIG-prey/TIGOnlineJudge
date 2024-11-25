package com.js.jsoj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JianShang
 * @version 1.0.0
 * @description
 * @date 2024-10-24 10:40:45
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ExecuteCodeRequest {

    /**
     * 输入用例
     */
    private List<String> inputList;

    /**
     * 提交的代码
     */
    private String code;

    /**
     * 选择的编程语言
     */
    private String language;
}
