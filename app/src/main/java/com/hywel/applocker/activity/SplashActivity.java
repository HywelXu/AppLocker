package com.hywel.applocker.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.hywel.applocker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.image_splash)
    ImageView mSplashImageView;
    @BindView(R.id.tv_appName)
    TextView mAppNameTV;
    @BindView(R.id.tv_appDes)
    TextView mAppDesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        startSplashAnim();
    }

    private void startSplashAnim() {
        ObjectAnimator iconAnimator = ObjectAnimator.ofFloat(mSplashImageView, "rotationY", 0f, 180f);
        ObjectAnimator nameTvAnimator = ObjectAnimator.ofFloat(mAppNameTV, "alpha", 0f, 1f);
        ObjectAnimator desTvAnimator = ObjectAnimator.ofFloat(mAppDesTV, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
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
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                        startActivity(new Intent(SplashActivity.this, AppActivity.class));
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

}
