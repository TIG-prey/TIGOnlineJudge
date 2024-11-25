package com.js.jsoj.judge.service;

import com.js.jsoj.model.entity.QuestionSubmit;

/**
 * 判题服务
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-24 02:44:33
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param questionSubmitId 题目提交的id
     * @return {@link QuestionSubmit }
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
