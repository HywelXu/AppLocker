package com.hywel.applocker.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hywel.applocker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.password_panel_guide)
    TextView mGuideTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        startGuideTVAnim();
    }

    private void startGuideTVAnim() {
        final int startScrollPos = getResources()
                .getDimensionPixelSize(R.dimen.scroll_animation_start);

        ObjectAnimator animator = ObjectAnimator.ofInt(mGuideTV, "scrollY", startScrollPos);
        animator.setDuration(2000).start();
    }

}
