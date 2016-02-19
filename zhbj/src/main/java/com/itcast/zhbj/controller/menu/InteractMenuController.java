package com.itcast.zhbj.controller.menu;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itcast.zhbj.controller.BaseController;

public class InteractMenuController extends BaseController {
    public InteractMenuController(Context context) {
        super(context);
    }

    @Override
    public View initView(Context context) {
//        TextView tv = new TextView(context);
//
//        tv.setText("互动");
//        tv.setTextColor(Color.RED);
//        tv.setTextSize(25);
//        tv.setGravity(Gravity.CENTER);

        View view = new View(mContext);
//        ButterKnife.bind();
        return view;
    }

    @Override
    public void initData() {
        ViewPager mPager = new ViewPager(mContext);
        mPager.setAdapter(new InteractAdapter());
    }

    private class InteractAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //List --> adapter
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //position 选中的view标记
            //添加view
            TextView tv = new TextView(mContext);
            int i = 0;
            tv.setText("页面"+(i++));
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.RED);
            container.addView(tv);
            //返回标记
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
