package com.hywel.applocker.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hywel.applocker.R;
import com.hywel.applocker.adapter.AppAdapter;
import com.hywel.applocker.model.AppInfo;

import java.util.ArrayList;

/**
 * Author: Hywel
 * Time: 2019-08-29
 * Function:
 * <p>Copyright 2019 GRZQ.</p>
 */
public abstract class BaseFragment extends Fragment {
    Context mContext;
    RecyclerView mAppListRV;
    ArrayList<AppInfo> mAppInfos;
    AppAdapter mAppAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            mAppInfos = new ArrayList<>();
        } else {
            mAppInfos = getArguments().getParcelableArrayList("datas");
        }
    }


    @Nullable
    @Override
    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        mAppListRV = (RecyclerView) view.findViewById(R.id.app_recycler_view);
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        mAppAdapter = new AppAdapter(mContext, mAppInfos);
        mAppListRV.setItemAnimator(new DefaultItemAnimator());
        mAppListRV.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mAppListRV.setLayoutManager(new LinearLayoutManager(mContext));
        mAppListRV.setAdapter(mAppAdapter);
    }

    protected abstract @LayoutRes
    int getLayoutId();

    public void highLightItem(int pI) {
        for (int vI = 0; vI < mAppInfos.size(); vI++) {
            AppInfo vAppInfo = mAppInfos.get(vI);
            if (vI == pI) {
                vAppInfo.setHighLight(true);
            } else {
                vAppInfo.setHighLight(false);
            }
        }
        mAppAdapter.notifyDataSetChanged();
        mAppListRV.scrollToPosition(pI);
    }
}
