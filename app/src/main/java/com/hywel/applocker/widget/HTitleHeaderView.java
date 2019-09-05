package com.hywel.applocker.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hywel.applocker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Hywel
 * Time: 2019-08-28
 * Function: 页面块状导航栏
 * <p>Copyright 2019 GRZQ.</p>
 */
public class HTitleHeaderView extends FrameLayout implements View.OnClickListener {
    //标题栏
    @BindView(R.id.iv_back)
    ImageView mBackImageView;
    @BindView(R.id.iv_right)
    ImageView mRightImageView;
    @BindView(R.id.title_bar_layout)
    FrameLayout mTitleBarLayout;

    //导航头部
    @BindView(R.id.password_panel_header)
    LinearLayout mPswPanelHeader;
    @BindView(R.id.iv_icon)
    ImageView mIconImageView;
    @BindView(R.id.password_panel_text)
    TextView mPswPanelText;

    private Unbinder mBind;
    private Context mContext;
    private HTitleBarListener mHTitleBarListener;

    public void setHTitleBarListener(HTitleBarListener pHTitleBarListener) {
        mHTitleBarListener = pHTitleBarListener;
    }

    public HTitleHeaderView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public HTitleHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public HTitleHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        View vView = LayoutInflater.from(mContext).inflate(R.layout.title_header_view, this);
        mBind = ButterKnife.bind(vView);
        mBackImageView.setOnClickListener(this);
        mRightImageView.setOnClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        makeHeaderAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBind.unbind();
    }

    /**
     * 布局头部的动画
     */
    private void makeHeaderAnimation() {
        mIconImageView.animate().rotationY(360f).setDuration(800).start();
        mPswPanelText.animate().rotationX(360f).setDuration(800).start();
//        isFirstPerformAnim = false;
    }

    public FrameLayout getTitleBarLayout() {
        return mTitleBarLayout;
    }

    public LinearLayout getPswPanelHeader() {
        return mPswPanelHeader;
    }

    public ImageView getIconImageView() {
        return mIconImageView;
    }

    public TextView getPswPanelText() {
        return mPswPanelText;
    }

    public void setRightMenuIcon(@DrawableRes int pResId) {
        mRightImageView.setImageResource(pResId);
    }

    public void setRightMenuIcon(Bitmap pBitmap) {
        mRightImageView.setImageBitmap(pBitmap);
    }

    public void setTitleHaderImageView(@DrawableRes int pResId) {
        mIconImageView.setImageResource(pResId);
    }

    public void setTitleHaderImageView(Bitmap pBitmap) {
        mIconImageView.setImageBitmap(pBitmap);
    }

    public void setPswPanelText(String pPswPanelText) {
        mPswPanelText.setText(pPswPanelText);
    }

    public void setPswPanelTextColor(int pPswPanelTextColor) {
        mPswPanelText.setTextColor(pPswPanelTextColor);
    }

    public void setPswPanelText(CharSequence pPswPanelText) {
        mPswPanelText.setText(pPswPanelText);
    }

    public void showTitleBarLayout() {
        setViewVisibility(mTitleBarLayout, true);
    }

    public void hideTitleBarLayout() {
        setViewVisibility(mTitleBarLayout, false);
    }

    public void setTitleBarLayoutBgColor(int pBgColor) {
        mPswPanelHeader.setBackgroundColor(pBgColor);
    }

    public void setTitleBarLayoutBgColorRes(int pBgColorRes) {
        mPswPanelHeader.setBackgroundResource(pBgColorRes);
    }

//    public void setTitleBarLayoutBgColorDrawable(int pBgColorRes) {
//        mPswPanelHeader.setBackgroundResource(pBgColorRes);
//    }

    public void showRightMenu() {
        setViewVisibility(mRightImageView, true);
    }

    public void hideRightMenu() {
        setViewVisibility(mRightImageView, false);
    }

    public boolean isRightMenuVisible() {
        return mRightImageView.getVisibility() == VISIBLE;
    }

    private void setViewVisibility(View pView, boolean isShow) {
        pView.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        if (mHTitleBarListener != null) {
            switch (v.getId()) {
                case R.id.iv_back:
                    mHTitleBarListener.onHTitleBackClicked(v);
                    break;
                case R.id.iv_right:
                    mHTitleBarListener.onHTitleBarMenuClicked(v);
                    break;

                default:
                    break;
            }
        }
    }

    public interface HTitleBarListener {
        //返回按钮
        void onHTitleBackClicked(View view);

        //右侧图标
        void onHTitleBarMenuClicked(View view);
    }
}
