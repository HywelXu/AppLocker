package com.hywel.applocker.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.hywel.applocker.R;
import com.hywel.applocker.utils.SpUtil;

public class SplashActivity extends BaseActivity {
    ImageView mSplashImageView;
    TextView mAppNameTV;
    TextView mAppDesTV;
    private boolean lockerOpen;
    private boolean firstIn;

    @Override
    protected void setRightTitleBar() {
        mRightImageView.setImageDrawable(null);
    }

    @Override
    protected void makeActions() {

    }

    @Override
    protected void renderData() {

    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
        hideHeader();
        injectView();
        initSelfView();
        lockerOpen = SpUtil.getInstance(this).isLockerOpen();
        firstIn = SpUtil.getInstance(this).isFirstIn();
        startSplashAnim();
    }

    @Override
    public int getInjectLayoutId() {
        return R.layout.activity_splash;
    }

    private void initSelfView() {
        mSplashImageView = (ImageView) findViewById(R.id.image_splash);
        mAppNameTV = (TextView) findViewById(R.id.tv_appName);
        mAppDesTV = (TextView) findViewById(R.id.tv_appDes);
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

//                        if (firstIn) {
//                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                        } else {
//                            if (lockerOpen) {
//                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                            } else {
//                                startActivity(new Intent(SplashActivity.this, AppActivity.class));
//                            }
//                        }

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));

                        overridePendingTransition(R.anim.drop_in, R.anim.drop_out);
                        finish();
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
    public void onBackPressed() {
    }
}
