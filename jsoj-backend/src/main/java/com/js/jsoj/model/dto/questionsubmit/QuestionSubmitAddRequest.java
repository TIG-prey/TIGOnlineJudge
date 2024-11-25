package com.js.jsoj.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * @author JianShang
 * @version 1.0.0
 * @description 添加 题目提交
 * @date 2024-10-16 09:26:13
 * */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目Id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}