package com.itcast.zhbj.controller;

import android.content.Context;
import android.view.View;

public abstract class BaseController {
    protected View mRootView;
    protected Context mContext;

    public BaseController(Context context){
        this.mContext = context;
        mRootView = initView(context);
    }

    /**
     * 初始化界面方法
     * @param context
     * @return
     */
    public abstract View initView(Context context);

    public View getRootView() {
        return mRootView;
    }

    /**
     * 初始化数据方法
     */
    public void initData(){

    }
}
