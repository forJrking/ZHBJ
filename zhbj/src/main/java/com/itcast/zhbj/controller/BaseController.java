package com.itcast.zhbj.controller;

import android.content.Context;
import android.view.View;

/*
 * @创建者     Administrator
 * @创建时间   2016/2/17 23:01
 * @描述	      ${TODO}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
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
