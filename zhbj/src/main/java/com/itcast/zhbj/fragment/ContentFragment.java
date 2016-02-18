package com.itcast.zhbj.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.itcast.zhbj.R;
import com.itcast.zhbj.activity.MainUI;
import com.itcast.zhbj.controller.BaseController;
import com.itcast.zhbj.controller.TabController;
import com.itcast.zhbj.controller.tab.GovController;
import com.itcast.zhbj.controller.tab.HomeController;
import com.itcast.zhbj.controller.tab.NewsCenterController;
import com.itcast.zhbj.controller.tab.SettingController;
import com.itcast.zhbj.controller.tab.SmartServiceController;
import com.itcast.zhbj.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {


    private NoScrollViewPager mPager;
    private RadioGroup mTabs;
    private List<TabController> mPagerDatas;
    private FragmentActivity mActivity;
    private int mCurrentIndex;

    public ContentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化view
        mPager = (NoScrollViewPager) view.findViewById(R.id.content_pager);
        mTabs = (RadioGroup) view.findViewById(R.id.content_tabs);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        mPagerDatas = new ArrayList<>();

        mPagerDatas.add(new HomeController(mActivity));
        mPagerDatas.add(new NewsCenterController(mActivity));
        mPagerDatas.add(new SmartServiceController(mActivity));
        mPagerDatas.add(new GovController(mActivity));
        mPagerDatas.add(new SettingController(mActivity));
        //初始化界面
        mPager.setAdapter(new ContentPagerAdapter());

        mTabs.setOnCheckedChangeListener(new TabCheckedListener());
        //设置默认选中
        mTabs.check(R.id.content_rb_newscenter);
    }

    public void changeMenu(int position) {
        //当前选中的tab
        TabController controller = mPagerDatas.get(mCurrentIndex);

        controller.changeMenu(position);
    }

    private class TabCheckedListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

            switch (checkedId) {
                case R.id.content_rb_home:
                    mCurrentIndex = 0;
                    enableSlidingMenu(false);
                    break;
                case R.id.content_rb_newscenter:
                    mCurrentIndex = 1;
                    enableSlidingMenu(true);
                    break;
                case R.id.content_rb_smart:
                    mCurrentIndex = 2;
                    enableSlidingMenu(true);
                    break;
                case R.id.content_rb_gov:
                    mCurrentIndex = 3;
                    enableSlidingMenu(true);
                    break;
                case R.id.content_rb_setting:
                    mCurrentIndex = 4;
                    //不可拖拽菜单
                    enableSlidingMenu(false);
                    break;

            }
            mPager.setCurrentItem(mCurrentIndex);
        }
    }

    private void enableSlidingMenu(boolean enable){

        MainUI ui = (MainUI) mActivity;

        SlidingMenu menu =  ui.getSlidingMenu();
        menu.setTouchModeAbove(enable?SlidingMenu.LEFT:SlidingMenu.TOUCHMODE_NONE);
    }

    private class ContentPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (mPagerDatas != null) {
                return mPagerDatas.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseController controller = mPagerDatas.get(position);
            View view = controller.getRootView();
            //加载界面
            container.addView(view);
            //加载数据
            controller.initData();
            //返回标记
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
