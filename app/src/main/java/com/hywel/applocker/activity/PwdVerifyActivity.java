package com.hywel.applocker.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.hywel.applocker.R;
import com.hywel.applocker.service.AppConstants;
import com.hywel.applocker.service.FindApkService;
import com.hywel.applocker.utils.FancyToastUtils;
import com.hywel.applocker.utils.SpUtil;
import com.hywel.applocker.widget.GestureLockView.GestureLockCallback;
import com.hywel.applocker.widget.GestureLockView.GestureLockIndicator;
import com.hywel.applocker.widget.PasswordPanel;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements GestureLockCallback {
    @BindView(R.id.activity_gustlockset_message)
    TextView mMessageTextView;
    @BindView(R.id.password_panel)
    PasswordPanel passwordPanel;
    /**
     * 手势密码轨迹图案提示控件
     */
    @BindView(R.id.activity_gustlockset_indicator)
    GestureLockIndicator guestIndictor;
    private Intent serviceIntent;

    @Override
    protected int setRightTitleBarIcon() {
        return -1;
    }

    @Override
    protected void onRightMenuClicked(View view) {
    }

    @Override
    protected void makeActions() {
        passwordPanel.setxCallback(this);
        registerPwdLockReceiver();
    }

    @Override
    protected void renderData() {
        startApkService();
    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void startApkService() {
        serviceIntent = new Intent(this, FindApkService.class);
        startService(serviceIntent);
    }

    private void stopApkService() {
        if (null != serviceIntent) {
            stopService(serviceIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopApkService();
        unregisterPwdLockReceiver();
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
                        startActivity(new Intent(MainActivity.this, AppActivity.class));
                        finish();
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

    private PwdLockReceiver mPwdLockReceiver;

    private void registerPwdLockReceiver() {
        mPwdLockReceiver = new PwdLockReceiver();
        IntentFilter vIntentFilter = new IntentFilter(AppConstants.PWD_LOCK_BROAD_ACTION);
        registerReceiver(mPwdLockReceiver, vIntentFilter);
    }

    private void unregisterPwdLockReceiver() {
        if (mPwdLockReceiver != null) {
            unregisterReceiver(mPwdLockReceiver);
        }
    }


    public class PwdLockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
