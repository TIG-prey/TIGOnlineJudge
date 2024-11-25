package com.js.jsoj.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JianShang
 * @version 1.0.0
 * @description 判题信息消息枚举
 * @date 2024-10-17 07:52:32
 */
@Getter
public enum JudgeInfoMessageEnum {

    /**
     * ACCEPTED 成功
     */
    ACCEPTED("成功", "Accepted"),
    /**
     * Wrong Answer 答案错误
     */
    WRONG_ANSWER("答案错误", "Wrong Answer"),
    /**
     * Compile Error 编译错误
     */
    COMPILE_ERROR("编译错误", "Compile Error"),
    /**
     * Memory Limit Exceeded 内存溢出
     */
    MEMORY_LIMIT_EXCEEDED("内存溢出", "Memory Limit Exceeded"),
    /**
     * Time Limit Exceeded 超时
     */
    TIME_LIMIT_EXCEEDED("超时", "Time Limit Exceeded"),
    /**
     * Presentation Error 展示错误
     */
    PRESENTATION_ERROR("展示错误", "Presentation Error"),
    /**
     * Waiting 等待中
     */
    WAITING("Waiting", "等待中"),
    /**
     * Output Limit Exceeded 输出溢出
     */
    OUTPUT_LIMIT_EXCEEDED("输出溢出","Output Limit Exceeded"),
    /**
     * Dangerous Operation 危险操作
     */
    DANGEROUS_OPERATION( "危险操作","Dangerous Operation"),
    /**
     * Runtime Error 运行错误（用户程序的问题）
     */
    RUNTIME_ERROR( "运行错误","Runtime Error"),
    /**
     * System Error 系统错误（做系统人的问题）
     */
    SYSTEM_ERROR( "系统错误","System Error");


    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
