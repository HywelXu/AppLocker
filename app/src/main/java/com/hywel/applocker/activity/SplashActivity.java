package com.hywel.applocker.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.hywel.applocker.R;
import com.hywel.applocker.utils.BusinessHelper;
import com.hywel.applocker.utils.SpUtil;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.image_splash)
    ImageView mSplashImageView;
    @BindView(R.id.tv_appName)
    TextView mAppNameTV;
    @BindView(R.id.tv_appDes)
    TextView mAppDesTV;

    private boolean lockerOpen;
    private boolean firstIn;
    private Intent serviceIntent;

    @Override
    protected int setRightTitleBarIcon() {
        return -1;
    }

    @Override
    protected void onRightMenuClicked(View view) {

    }

    @Override
    protected void makeActions() {
        startSplashAnim();
    }

    @Override
    protected void renderData() {
        BusinessHelper.getInstance().startApkService();
    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
        hideHeader();
        lockerOpen = SpUtil.getInstance().isLockerOpen();
        firstIn = SpUtil.getInstance().isFirstIn();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    private void startSplashAnim() {
        ObjectAnimator iconAnimator = ObjectAnimator.ofFloat(mSplashImageView, "rotationY", 0f, 180f);
        iconAnimator.setDuration(1000);
        ObjectAnimator nameTvAnimator = ObjectAnimator.ofFloat(mAppNameTV, "alpha", 0f, 1f);
        nameTvAnimator.setDuration(1300);
        ObjectAnimator desTvAnimator = ObjectAnimator.ofFloat(mAppDesTV, "alpha", 0f, 1f);
        desTvAnimator.setDuration(1300);

        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.play(iconAnimator).with(nameTvAnimator).with(desTvAnimator);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BusinessHelper.getInstance().transferPageThenFinish(SplashActivity.this, AppActivity.class);
                    }
                }, 1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusinessHelper.getInstance().stopApkService();
    }

    @Override
    public void onBackPressed() {
    }
}
