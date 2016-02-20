package com.itcast.zhbj.controller.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itcast.zhbj.controller.BaseController;

/*
 * @创建者     Administrator
 * @创建时间   2016/2/18 19:55
 */
public class PicMenuController extends BaseController {
    public PicMenuController(Context context) {
        super(context);
    }

    @Override
    public View initView(Context context) {
        TextView tv = new TextView(context);

        tv.setText("组图");
        tv.setTextColor(Color.RED);
        tv.setTextSize(25);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }
}
