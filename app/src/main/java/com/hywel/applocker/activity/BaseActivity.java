package com.hywel.applocker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.R;

/**
 * Created by hywel on 2017/6/30.
 */

public abstract class BaseActivity extends AppCompatActivity {
    //标题栏
    ImageView mBackImageView;
    ImageView mRightImageView;
    FrameLayout mTitleBarLayout;

    //导航头部
    LinearLayout mPswPanelHeader;
    ImageView mIconImageView;
    TextView mPswPanelText;

    //装载子布局的容器
    LinearLayout mViewContainer;

//    private boolean isFirstPerformAnim = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        LockerApplication.getInstance().doForCreate(this);

        initBaseSelfView();
        renderView(savedInstanceState);
        initHeader();
        renderData();
    }

    private void initBaseSelfView() {
        //标题栏
        mBackImageView = (ImageView) findViewById(R.id.iv_back);
        mRightImageView = (ImageView) findViewById(R.id.iv_right);
        mTitleBarLayout = (FrameLayout) findViewById(R.id.title_bar_layout);

        //导航头部
        mPswPanelHeader = (LinearLayout) findViewById(R.id.password_panel_header);
        mIconImageView = (ImageView) findViewById(R.id.iv_icon);
        mPswPanelText = (TextView) findViewById(R.id.password_panel_text);

        //装载子布局的容器
        mViewContainer = (LinearLayout) findViewById(R.id.view_container_layout);
    }

    protected void hideHeader() {
        mTitleBarLayout.setVisibility(View.GONE);
    }

    protected void showHeader() {
        mTitleBarLayout.setVisibility(View.VISIBLE);
    }

    private void initHeader() {
//        showHeader();
        setLeftTitleBar();
        setRightTitleBar();
    }

    protected abstract void setRightTitleBar();

    private void setLeftTitleBar() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackArrowClicked();
            }
        });
    }


    protected void injectView() {
        getLayoutInflater().inflate(getInjectLayoutId(), mViewContainer);
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
     *
     * @param savedInstanceState
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
    public abstract int getInjectLayoutId();

    @Override
    protected void onStart() {
        super.onStart();
        makeActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isFirstPerformAnim)
        makeHeaderAnimation();
    }

    /**
     * 布局头部的动画
     */
    private void makeHeaderAnimation() {
        mIconImageView.animate().rotationY(360f).setDuration(800).start();
        mPswPanelText.animate().rotationX(360f).setDuration(800).start();
//        isFirstPerformAnim = false;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LockerApplication.getInstance().doForFinish(this);
    }

    public void onBackArrowClicked() {
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
