package com.itcast.zhbj.controller;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.itcast.zhbj.R;
import com.itcast.zhbj.activity.MainUI;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/*
 * @创建者     Administrator
 * @创建时间   2016/2/17 23:37
 * @描述	      ${TODO}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public abstract class TabController extends BaseController {

    protected ImageView mIvMenu;
    protected TextView mTVTitle;
    protected FrameLayout mFlContainer;

    public TabController(Context context) {
        super(context);
    }

    @Override
    public View initView(Context context) {
        //填充布局
        View view = View.inflate(context, R.layout.tab_controller, null);
        mIvMenu = (ImageView) view.findViewById(R.id.tab_iv_menu);
        mTVTitle = (TextView) view.findViewById(R.id.tab_tv_title);
        mFlContainer = (FrameLayout) view.findViewById(R.id.tab_fl_container);
        //添加额外不同视图部分
        mFlContainer.addView(initContentView(context));

        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开关闭 菜单
                SlidingMenu menu = ((MainUI) mContext).getSlidingMenu();
                menu.toggle();
            }
        });
        return view;
    }

    protected abstract View initContentView(Context context);

    /**
     * 有菜单的子类复写这个方法
     *
     * @param position
     */
    public void changeMenu(int position) {

    }
}
