package com.hywel.applocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hywel.applocker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {
    @BindView(R.id.password_panel_guide)
    TextView mPasswordGuideTV;
    @BindView(R.id.iv_icon_setting)
    ImageView mIconSettingIV;
    @BindView(R.id.password_panel_guide_layout)
    LinearLayout mPasswordGuideLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        startHeaderAnim();
    }

    /**
     * 布局头部的动画
     */
    private void startHeaderAnim() {
        mIconSettingIV.animate().rotationY(360f).setDuration(1000).setInterpolator(new AnticipateInterpolator()).start();
        mPasswordGuideTV.animate().rotationX(360f).setDuration(1500).setInterpolator(new DecelerateInterpolator()).start();
    }

    public void gotoShare(View view) {
        simpleShare(getText(R.string.setting_panel_all_about_me_share_title).toString(), getText(R.string.setting_panel_all_about_me_share_text).toString());
    }

    private void simpleShare(String title, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, title));
    }

    //// TODO: 2017/7/5 加入分享截图功能


    public void onBackArrowClicked(View view) {
        onBackPressed();
    }
}
