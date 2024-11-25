package com.js.jsoj.judge.codesandbox;

import com.js.jsoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.js.jsoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
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
