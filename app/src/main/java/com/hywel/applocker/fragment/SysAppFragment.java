package com.hywel.applocker.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
 * Created by hywel on 2017/7/3.
 */

public class SysAppFragment extends Fragment {

    RecyclerView mAppListRV;
    private Context mContext;
    private ArrayList<AppInfo> mAppInfos;

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
//            mAppInfos = (ArrayList<AppInfo>) getArguments().getSerializable("datas");
        }
    }

    public static SysAppFragment newInstance(ArrayList<AppInfo> list) {
        SysAppFragment fragment = new SysAppFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("datas", list);
//        args.putSerializable("datas", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_applist, null);
        mAppListRV = (RecyclerView) view.findViewById(R.id.app_recycler_view);
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        AppAdapter appAdapter = new AppAdapter(mContext, mAppInfos);
        mAppListRV.setItemAnimator(new DefaultItemAnimator());
        mAppListRV.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mAppListRV.setLayoutManager(new LinearLayoutManager(mContext));
        mAppListRV.setAdapter(appAdapter);
    }

}
