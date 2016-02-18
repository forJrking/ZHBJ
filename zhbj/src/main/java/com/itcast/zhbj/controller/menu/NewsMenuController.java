package com.itcast.zhbj.controller.menu;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itcast.zhbj.R;
import com.itcast.zhbj.bean.NewsCenterBean;
import com.itcast.zhbj.controller.BaseController;

import java.util.List;

public class NewsMenuController extends BaseController {

    private ViewPager mPager;
    private List<NewsCenterBean.NewsCenterPagerBean> mDatas;
    public NewsMenuController(Context context, List<NewsCenterBean.NewsCenterPagerBean> children) {
        super(context);
        this.mDatas = children;
    }

    @Override
    public View initView(Context context) {
        //初始化界面
        View view = View.inflate(context, R.layout.menu_news, null);

        mPager = (ViewPager) view.findViewById(R.id.menu_news_pager);

        return view;
    }

    @Override
    public void initData() {
        mPager.setAdapter(new NewsAdapter());
    }

    private class NewsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if(mDatas!=null){
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

            TextView tv = new TextView(mContext);
            tv.setText(bean.title);
            tv.setTextColor(Color.BLUE);
            tv.setTextSize(25);
            tv.setGravity(Gravity.CENTER);

            container.addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
