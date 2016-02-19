package com.itcast.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchedViewPager extends ViewPager {

    public TouchedViewPager(Context context) {
        super(context);
    }

    public TouchedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int position = getCurrentItem();
        int count = getAdapter().getCount();
        float downX = 0;
        float dowmY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                dowmY = ev.getY();

                //请求父类不拦截子类请求
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();

                if (Math.abs(downX - moveX) > Math.abs(dowmY - moveY)) {
                    //0页面打开menu 拦截
                    if (position == 0 && moveX > downX) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (position == count - 1 && moveX < downX) {
                        //最后页面
                        //请求父类拦截子类请求
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        //请求父类不拦截子类请求
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
