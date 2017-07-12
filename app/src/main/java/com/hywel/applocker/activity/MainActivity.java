package com.hywel.applocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.hywel.applocker.R;
import com.hywel.applocker.service.FindApkService;
import com.hywel.applocker.utils.SpUtil;
import com.hywel.applocker.widget.GestureLockView.GestureLockCallback;
import com.hywel.applocker.widget.GestureLockView.GestureLockIndicator;
import com.hywel.applocker.widget.PasswordPanel;

public class MainActivity extends BaseActivity implements GestureLockCallback {
    TextView mMessageTextView;
    PasswordPanel passwordPanel;
    /**
     * 手势密码轨迹图案提示控件
     */
    GestureLockIndicator guestIndictor;
    private Intent serviceIntent;

    @Override
    protected void setRightTitleBar() {
        mRightImageView.setImageDrawable(null);
    }

    @Override
    protected void makeActions() {
        passwordPanel.setxCallback(this);
    }

    @Override
    protected void renderData() {
        startApkService();
    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
        injectView();
        mMessageTextView = (TextView) findViewById(R.id.activity_gustlockset_message);
        passwordPanel = (PasswordPanel) findViewById(R.id.password_panel);
        guestIndictor = (GestureLockIndicator) findViewById(R.id.activity_gustlockset_indicator);
        mPswPanelHeader.setBackgroundResource(R.drawable.shape_password_panel_header_mainlayout);
        mPswPanelText.setText(getText(R.string.password_panel_remind_to_lock_tip));
    }

    @Override
    public int getInjectLayoutId() {
        return R.layout.activity_main;
    }

    private void startApkService() {
        serviceIntent = new Intent(this, FindApkService.class);
        startService(serviceIntent);
    }

    private void stopApkService() {
        if (null != serviceIntent)
            stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopApkService();
    }

    /**
     * 重新设置手势密码的方法
     */
    private void resetMethod() {
        passwordPanel.setVerify(false);
        passwordPanel.clearMethod();
        guestIndictor.clearMethod();
        mPswPanelText.setText(getString(R.string.string_guestset_typefirst));
        mPswPanelText.setTextColor(getResources().getColor(R.color.heise));
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
                mPswPanelText.setTextColor(getResources().getColor(R.color.colorWhite));
                mPswPanelText.animate().rotationY(360).setDuration(500).start();
                mIconImageView.animate().rotationY(360).setDuration(500).start();
                boolean firstIn = SpUtil.getInstance(this).isFirstIn();
                String tip = firstIn ? "设置成功" : "解锁成功";
                mPswPanelText.setText(tip);
                Toast.makeText(this, tip, Toast.LENGTH_LONG).show();

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

}
