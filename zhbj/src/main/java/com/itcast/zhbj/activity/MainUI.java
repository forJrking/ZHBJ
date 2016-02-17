package com.itcast.zhbj.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.itcast.zhbj.R;
import com.itcast.zhbj.fragment.ContentFragment;
import com.itcast.zhbj.fragment.MenuFragment;
import com.itcast.zhbj.utils.DensityUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainUI extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //设置菜单部分
        setBehindContentView(R.layout.main_menu);
//        设置主界面部分
        setContentView(R.layout.main_content);
//        设置菜单
        SlidingMenu menu = getSlidingMenu();
        //全屏触摸滑出菜单
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //左侧滑出
        menu.setMode(SlidingMenu.LEFT);
        //设置菜单宽度
        menu.setBehindWidth(DensityUtils.dp2px(this,120));
        //设置阴影
        //menu.setShadowWidth(DensityUtils.dp2px(this,120));
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindScrollScale(0.5f);
        menu.setFadeDegree(0.5f);
        //加载菜单
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
       // FragmentManager fm = getFragmentManager();

       FragmentTransaction transaction = fm.beginTransaction();

        transaction.replace(R.id.main_menu_container,new MenuFragment());
        transaction.replace(R.id.main_content_container,new ContentFragment());

        transaction.commit();
    }
}
