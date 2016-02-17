package com.itcast.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.itcast.zhbj.R;
import com.itcast.zhbj.constant.Constants;
import com.itcast.zhbj.utils.PreferenceUtils;


public class SplashUI extends Activity implements Animation.AnimationListener {

    private static final long DURATION = 1500;
    private RelativeLayout mRlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_splash);

        initView();
        //界面动画相关
        initAnim();
    }

    private void initView() {
        mRlRoot = (RelativeLayout) findViewById(R.id.ui_splash_rl_root);
    }

    private void initAnim() {
        AnimationSet set = new AnimationSet(false);
        set.setDuration(DURATION);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        ScaleAnimation scale = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        set.addAnimation(alpha);
        set.addAnimation(scale);

        mRlRoot.setAnimation(set);

        set.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //动画结束变换U
        if (PreferenceUtils.getBoolean(this, Constants.SPLASH_FIRST_USE, true)) {
            startActivity(new Intent(this, GuideUi.class));
        } else {
            startActivity(new Intent(this, MainUI.class));
        }
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
