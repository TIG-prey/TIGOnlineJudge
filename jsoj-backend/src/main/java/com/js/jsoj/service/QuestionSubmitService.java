package com.js.jsoj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.js.jsoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.js.jsoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.js.jsoj.model.entity.QuestionSubmit;
import com.js.jsoj.model.entity.User;
import com.js.jsoj.model.vo.QuestionSubmitVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author jsnlg
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2024-10-15 15:12:13
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return long
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return {@link QueryWrapper }<{@link QuestionSubmit }>
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装类
     *
     * @param questionSubmit
     * @param loginUser
     * @return {@link QuestionSubmitVO }
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return {@link Page }<{@link QuestionSubmitVO }>
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);

    // int doQuestionSubmitInner(long userId, long questionId);
}
