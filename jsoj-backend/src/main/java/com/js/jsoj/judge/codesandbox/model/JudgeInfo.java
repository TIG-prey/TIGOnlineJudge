package com.js.jsoj.judge.codesandbox.model;

import lombok.Data;

/**
 * 题目判断信息
 * @author JianShang
 * @version 1.0.0
 * @date 2024-10-15 04:13:35
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;
    /**
     * 消耗内存(KB)
     */
    private Long memory;
    /**
     * 消耗时间(ms)
     */
    private Long time;
}
