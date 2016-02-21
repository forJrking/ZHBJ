package com.itcast.zhbj.constant;

public interface Constants {

    /**
     * 引导页加载记录
     */
    String SPLASH_FIRST_USE = "splash_first_use";

    /**
     * 新闻中心的url
     */
    String BASE_URL = "http://188.188.7.167:8080/zhbj";

    //genymotion的向导ip
    //String BASE_URL         = "http://192.168.56.1:8080/zhbj";

    String NEWSCENTER_URL = BASE_URL + "/categories.json";
    //手机端真实url
    String REPLACE_OLD = "http://10.0.2.2:8080/zhbj";
    //测试 url
    String REPLACE_NEW = BASE_URL;

    /**
     * 延时校验时间
     */
    long DATA_DELAY = 10 * 1000;
    //文字大小
    String KEY_TEXTSIZE = "textsize";
}
