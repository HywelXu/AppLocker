package com.hywel.applocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hywel.applocker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.setting_panel_guide_layout)
    LinearLayout mSettingGuideLayout;
    @BindView(R.id.setting_panel_guide)
    TextView mSettingGuideTV;
    @BindView(R.id.lock_tip)
    TextView mLockTipTV;
    @BindView(R.id.iv_icon_setting)
    ImageView mIconSettingIV;
    @BindView(R.id.switch_compat)
    CheckBox mSwitchOperator;
    @BindView(R.id.btn_change_pwd)
    TextView mChangePwdTV;
    @BindView(R.id.about_me)
    TextView mAboutTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);
        setListener();
        startBackAnim();
    }

    private void startBackAnim() {
        mIconSettingIV.animate().rotationY(360f).setDuration(3000).setInterpolator(new OvershootInterpolator()).start();
        mSettingGuideTV.animate().rotationX(360f).setDuration(3000).start();
    }

    private void setListener() {
        mSwitchOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitchOperator.isChecked()) {
                    mLockTipTV.setText(getText(R.string.setting_lock_tip_locked));
                } else {
                    mLockTipTV.setText(getText(R.string.setting_lock_tip_unlocked));
                }
            }
        });

        mChangePwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mAboutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
            }
        });

    }

    public void onBackArrowClicked(View view) {
        onBackPressed();
    }
}
