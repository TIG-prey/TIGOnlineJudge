package com.js.jsoj.jsojcodesandbox.judge;

import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteCodeRequest;
import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-24 11:08:19
 */
public interface CodeSandBox {
    /**
     * 代码沙箱执行代码接口
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
