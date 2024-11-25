package com.js.jsoj.model.dto.questionsubmit;

import com.js.jsoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author JianShang
 * @version 1.0.0
 * @description 查询 题目提交
 * @date 2024-10-15 03:35:48
 * */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目ID
     */
    private Long questionId;
    /**
     * 提交的语言
     */
    private String language;
    /**
     * 提交状态
     */
    private Integer status;
    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}