package com.js.jsoj.jsojcodesandbox.judge.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.WordTree;
import com.js.jsoj.jsojcodesandbox.judge.CodeSandBox;
import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteCodeRequest;
import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteCodeResponse;
import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteMessage;
import com.js.jsoj.jsojcodesandbox.judge.model.JudgeInfo;
import com.js.jsoj.jsojcodesandbox.utils.ProcessUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Java代码沙箱实现
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-26 02:26:48
 */
@Component
public class JavaNativeCodeSandBox extends JavaCodeSandBoxTemplate {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
