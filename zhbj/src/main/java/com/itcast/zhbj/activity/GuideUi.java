package com.itcast.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.itcast.zhbj.R;
import com.itcast.zhbj.constant.Constants;
import com.itcast.zhbj.utils.DensityUtils;
import com.itcast.zhbj.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideUi extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener {

    private ViewPager mViewPager;
    private LinearLayout mPointContainer;
    private Button mBtnStart;

    private final static int[] PICS = {
            R.mipmap.guide_1,
            R.mipmap.guide_2,
            R.mipmap.guide_3,
    };

    private int mSpace;
    private List<ImageView> mPicImages;
    private View mPointSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_guide);

        initView();

        initData();

        initListener();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_pager);
        mPointContainer = (LinearLayout) findViewById(R.id.guide_point_container);
        mPointSelected = findViewById(R.id.guide_selected_point);
        mBtnStart = (Button) findViewById(R.id.guide_btn_start);
    }

    private void initData() {
        mPicImages = new ArrayList<>();
        for (int i = 0; i < PICS.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(PICS[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //添加到集合
            mPicImages.add(iv);

            View pointNormal = new View(this);
            pointNormal.setBackgroundResource(R.drawable.point_normal);
            //固定大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(this, 10), DensityUtils.dp2px(this, 10));
            //第一个点不设左间距
            if (i != 0) {
                params.leftMargin = DensityUtils.dp2px(this, 10);
            }
            mPointContainer.addView(pointNormal, params);
        }

        mViewPager.setAdapter(new GuideAdapter());
    }

    private void initListener() {
        mBtnStart.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);

        mPointContainer.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onClick(View view) {
        PreferenceUtils.putBoolean(this, Constants.SPLASH_FIRST_USE, false);
        startActivity(new Intent(this, MainUI.class));
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //页面滑动
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPointSelected.getLayoutParams();
        params.leftMargin = (int) (mSpace*position +mSpace*positionOffset+0.5f);
        mPointSelected.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        //页面选中
        mBtnStart.setVisibility((position == PICS.length - 1) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //状态改变
    }

    /**
     * 当界面布局变化的时候调用
     */
    @Override
    public void onGlobalLayout() {
        mSpace = mPointContainer.getChildAt(1).getLeft() - mPointContainer.getChildAt(0).getLeft();
        //当初始化间距就移除监听
        mPointContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (mPicImages != null) {
                return mPicImages.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mPicImages.get(position);
            //添加界面样式
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
