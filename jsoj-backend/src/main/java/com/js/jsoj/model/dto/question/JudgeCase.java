package com.js.jsoj.model.dto.question;

import lombok.Data;

/**
 * @author JianShang
 * @version 1.0.0
 * @description 题目用例
 * @date 2024-10-15 04:07:37
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
