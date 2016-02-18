package com.itcast.zhbj.controller.tab;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itcast.zhbj.controller.TabController;


public class HomeController extends TabController {
    public HomeController(Context context) {
        super(context);
    }

    @Override
    public View initContentView(Context context) {
        TextView tv = new TextView(context);

        tv.setText("首页");
        tv.setTextColor(Color.RED);
        tv.setTextSize(25);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }

    @Override
    public void initData() {

        mTVTitle.setText("首页");
        mIvMenu.setVisibility(View.GONE);
    }
}
