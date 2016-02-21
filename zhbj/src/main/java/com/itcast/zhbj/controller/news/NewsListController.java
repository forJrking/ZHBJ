package com.itcast.zhbj.controller.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.itcast.zhbj.activity.NewsDetailUI;
import com.itcast.zhbj.bean.NewsCenterBean;
import com.itcast.zhbj.bean.NewsListPagerBean;
import com.itcast.zhbj.constant.Constants;
import com.itcast.zhbj.controller.BaseController;
import com.itcast.zhbj.controller.menu.NewsMenuController;
import com.itcast.zhbj.utils.DensityUtils;
import com.itcast.zhbj.utils.LogUtils;
import com.itcast.zhbj.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import org.itheima19.library.RefreshListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsListController extends BaseController implements ViewPager.OnPageChangeListener, NewsMenuController.OnViewIDLEListener, RefreshListView.OnRefreshListener, AdapterView.OnItemClickListener {
    @Bind(R.id.news_top_viewpager)
    ViewPager mNewsTopViewpager;
    @Bind(R.id.news_top_tv_title)
    TextView mNewsTopTvTitle;
    @Bind(R.id.news_top_point_container)
    LinearLayout mNewsTopPointContainer;

    private RefreshListView mNewsListView;

    private NewsCenterBean.NewsCenterPagerBean mData;
    private List<NewsListPagerBean.TopNewsBean> mTopNewsDatas;
    private SwitchTask mTask;
    private List<NewsListPagerBean.NewsBean> mNewsDatas;
    private String mMoreUrl;
    private NewsAdapter mNewsAdapter;

    public NewsListController(Context context, NewsCenterBean.NewsCenterPagerBean bean) {
        super(context);
        this.mData = bean;
    }

    @Override
    public View initView(Context context) {

        View view = View.inflate(context, R.layout.news_list, null);
        View topView = View.inflate(context, R.layout.news_top, null);
        ButterKnife.bind(this, topView);

        mNewsListView = (RefreshListView) view.findViewById(R.id.news_listview);
        //刷新监听
        mNewsListView.addOnRefreshListener(this);
        //点击条目的监听
        mNewsListView.setOnItemClickListener(this);

        mNewsListView.addHeaderView(topView);
        return view;
    }

    @Override
    public void initData() {
        //轮播图
        final String url = Constants.BASE_URL + mData.url;
        //读取缓存
        String json = PreferenceUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(json)) {
            //记载缓存数据
            processData(json);
        }
        //网络
        RequestQueue queue = Volley.newRequestQueue(mContext);

        Response.Listener<String> success = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //缓存数据
                PreferenceUtils.putString(mContext, url, response);
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
        //更多数据加载的url
        mMoreUrl = bean.data.more;

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
        if (mTask == null) {
            mTask = new SwitchTask();
            // mTask.start();
        }
        mTask.start();//会让轮播多跳动

        //对pager的touch监听
        pagerListener();
        //获取新闻条目数据
        mNewsDatas = bean.data.news;
        mNewsAdapter = new NewsAdapter();
        mNewsListView.setAdapter(mNewsAdapter);
    }

    @Override
    public void onRefreshing() {
        final String url = Constants.BASE_URL + mData.url;
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        Response.Listener<String> success = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //存储缓存
                PreferenceUtils.putString(mContext, url, response);
                //json --》 object ---》ui
                processData(response);
                //通知刷新完成
                mNewsListView.refreshFinish();
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //通知刷新完成
                mNewsListView.refreshFinish();

            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, url, success, error);

        queue.add(request);
    }

    @Override
    public void onLoadingMore() {

        if(TextUtils.isEmpty(mMoreUrl)){
            mNewsListView.refreshFinish(false);
            return;
        }

        String url = Constants.BASE_URL + mMoreUrl;

        RequestQueue queue = Volley.newRequestQueue(mContext);
        Response.Listener<String> success = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                NewsListPagerBean bean = gson.fromJson(response, NewsListPagerBean.class);
                //保留下一页加载的数据
                mMoreUrl = bean.data.more;

                List<NewsListPagerBean.NewsBean> moreNews = bean.data.news;
                //数据添加到集合
                mNewsDatas.addAll(moreNews);
                //ui进行更新
                mNewsAdapter.notifyDataSetChanged();

                //下面没有更多时 不显示加载更多
                mNewsListView.refreshFinish(!TextUtils.isEmpty(mMoreUrl));
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mNewsListView.refreshFinish();
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, url, success, error);
        queue.add(request);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //获得listView头的数量
        position = position - mNewsListView.getHeaderViewsCount();
        LogUtils.p(mNewsListView.getHeaderViewsCount()+"#####ListView的头数");
        NewsListPagerBean.NewsBean bean = mNewsDatas.get(position);
        //已读
        PreferenceUtils.putBoolean(mContext, bean.id + "", true);
        //更新UI
        mNewsAdapter.notifyDataSetChanged();
        //跳转到新闻详情
        Intent intent = new Intent(mContext,NewsDetailUI.class);
           intent.putExtra(NewsDetailUI.KEY_URL,bean.url);
        mContext.startActivity(intent);
    }

    private class NewsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mNewsDatas != null) {
                return mNewsDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mNewsDatas != null) {
                return mNewsDatas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                //初次加载
                convertView = View.inflate(mContext, R.layout.item_newslist, null);
                holder = new ViewHolder();
                //设置标记
                convertView.setTag(holder);
                holder.ivPic = (ImageView) convertView.findViewById(R.id.item_newslist_iv_pic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.item_newslist_tv_title);
                holder.tvData = (TextView) convertView.findViewById(R.id.item_newslist_tv_date);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //加载数据到界面
            NewsListPagerBean.NewsBean newsBean = mNewsDatas.get(position);
            holder.tvTitle.setText(newsBean.title);
            holder.tvData.setText(newsBean.pubdate);
            String url = newsBean.listimage;
            url = url.replace(Constants.REPLACE_OLD, Constants.REPLACE_NEW);
            //记载图片
            Picasso.with(mContext).load(url).placeholder(R.mipmap.pic_item_list_default).error(R.mipmap.pic_item_list_default).into(holder.ivPic);
            boolean isRead = PreferenceUtils.getBoolean(mContext, newsBean.id + "");
            holder.tvTitle.setTextColor(isRead? Color.parseColor("#77000000"):Color.BLACK);

            return convertView;
        }
    }

    private class ViewHolder {
        ImageView ivPic;
        TextView tvTitle;
        TextView tvData;
    }

    private void pagerListener() {
        mNewsTopViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //检测到按下就停止轮播
                        mTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mTask.start();
                        //开启轮播时间不当
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onIDLE() {
        mTask.start();
    }

    @Override
    public void onScroll() {
        mTask.stop();
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
            this.removeCallbacks(this);
            this.postDelayed(this, 2000);
        }

        public void stop() {
            this.removeCallbacks(this);//清空当前任务
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
