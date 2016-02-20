package com.itcast.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchedViewPager extends ViewPager {
    private float mDownX;
    private float mDownY;

    public TouchedViewPager(Context context) {
        super(context);
    }

    public TouchedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int position = getCurrentItem();
        int count    = getAdapter().getCount();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();

                //请求父容器不要拦截touch事件
                //true：不要拦截
                //false: 拦截
                getParent().requestDisallowInterceptTouchEvent(true);

                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();

                //水平滑动时
                if (Math.abs(mDownX - moveX) > Math.abs(mDownY - moveY)) {
                    //  从左往右滑动(moveX > mDownX)
                    //  从右往左滑动(moveX < mDownX)
                    if (position == 0 && moveX > mDownX) {
                        //如果在第0个页面，从左往右滑动，slidingmenu打开-->父容器响应touch-->让父容器拦截touch
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (position == count - 1 && moveX < mDownX) {
                        //如果是在最后一个页面，从右往左滑动，滑动到下一页-->外侧viewpager响应touch-->让父容器拦截touch
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        //其他情况，自己响应touch--->请求父容器不要拦截touch事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    //父容器响应
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
