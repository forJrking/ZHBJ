package com.itcast.zhbj.controller.tab;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.itcast.zhbj.activity.MainUI;
import com.itcast.zhbj.bean.NewsCenterBean;
import com.itcast.zhbj.constant.Constants;
import com.itcast.zhbj.controller.BaseController;
import com.itcast.zhbj.controller.TabController;
import com.itcast.zhbj.controller.menu.InteractMenuController;
import com.itcast.zhbj.controller.menu.NewsMenuController;
import com.itcast.zhbj.controller.menu.PicMenuController;
import com.itcast.zhbj.controller.menu.SubjectMenuController;
import com.itcast.zhbj.fragment.MenuFragment;
import com.itcast.zhbj.utils.LogUtils;
import com.itcast.zhbj.utils.PreferenceUtils;
import com.itcast.zhbj.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class NewsCenterController extends TabController {

    private FrameLayout mContainer;
    private List<BaseController> mMenuControllers;
    private List<NewsCenterBean.NewsCenterMenuBean> mMenuDatas;

    public NewsCenterController(Context context) {
        super(context);
    }

    @Override
    public View initContentView(Context context) {
//        TextView tv = new TextView(context);
//
//        tv.setText("新闻中心");
//        tv.setTextColor(Color.RED);
//        tv.setTextSize(25);
//        tv.setGravity(Gravity.CENTER);
        mContainer = new FrameLayout(context);
        //显示视图
        return mContainer;
    }

    @Override
    public void initData() {

        mTVTitle.setText("新闻");
        //realTime(); //实时更新

        //隔时更新数据
        delayData();
    }

    private void delayData() {
        //url
        final String url = Constants.NEWSCENTER_URL;
        //隔时更新
        String json = PreferenceUtils.getString(mContext, url);

        if (!TextUtils.isEmpty(json)) {
            long nowTime = PreferenceUtils.getLong(mContext, url + "-time");
            long currentTime = System.currentTimeMillis();
            //过期
            if (nowTime + Constants.DATA_DELAY < currentTime) {
                LogUtils.p("数据过期####");
                processData(json);

                //加载网络
                loadNetData();
            } else {
                //没有过期
                LogUtils.p("数据没过期####");
                processData(json);
            }
        } else {
            LogUtils.p("初次加载####");
            loadNetData();
        }
    }

    private void loadNetData() {

        final String url = Constants.NEWSCENTER_URL;
        RequestQueue queue = Volley.newRequestQueue(mContext);
        //成功的回调
        Response.Listener<String> success = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //持久化存储
                LogUtils.p("加载网络数据成功####");
                PreferenceUtils.putString(mContext, url, response);
                //存储时间
                PreferenceUtils.putLong(mContext, url + "-time", System.currentTimeMillis());
                //解析结果 json
                processData(response);
            }
        };
        //失败
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.p("数据联网失败####");
                ToastUtils.showShort(mContext, "请检查网络连接...");
            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, url, success, error);
        //发送请求
        queue.add(request);
    }

    /**
     * 实时获取数据
     */
    private void realTime() {
        final String url = Constants.NEWSCENTER_URL;
        //读取缓存
        String json = PreferenceUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(json)) {
            processData(json);
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);
        //成功的回调
        Response.Listener<String> success = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //持久化存储
                PreferenceUtils.putString(mContext, url, response);
                //解析结果 json
                processData(response);
            }
        };
        //失败
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showShort(mContext, "请检查网络连接...");
            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, url, success, error);
        //发送请求
        queue.add(request);
    }

    private void processData(String response) {
        Gson gson = new Gson();
        NewsCenterBean bean = gson.fromJson(response, NewsCenterBean.class);

        mMenuDatas = bean.data;
        //让菜单初始化数据
        MainUI ui = (MainUI) mContext;
        MenuFragment menuFragment = ui.getMenuFragment();
        menuFragment.setMenuDatas(mMenuDatas);

        mMenuControllers = new ArrayList<>();
        for (NewsCenterBean.NewsCenterMenuBean menu : mMenuDatas) {
            BaseController controller = null;
            switch (menu.type) {
                case 1:
                    controller = new NewsMenuController(mContext, menu.children);
                    break;
                case 2://组图
                    controller = new PicMenuController(mContext);
                    break;
                case 3://互动
                    controller = new InteractMenuController(mContext);
                    break;
                case 10://专题
                    controller = new SubjectMenuController(mContext);
                    break;
            }
            mMenuControllers.add(controller);
        }
        //默认显示新闻页面
        changeMenu(0);
    }

    /**
     * @param position 控制器标记
     */
    @Override
    public void changeMenu(int position) {

        //清除
        mContainer.removeAllViews();

        NewsCenterBean.NewsCenterMenuBean bean = mMenuDatas.get(position);
        //变换标题
        mTVTitle.setText(bean.title);
        BaseController controller = mMenuControllers.get(position);
        View rootView = controller.getRootView();
        //视图添加
        mContainer.addView(rootView);
        //加载数据
        controller.initData();
    }
}
