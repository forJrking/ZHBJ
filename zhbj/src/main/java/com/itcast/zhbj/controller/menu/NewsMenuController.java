package com.itcast.zhbj.controller.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.itcast.zhbj.R;
import com.itcast.zhbj.activity.MainUI;
import com.itcast.zhbj.bean.NewsCenterBean;
import com.itcast.zhbj.controller.BaseController;
import com.itcast.zhbj.controller.news.NewsListController;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsMenuController extends BaseController implements ViewPager.OnPageChangeListener {


    @Bind(R.id.menu_news_indicator)
    TabPageIndicator mIndicator;
    @Bind(R.id.menu_news_pager)
    ViewPager mPager;

    private List<NewsCenterBean.NewsCenterPagerBean> mDatas;

    public NewsMenuController(Context context, List<NewsCenterBean.NewsCenterPagerBean> children) {
        super(context);
        this.mDatas = children;
    }

    @Override
    public View initView(Context context) {
        //初始化界面
        View view = View.inflate(context, R.layout.menu_news, null);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        mPager.setAdapter(new NewsAdapter());
        //设置indicator
        mIndicator.setViewPager(mPager);
        //设置pager监听
        mPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        SlidingMenu menu = ((MainUI)mContext).getSlidingMenu();
        menu.setTouchModeAbove(position == 0 ? SlidingMenu.TOUCHMODE_FULLSCREEN : SlidingMenu.TOUCHMODE_NONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class NewsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (mDatas != null) {
                return mDatas.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsCenterBean.NewsCenterPagerBean bean = mDatas.get(position);

            NewsListController newsListController = new NewsListController(mContext,bean);
            View rootView = newsListController.getRootView();
            container.addView(rootView);
            //夹在数据
            newsListController.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //设置 顶部标题
            NewsCenterBean.NewsCenterPagerBean bean = mDatas.get(position);
            return bean.title;
        }
    }
}
