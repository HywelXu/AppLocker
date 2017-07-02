package com.hywel.applocker.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.hywel.applocker.R;
import com.hywel.applocker.widget.PasswordPanel;
import com.hywel.applocker.widget.LockCallback;
import com.hywel.applocker.widget.LockIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LockCallback {
    /**
     * 文字提示
     */
    @BindView(R.id.password_panel_guide)
    TextView message;
    @BindView(R.id.password_panel)
    PasswordPanel passwordPanel;

    /**
     * 手势密码轨迹图案提示控件
     */
    @BindView(R.id.activity_gustlockset_indicator)
    LockIndicator guestIndictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        passwordPanel.setxCallback(this);
    }

    /**
     * 重新设置手势密码的方法
     */
    private void resetMethod() {
        passwordPanel.setVerify(false);
        passwordPanel.clearMethod();
        guestIndictor.clearMethod();
        message.setText(getString(R.string.string_guestset_typefirst));
        message.setTextColor(getResources().getColor(R.color.heise));
    }

    @Override
    public void onLockCallback(int type) {
        switch (type) {
            case LockCallback.POINT_LENGTH_SHORT:
                message.setText(getString(R.string.string_guestset_short));
                message.setTextColor(getResources().getColor(R.color.themecolor3));
//                startAnimationMethod();// 调用执行动画的方法
                break;
            case LockCallback.TWICE_NOT_SAME:
                message.setText(getString(R.string.string_guestset_notsame));
                message.setTextColor(getResources().getColor(R.color.themecolor3));
//                startAnimationMethod();// 调用执行动画的方法
                break;
            case LockCallback.TWICE_LINE_SAME:
                message.setTextColor(getResources().getColor(R.color.colorWhite));
                message.animate().rotationY(360).setDuration(500).start();
                message.setText("设置成功");
                Toast.makeText(this, "设置成功", Toast.LENGTH_LONG).show();
                break;
            case LockCallback.FIRST_LINE_OVER:
                guestIndictor.setSelectedMethod(passwordPanel.getHaschoosed()
                        .toString());
                message.setText(getString(R.string.string_guestset_typesecond));
                message.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            default:
                break;
        }
    }

    /** 执行文字提示动画的方法 */
//    private void startAnimationMethod() {
//        TranslateAnimation anima = ManagerAnimation.getMethod()
//                .setTranslateAnimation(1, -0.1f, 1, 0.1f, 1, 0, 1, 0, 100, 0,
//                        null, false, 5, 2);
//        message.startAnimation(anima);
//    }
}
