package com.hywel.applocker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.hywel.applocker.R;
import com.hywel.applocker.service.AppConstants;
import com.hywel.applocker.utils.BusinessHelper;
import com.hywel.applocker.utils.FancyToastUtils;
import com.hywel.applocker.utils.SpUtil;
import com.hywel.applocker.widget.GestureLockView.GestureLockCallback;
import com.hywel.applocker.widget.GestureLockView.GestureLockIndicator;
import com.hywel.applocker.widget.PasswordPanel;

import butterknife.BindView;

public class PwdVerifyActivity extends BaseActivity implements GestureLockCallback {
    @BindView(R.id.activity_gustlockset_message)
    TextView mMessageTextView;
    @BindView(R.id.password_panel)
    PasswordPanel passwordPanel;
    /**
     * 手势密码轨迹图案提示控件
     */
    @BindView(R.id.activity_gustlockset_indicator)
    GestureLockIndicator guestIndictor;
    private String mPackageName;
    private boolean mUnlockFromOthers = false;

    @Override
    protected int setRightTitleBarIcon() {
        return -1;
    }

    @Override
    protected void onRightMenuClicked(View view) {
    }

    /**
     * 执行解锁
     *
     * @param mContext     Context
     * @param pPackageName 目标 APP 的包名
     * @param pFrom        是否从其他 APP 而来
     */
    public static void performPwdVerify(Context mContext, String pPackageName, boolean pFrom) {
        Intent intent = new Intent(mContext, PwdVerifyActivity.class);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, pPackageName);
        intent.putExtra(AppConstants.LOCK_FROM, pFrom);
        mContext.startActivity(intent);
    }

    @Override
    protected void makeActions() {
        passwordPanel.setxCallback(this);

        Intent vIntent = getIntent();
        if (vIntent != null) {
            mPackageName = vIntent.getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
            mUnlockFromOthers = vIntent.getBooleanExtra(AppConstants.LOCK_FROM, false);
        }
    }

    @Override
    protected void renderData() {
    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    /**
     * 重新设置手势密码的方法
     */
    private void resetMethod() {
        passwordPanel.setVerify(false);
        passwordPanel.clearMethod();
        guestIndictor.clearMethod();
        mHTitleHeaderView.setPswPanelText(getString(R.string.string_guestset_typefirst));
        mHTitleHeaderView.setPswPanelTextColor(getResources().getColor(R.color.heise));
    }

    @Override
    public void onLockCallback(int type) {

        switch (type) {
            case GestureLockCallback.POINT_LENGTH_SHORT:
                mMessageTextView.setText(getString(R.string.string_guestset_short));
                mMessageTextView.setTextColor(getResources().getColor(R.color.themecolor3));
//                startAnimationMethod();// 调用执行动画的方法
                break;

            case GestureLockCallback.TWICE_NOT_SAME:
                mMessageTextView.setText(getString(R.string.string_guestset_notsame));
                mMessageTextView.setTextColor(getResources().getColor(R.color.themecolor3));
//                startAnimationMethod();// 调用执行动画的方法
                break;

            case GestureLockCallback.TWICE_LINE_SAME:
                mHTitleHeaderView.setPswPanelTextColor(getResources().getColor(R.color.colorWhite));
                mHTitleHeaderView.getPswPanelText().animate().rotationY(360).setDuration(500).start();
                mHTitleHeaderView.getIconImageView().animate().rotationY(360).setDuration(500).start();

                boolean firstIn = SpUtil.getInstance().isFirstIn();
                String tip = firstIn ? "设置成功" : "解锁成功";
                mHTitleHeaderView.setPswPanelText(tip);
                FancyToastUtils.showSuccessToast(tip);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mUnlockFromOthers) {
                            finish();
                        } else {
                            FancyToastUtils.showSuccessToast(mPackageName);
                            BusinessHelper.getInstance().sendPwdLockReceiver(mPackageName);
                            finish();
                        }
                    }
                }, 500);
                break;

            case GestureLockCallback.FIRST_LINE_OVER:
                guestIndictor.setSelectedMethod(passwordPanel.getHaschoosed()
                        .toString());
                mMessageTextView.setText(getString(R.string.string_guestset_typesecond));
                mMessageTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                break;

            default:
                break;
        }
    }


}
