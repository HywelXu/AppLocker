package com.hywel.applocker.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        mIconSettingIV.animate().rotationY(360f).setDuration(1000).setInterpolator(new OvershootInterpolator()).start();
        mPasswordGuideTV.animate().rotationX(360f).setDuration(1000).setInterpolator(new AccelerateInterpolator()).start();
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

    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param activity    承载跳转的Activity
     * @param packageName 所需下载（评论）的应用包名
     */
    public static void shareAppShop(Activity activity, String packageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "您没有安装应用市场", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackArrowClicked(View view) {
        onBackPressed();
    }
}
