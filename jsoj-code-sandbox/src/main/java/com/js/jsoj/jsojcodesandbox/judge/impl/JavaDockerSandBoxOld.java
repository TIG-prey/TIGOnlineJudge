package com.js.jsoj.jsojcodesandbox.judge.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.js.jsoj.jsojcodesandbox.judge.CodeSandBox;
import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteCodeRequest;
import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteCodeResponse;
import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteMessage;
import com.js.jsoj.jsojcodesandbox.judge.model.JudgeInfo;
import com.js.jsoj.jsojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Java代码沙箱实现
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-26 02:26:48
 */
@Slf4j
public class JavaDockerSandBoxOld implements CodeSandBox {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final Long TIME_OUT = 5L;

    private static final String SECURITY_MANAGER_PATH = "L:\\JSOJ\\jsoj-code-sandbox\\src\\main\\resources\\security";

    private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";

    private static final Boolean FIRST_INIT = true;

    public static void main(String[] args) {
        JavaDockerSandBoxOld javaNativeCodeSandBox = new JavaDockerSandBoxOld();
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
        String code = ResourceUtil.readStr("test/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
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

        // 2.编译代码，得到 class文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            // 通过process执行终端命令
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println("编译结束：" + executeMessage);
        } catch (IOException e) {
            return getResponse(e);
        }

        // 3、创建容器，上传编译文件到容器内
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        // 3.1、拉取镜像
        String image = "openjdk:8-alpine";
        // 检查镜像是否已存在
        if (!isImagePresent(dockerClient, image)) {
            // 拉取镜像
            if (FIRST_INIT) {
                PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
                PullImageResultCallback resultCallback = new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        System.out.println("拉取镜像：" + item.getStatus());
                        super.onNext(item);
                    }
                };
                try {
                    pullImageCmd.exec(resultCallback).awaitCompletion();
                    System.out.println("成功拉取镜像");
                } catch (InterruptedException e) {
                    System.out.println("拉取镜像异常");
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("镜像已存在，无需拉取");
        }

        // 3.2、创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        // 用于存储容器的主机配置
        HostConfig hostConfig = new HostConfig();
        String profileConfig = ResourceUtil.readUtf8Str("profile.json");
        // 开启安全机制
        hostConfig.withSecurityOpts(Arrays.asList("seccomp=" + profileConfig));
        // 限制内存
        hostConfig.withMemory(100 * 1000 * 1000L);
        // 容器可以使用的最大交换空间大小,
        // 0L：表示不设置交换空间限制，即容器可以使用无限的交换空间,
        // -1L：表示交换空间限制与内存限制相同
        // 正值：表示设置一个具体的交换空间限制
        hostConfig.withMemorySwap(0L);
        // 设置CPU
        hostConfig.withCpuCount(1L);
        // 设置容器挂载目录
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/app")));
        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withAttachStderr(true)
                .withAttachStdin(true)  // 开启输入输出
                .withAttachStdout(true)
                .withTty(true) // 开启一个交互终端
                .exec();
        String containerId = createContainerResponse.getId();
        System.out.println("创建的容器id：" + containerId);

        // 4. 启动容器
        dockerClient.startContainerCmd(containerId).exec();
        // docker exec affectionate_robinson     java -cp /app Main     1 3
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArgsArray);
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建执行命令：" + execCreateCmdResponse);
            // 记录执行时间
            long time = 0L;
            StopWatch stopWatch = new StopWatch();
            // 记录执行信息
            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};
            // 判断程序是正常执行还是超时执行
            final boolean[] timeout = {true};
            String execId = execCreateCmdResponse.getId();
            // dockerClient的exec方法 接收一个异步执行结果
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    // 如果程序执行完成则说明不是超时
                    timeout[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    // 如何区分正常输出和异常输出
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        // 尽管数组的内容被修改了，但 message 变量本身仍然指向同一个数组对象。只是数组内部的元素被改变了
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("输出错误结果:" + errorMessage[0]);
                    } else {
                        // 程序正常输出的情况下
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果：" + message[0]);
                    }
                    super.onNext(frame);
                }
            };
            // 获取占用的内存
            final long[] maxMemory = {0L};
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {
                @Override
                public void onNext(Statistics statistics) {
                    System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });
            statsCmd.exec(statisticsResultCallback);
            try {
                // 开始计时
                stopWatch.start();
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        .awaitCompletion(TIME_OUT, TimeUnit.SECONDS);
                // 结束计时
                stopWatch.stop();
                time = stopWatch.getLastTaskTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }
            // 若  timeout 为true 则说明程序执行超时
            if (timeout[0]) {
                ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
                executeCodeResponse.setOutputList(new ArrayList<>());
                // 程序执行状态设置为异常
                executeCodeResponse.setStatus(2);
                executeCodeResponse.setMessage("程序执行超时");
                return executeCodeResponse;
            }
            // 一次程序执行完成，收集执行信息
            // 执行状态
            executeMessage.setExitValue(1);
            // 执行结果
            executeMessage.setMessage(message[0]);
            // 若有错误信息,则记录错误信息
            executeMessage.setErrorMessage(errorMessage[0]);
            // 执行时间
            executeMessage.setExecutionTime(time);

            executeMessageList.add(executeMessage);
        }

        // 5、整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        // 用于收集返回输出结果
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，便于判断是否超时
        long maxTime = 0;
        // 判断程序执行信息中是否有错误信息
        for (ExecuteMessage executeMessage : executeMessageList) {
            // 是否存在 执行错误信息
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 执行 存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            // 若不存在错误,则将执行结果添加到返回信息中
            outputList.add(executeMessage.getMessage());
            Long executeTime = executeMessage.getExecutionTime();
            if (executeTime != null) {
                maxTime = Math.max(maxTime, executeTime);
            }
        }
        // 正常执行完成
        if (outputList.size() == executeMessageList.size()) {
            // 正常
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);

        // 6. 文件清理
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

    /**
     * 检查指定的镜像是否存在于 Docker 中
     *
     * @param dockerClient Docker 客户端
     * @param imageName    镜像名称
     * @return 如果镜像存在返回 true，否则返回 false
     */
    private static boolean isImagePresent(DockerClient dockerClient, String imageName) {
        List<Image> images = dockerClient.listImagesCmd().exec();
        for (Image image : images) {
            for (String tag : image.getRepoTags()) {
                if (tag.equals(imageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
