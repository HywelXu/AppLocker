package com.hywel.applocker.activity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.hywel.applocker.R;
import com.hywel.applocker.utils.AppManager;
import com.hywel.applocker.widget.HTitleHeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hywel on 2017/6/30.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.htitle_header_view)
    HTitleHeaderView mHTitleHeaderView;

    //装载子布局的容器
//    @BindView(R.id.view_container_layout)
    LinearLayout mViewContainer;

    private Unbinder mUnbinder;
    protected BaseActivity mBaseActivity;

//    private boolean isFirstPerformAnim = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = this;
        setContentView(getBaseView());
        mUnbinder = ButterKnife.bind(this);
//        LockerApplication.getInstance().doForCreate(this);
        AppManager.getAppManager().addActivity(this);
        renderView(savedInstanceState);
        initHeader();
        renderData();
    }

    @NonNull
    private View getBaseView() {
        View vView = getLayoutInflater().inflate(R.layout.activity_base, null);
        View vInjectView = getLayoutInflater().inflate(getLayoutId(), null);
        mViewContainer = vView.findViewById(R.id.view_container_layout);
        mViewContainer.removeAllViews();
        mViewContainer.addView(vInjectView);
        return vView;
    }

    protected void hideHeader() {
        mHTitleHeaderView.hideTitleBarLayout();
    }

    protected void showHeader() {
        mHTitleHeaderView.showTitleBarLayout();
    }

    private int[] titleBgColors = new int[]{
            R.drawable.shape_password_panel_header_mainlayout,
            R.drawable.shape_password_panel_header_applayout,
            R.drawable.shape_password_panel_header_settinglayout,
            R.drawable.shape_password_panel_header_setting_about_me
    };

    private String[] titleTexts = new String[]{
            "身份确认",
            "加密应用",
            "应用设置",
            "关于"
    };

    private String[] classNames = new String[]{
            "PwdVerifyActivity",
            "AppActivity",
            "SettingActivity",
            "AboutActivity"
    };

    private void initHeader() {
        final int vSetRightTitleBarIcon = setRightTitleBarIcon();
        if (vSetRightTitleBarIcon > 0) {
            mHTitleHeaderView.showRightMenu();
            mHTitleHeaderView.setRightMenuIcon(vSetRightTitleBarIcon);
        } else {
            mHTitleHeaderView.hideRightMenu();
        }

        mHTitleHeaderView.setHTitleBarListener(new HTitleHeaderView.HTitleBarListener() {
            @Override
            public void onHTitleBackClicked(View view) {
                onBackArrowClicked();
            }

            @Override
            public void onHTitleBarMenuClicked(View view) {
                if (vSetRightTitleBarIcon > 0 && mHTitleHeaderView.isRightMenuVisible()) {
                    onRightMenuClicked(view);
                }
            }
        });

        String vSimpleName = mBaseActivity.getClass().getSimpleName();
        for (int vI = 0; vI < classNames.length; vI++) {
            String vClassName = classNames[vI];
            if (vClassName.equals(vSimpleName)) {
                mHTitleHeaderView.setTitleBarLayoutBgColorRes(titleBgColors[vI]);
                mHTitleHeaderView.setPswPanelText(titleTexts[vI]);
            }
        }
    }

    /**
     * 指定行为
     */
    protected abstract void makeActions();

    /**
     * 渲染数据
     */
    protected abstract void renderData();

    /**
     * 初始化视图
     */
    protected abstract void renderView(Bundle savedInstanceState);

    /**
     * 获取布局Id
     *
     * @return
     */
//    public abstract int getContentLayoutId();

    /**
     * 获取要插入的布局Id
     *
     * @return
     */
    public abstract @LayoutRes
    int getLayoutId();

    /**
     * 设置右侧菜单按钮图标
     */
    protected abstract @DrawableRes
    int setRightTitleBarIcon();

    /**
     * 设置右侧按钮的点击事件
     *
     * @param view
     */
    protected abstract void onRightMenuClicked(View view);

    @Override
    protected void onStart() {
        super.onStart();
        makeActions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
//        LockerApplication.getInstance().doForFinish(this);
        AppManager.getAppManager().finishAndResetLockerState(this);
    }

    protected void onBackArrowClicked() {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onBackArrowClicked();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public final void clear() {
        super.finish();
    }

}
