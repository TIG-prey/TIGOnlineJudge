package com.js.jsoj.judge.codesandbox;

import com.js.jsoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.js.jsoj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 沙箱代理
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-24 02:27:30
 */
@Slf4j
@RequiredArgsConstructor
public class CodeSandBoxProxy implements CodeSandBox {

    private final CodeSandBox codeSandbox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}