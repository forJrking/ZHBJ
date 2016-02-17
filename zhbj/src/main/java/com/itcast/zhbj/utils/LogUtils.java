package com.itcast.zhbj.utils;

public class LogUtils {
    // debug调试的工具类 项目结束需要修改标记
    private static boolean isLog = true;

    public static void p(String log) {
        if (isLog) {
            System.out.println(log);
        }
    }
}
