package com.js.jsoj.model.dto.question;

import lombok.Data;

/**
 * @author JianShang
 * @version 1.0.0
 * @description 题目配置
 * @date 2024-10-15 04:07:33
 */
@Data
public class JudgeConfig {
/*     {
        "timeLimit": 1000,
        "memoryLimit": 1000,
        "stackLimit": 1000
    } */

    /**
     * 时间限制(ms)
     */
    private Long timeLimit;
    /**
     *内存限制(KB)
     */
    private Long memoryLimit;
    /**
     * 堆栈限制
     */
    private Long stackLimit;
}
