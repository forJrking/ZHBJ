package com.itcast.zhbj.controller.news;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.itcast.zhbj.R;
import com.itcast.zhbj.bean.NewsCenterBean;
import com.itcast.zhbj.bean.NewsListPagerBean;
import com.itcast.zhbj.constant.Constants;
import com.itcast.zhbj.controller.BaseController;
import com.itcast.zhbj.utils.DensityUtils;
import com.itcast.zhbj.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsListController extends BaseController implements ViewPager.OnPageChangeListener {
    @Bind(R.id.news_top_viewpager)
    ViewPager mNewsTopViewpager;
    @Bind(R.id.news_top_tv_title)
    TextView mNewsTopTvTitle;
    @Bind(R.id.news_top_point_container)
    LinearLayout mNewsTopPointContainer;

    private NewsCenterBean.NewsCenterPagerBean mData;
    private List<NewsListPagerBean.TopNewsBean> mTopNewsDatas;

    public NewsListController(Context context, NewsCenterBean.NewsCenterPagerBean bean) {
        super(context);
        this.mData = bean;
    }

    @Override
    public View initView(Context context) {

        View view = View.inflate(context, R.layout.news_list, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        //轮播图
        final String url = Constants.BASE_URL + mData.url;
        //读取缓存
        String json = PreferenceUtils.getString(mContext, url);
        if(!TextUtils.isEmpty(json)){
            //记载缓存数据
            processData(json);
        }
        //网络
        RequestQueue queue = Volley.newRequestQueue(mContext);

        Response.Listener<String> success = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //缓存数据
                PreferenceUtils.putString(mContext,url,response);
                processData(response);
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, url, success, error);
        queue.add(request);

    }

    private void processData(String json) {

        Gson gson = new Gson();

        NewsListPagerBean bean = gson.fromJson(json, NewsListPagerBean.class);
        //轮播图数据
        mTopNewsDatas = bean.data.topnews;

        mNewsTopViewpager.setAdapter(new TopNewsAdapter());
        //轮播图title
        mNewsTopTvTitle.setText(mTopNewsDatas.get(0).title);
        //清空转点容器
        mNewsTopPointContainer.removeAllViews();

        for (int i = 0; i < mTopNewsDatas.size(); i++) {
            View point = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(mContext, 6), DensityUtils.dp2px(mContext, 6));

            point.setBackgroundResource(R.mipmap.dot_normal);
            if (i != 0) {
                params.leftMargin = DensityUtils.dp2px(mContext, 6);
            } else {
                point.setBackgroundResource(R.mipmap.dot_focus);
            }

            mNewsTopPointContainer.addView(point, params);
        }
        //设置viewpager监听
        mNewsTopViewpager.addOnPageChangeListener(this);
        //开启定时器
        SwitchTask task = null;
        if(task==null) {
            task = new SwitchTask();
        }
        task.start();
    }

    private class SwitchTask extends Handler implements Runnable {

        @Override
        public void run() {
            //定时自动轮播
            //选中下一个
            int item = mNewsTopViewpager.getCurrentItem();
            if (item == mNewsTopViewpager.getAdapter().getCount() - 1) {
                item = 0;
            } else {
                item++;
            }
            mNewsTopViewpager.setCurrentItem(item);
            //递归执行定时
            this.postDelayed(this, 2000);
        }


        public void start() {
            this.postDelayed(this, 2000);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //点的选中
        int count = mNewsTopPointContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mNewsTopPointContainer.getChildAt(i);

            view.setBackgroundResource(i == position ? R.mipmap.dot_focus : R.mipmap.dot_normal);
        }
        //标题的改变
        mNewsTopTvTitle.setText(mTopNewsDatas.get(position).title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class TopNewsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (mTopNewsDatas != null) {
                return mTopNewsDatas.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载网络图片
            String url = mTopNewsDatas.get(position).topimage;
            //模拟器使用 TODO:
            url = url.replace(Constants.REPLACE_OLD, Constants.REPLACE_NEW);
            // LogUtils.p("######图片加载url" + url);
            Picasso.with(mContext).load(url).into(iv);
            //添加视图
            container.addView(iv);
            //返回标记
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
