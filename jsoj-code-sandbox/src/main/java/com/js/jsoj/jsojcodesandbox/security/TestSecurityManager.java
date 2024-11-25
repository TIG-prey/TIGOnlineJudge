package com.js.jsoj.jsojcodesandbox.security;

import cn.hutool.core.io.FileUtil;

import java.nio.charset.Charset;

public class TestSecurityManager {
    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());

        // List<String> strings = FileUtil.readLines("L:\\JSOJ\\jsoj-code-sandbox\\src\\main\\resources\\木马程序.bat", StandardCharsets.UTF_8);
        // System.out.println(strings);

        FileUtil.writeString("aa","aaa", Charset.defaultCharset());

    }
}
