package com.hywel.applocker.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hywel.applocker.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void setRightTitleBar() {
        mRightImageView.setImageResource(android.R.drawable.ic_menu_share);
        mRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleShare(getText(R.string.setting_panel_all_about_me_share_title).toString(), getText(R.string.setting_panel_all_about_me_share_text).toString());
            }
        });
    }


    @Override
    protected void makeActions() {

    }

    @Override
    protected void renderData() {

    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
        injectView();
    }

    @Override
    public int getInjectLayoutId() {
        return R.layout.activity_about;
    }

    /**
     * 简易分享
     *
     * @param title 主题
     * @param text  内容
     */
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

}
