package com.hywel.applocker.fragment;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hywel on 2017/7/3.
 */

public class UserAppFragment extends Fragment {

    @BindView(R.id.app_recycler_view)
    RecyclerView mAppListRV;

    private Context mContext;
    private List<AppInfo> mAppInfos;

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
//            mAppInfos = getArguments().getParcelableArrayList("datas");
            mAppInfos = (List<AppInfo>) getArguments().getSerializable("datas");
        }
    }

    public static UserAppFragment newInstance(List<AppInfo> list) {
        UserAppFragment fragment = new UserAppFragment();
        Bundle args = new Bundle();
//        args.putParcelableArrayList("datas", (ArrayList<? extends Parcelable>) list);
        args.putSerializable("datas", (ArrayList<? extends Serializable>) list);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_applist, null);
        ButterKnife.bind(this, view);

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
