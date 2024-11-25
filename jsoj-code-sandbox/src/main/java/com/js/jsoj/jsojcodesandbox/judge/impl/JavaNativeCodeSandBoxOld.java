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
public class JavaNativeCodeSandBoxOld implements CodeSandBox {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final Long TIME_OUT = 3000L;

    // 黑名单
    private static final List<String> BLACK_LIST = Arrays.asList("Files", "exec");

    private static final WordTree WORD_TREE;

    private static final String SECURITY_MANAGER_PATH = "L:\\JSOJ\\jsoj-code-sandbox\\src\\main\\resources\\security";

    private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";

    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(BLACK_LIST);
    }

    public static void main(String[] args) {
        JavaNativeCodeSandBoxOld javaNativeCodeSandBox = new JavaNativeCodeSandBoxOld();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        // Scanner inputScanner = new Scanner(System.in);
        // List<String> inputList = new ArrayList<>();
        // // 读取两行数据
        // for (int i = 0; i < 2; i++) {
        //     String line = inputScanner.nextLine();
        //     inputList.add(line);
        // }
        // // 关闭 Scanner 对象
        // inputScanner.close();
        // executeCodeRequest.setInputList(inputList);
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        String code = ResourceUtil.readStr("test/unsafe/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.executeCode(executeCodeRequest);
        System.out.println("返回结果：" + executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        // 校验代码是否包含有黑名单中命令
        // FoundWord foundWord = WORD_TREE.matchWord(code);
        // if (foundWord != null) {
        //     System.out.println("此文件包含敏感词：" + foundWord.getFoundWord());
        //     return null;
        // }

        // 1. 把用户的代码保存为文件
        // 获取用户工作文件路径，即(项目的根目录)
        String userDir = System.getProperty("user.dir");
        //  File.separator 区分不同系统的分隔符：\\ or /
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局目录路径是否存在
        if (!FileUtil.exist(globalCodePathName)) {
            // 不存在，则创建文件目录
            FileUtil.mkdir(globalCodePathName);
        }
        // 存在，则保存用户提交代码
        // 把用户的代码隔离存放，结尾不用 .toString() 是因为字符串拼接的时候已经默认执行了
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        // 实际存放文件的目录
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        // 将代码按UTF-8格式写入文件中
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        // 编译代码，得到 class文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            // 通过process执行终端命令
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println("编译结束：" + executeMessage);
        } catch (IOException e) {
            return getResponse(e);
        }

        // 3. 执行代码，得到输出结果
        // 收集执行结果信息
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath,
                    SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            try {



                // 通过process执行终端命令
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 超时控制
                // new Thread(() -> {
                //     try {
                //         System.out.println("开始超时控制");
                //         Thread.sleep(TIME_OUT);
                //         System.out.println("超时了，中断");
                //         runProcess.destroy();
                //     } catch (InterruptedException e) {
                //         throw new RuntimeException(e);
                //     }
                // // 启动线程
                // }).start();
                // System.out.println("继续执行");
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                // ExecuteMessage executeMessage = ProcessUtils.runInteractProcessAndGetMessage(runProcess, "执行", inputArgs);
                System.out.println("执行结束：" + executeMessage);
                executeMessageList.add(executeMessage);
            } catch (IOException e) {
                return getResponse(e);
            }
        }
        // 4、整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，便于判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            // 是否存在 执行错误信息
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 执行 存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long executeTime = executeMessage.getExecutionTime();
            if (executeTime != null) {
                maxTime = Math.max(maxTime, executeTime);
            }
        }
        // 正常执行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);

        // 5. 文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }

        return executeCodeResponse;
    }

    /**
     * 获取错误响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 代码沙箱错误，编译错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
