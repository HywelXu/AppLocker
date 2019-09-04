package com.hywel.applocker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hywel.applocker.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Hywel
 * Time: 2019-08-28
 * Function: 页面块状导航栏
 * <p>Copyright 2019 GRZQ.</p>
 */
public class TitleHeaderView extends FrameLayout {

    private Unbinder mBind;
    private Context mContext;

    public TitleHeaderView(@NonNull Context context) {
        super(context);
        thimContext=context;
        init();
    }

    public TitleHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View vView = LayoutInflater.from(getContext()).inflate(R.layout.title_header_view,this);
        mBind = ButterKnife.bind(vView);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBind.unbind();
    }
}
