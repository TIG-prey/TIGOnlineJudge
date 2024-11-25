package com.js.jsoj.jsojcodesandbox.judge.model;

import lombok.Data;

/**
 * 进程执行信息
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-26 04:29:54
 */
@Data
public class ExecuteMessage {
    /**
     * 状态码
     */
    private Integer exitValue;
    /**
     * 返回的信息
     */
    private String message;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 执行时间
     */
    private Long executionTime;
}
