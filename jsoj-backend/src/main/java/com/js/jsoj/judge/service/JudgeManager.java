package com.js.jsoj.judge.service;

import com.js.jsoj.judge.strategy.DefaultJudgeStrategy;
import com.js.jsoj.judge.strategy.JavaLanguageJudgeStrategy;
import com.js.jsoj.judge.strategy.JudgeContext;
import com.js.jsoj.judge.strategy.JudgeStrategy;
import com.js.jsoj.judge.codesandbox.model.JudgeInfo;
import com.js.jsoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-24 04:48:21
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return {@link JudgeInfo }
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
