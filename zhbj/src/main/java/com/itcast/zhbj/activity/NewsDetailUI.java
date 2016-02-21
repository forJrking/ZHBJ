package com.itcast.zhbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.itcast.zhbj.R;
import com.itcast.zhbj.constant.Constants;
import com.itcast.zhbj.utils.PreferenceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailUI extends Activity {

    public static final String KEY_URL = "url";
    @Bind(R.id.news_deatil_webview)
    WebView mNewsDeatilWebview;
    @Bind(R.id.news_deatil_loading)
    ProgressBar mNewsDeatilLoading;

    private int mCheckItem;
    private WebSettings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_news_detail);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        //获得url
        String url = getIntent().getStringExtra(KEY_URL);
        //通过url加载 新闻页面数据
        url = url.replace(Constants.REPLACE_OLD, Constants.REPLACE_NEW);

        mSettings = mNewsDeatilWebview.getSettings();
        mSettings.setJavaScriptEnabled(true);//开启js代码
        mSettings.setBuiltInZoomControls(true);//放大缩小
        mSettings.setUseWideViewPort(true);//双击缩放

        mNewsDeatilWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mNewsDeatilLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mNewsDeatilLoading.setVisibility(View.GONE);
            }
        });
        //设置字体大小
        mCheckItem = PreferenceUtils.getInt(this, Constants.KEY_TEXTSIZE, 2);
        changeTextSize();

        mNewsDeatilWebview.loadUrl(url);
    }

    private void changeTextSize() {
        int size = 100;
        switch (mCheckItem) {
            case 0:
                size = 200;
                break;
            case 1:
                size = 150;
                break;
            case 2:
                size = 100;
                break;
            case 3:
                size = 75;
                break;
            case 4:
                size = 50;
                break;
        }
        //设置字体大小
        mSettings.setTextZoom(size);
    }


    @OnClick({R.id.news_deatil_back, R.id.news_deatil_textsize, R.id.news_deatil_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.news_deatil_back:
                finish();
                break;
            case R.id.news_deatil_textsize:
//                调整字体大小
                setTextSize();
                break;
            case R.id.news_deatil_share:
                //分享
                break;
        }
    }

    private void setTextSize() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("设置字体大小");

        CharSequence[] item = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        dialog.setSingleChoiceItems(item, mCheckItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCheckItem = which;
            }
        });

        dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeTextSize();
                //存储字体大小
                PreferenceUtils.putInt(NewsDetailUI.this, Constants.KEY_TEXTSIZE, mCheckItem);
            }
        });
        dialog.show();
    }
}
