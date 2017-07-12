package com.hywel.applocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hywel.applocker.R;
import com.hywel.applocker.utils.SpUtil;

public class SettingActivity extends BaseActivity {

    private TextView mLockTipTV;
    private TextView mChangePwdTV;
    private TextView mAboutTV;
    private CheckBox mSwitchOperator;

    @Override
    protected void setRightTitleBar() {
        mRightImageView.setImageDrawable(null);
    }

    @Override
    protected void makeActions() {
        setListener();
    }

    @Override
    protected void renderData() {

    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
        injectView();
        mLockTipTV = (TextView) findViewById(R.id.lock_text);
        mSwitchOperator = (CheckBox) findViewById(R.id.switch_compat);
        mChangePwdTV = (TextView) findViewById(R.id.change_pwd);
        mAboutTV = (TextView) findViewById(R.id.about_me);
        boolean lockerOpen = SpUtil.getInstance(this).isLockerOpen();
        mSwitchOperator.setChecked(lockerOpen);
        mPswPanelHeader.setBackgroundResource(R.drawable.shape_password_panel_header_settinglayout);
        mPswPanelText.setText(getText(R.string.setting_panel_all_apps_to_lock_tip));
    }

    @Override
    public int getInjectLayoutId() {
        return R.layout.activity_setting;
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
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        SpUtil.getInstance(this).saveLockerIsOpen(mSwitchOperator.isChecked());
    }

}
