package com.js.jsoj.jsojcodesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.js.jsoj.jsojcodesandbox.judge.model.ExecuteMessage;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-26 04:27:50
 */
public class ProcessUtils {

    /**
     * 非交互式执行进程并获取进程信息
     *
     * @param runProcess
     * @param processState
     * @return {@link ExecuteMessage }
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String processState) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            // 开启统计执行时间
            // System.out.println("开始统计时间");
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            // 等待 Process 程序执行完，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);

            // 正常退出
            if (exitValue == 0) {
                System.out.println(processState + "成功");
                // 通过进程获取正常输出到控制台的信息
                extracted(runProcess, executeMessage);
            } else {
                // 异常退出
                System.out.println(processState + "失败，错误码：" + exitValue);
                // 通过进程获取正常输出到控制台的信息
                extracted(runProcess, executeMessage);
                // 分批获取进程的错误输出
                extractedError(runProcess, executeMessage);
            }
            // 结束计时
            stopWatch.stop();
            executeMessage.setExecutionTime(stopWatch.getLastTaskTimeMillis());
            // System.out.println("统计时间结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }

    /**
     * 获取控制台信息
     *
     * @param runProcess
     * @param executeMessage
     */
    private static void extracted(Process runProcess, ExecuteMessage executeMessage) throws IOException {
        // 通过进程获取正常输出到控制台的信息
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        // StringBuilder compileOutputStringBuilder = new StringBuilder();
        List<String> outputStrList = new ArrayList<>();
        // 逐行读取
        String compileOutputLine;
        while ((compileOutputLine = bufferedReader.readLine()) != null) {
            // compileOutputStringBuilder.append(compileOutputLine).append("\n");
            outputStrList.add(compileOutputLine);
        }
        executeMessage.setMessage(StringUtils.join(outputStrList, '\n'));
    }

    /**
     * 获取进程的错误输出
     *
     * @param runProcess
     * @param executeMessage
     */
    private static void extractedError(Process runProcess, ExecuteMessage executeMessage) throws IOException {
        BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
        List<String> errorOutputStrList = new ArrayList<>();
        // 逐行读取
        String errorCompileOutputLine;
        while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
            errorOutputStrList.add(errorCompileOutputLine);
        }
        executeMessage.setErrorMessage(StringUtils.join(errorOutputStrList, '\n'));
    }


    /**
     * 交互式执行进程并获取进程信息
     *
     * @param runProcess
     * @param args
     * @return {@link ExecuteMessage }
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String processState, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            // 从控制台输入参数
            OutputStream outputStream = runProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            String[] arguments = args.split(" ");
            String join = StrUtil.join("\n", arguments) + "\n";
            outputStreamWriter.write(join);
            // 回车，发送参数
            outputStreamWriter.flush();

            // 通过进程获取正常输出到控制台的信息
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            // 等待 Process 程序执行完，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            executeMessage.setMessage(compileOutputStringBuilder.toString());

            // 释放资源
            outputStream.close();
            outputStreamWriter.close();
            inputStream.close();
            runProcess.destroy();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
}
