package com.hywel.applocker.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hywel.applocker.R;
import com.hywel.applocker.utils.BusinessHelper;
import com.hywel.applocker.utils.SpUtil;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.lock_text)
    TextView mLockTipTV;
    @BindView(R.id.change_pwd)
    TextView mChangePwdTV;
    @BindView(R.id.about_me)
    TextView mAboutTV;
    @BindView(R.id.switch_compat)
    CheckBox mSwitchOperator;

    @Override
    protected int setRightTitleBarIcon() {
        return -1;
    }

    @Override
    protected void onRightMenuClicked(View view) {

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
        boolean lockerOpen = SpUtil.getInstance().isLockerOpen();
        mSwitchOperator.setChecked(lockerOpen);
    }

    @Override
    public int getLayoutId() {
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
                BusinessHelper.getInstance().transferPageWithAnim(SettingActivity.this, AboutActivity.class, R.anim.slide_in_from_right, R.anim.slide_out_from_left);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        SpUtil.getInstance().saveLockerIsOpen(mSwitchOperator.isChecked());
    }

}
