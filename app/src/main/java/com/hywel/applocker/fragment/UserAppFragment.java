package com.hywel.applocker.fragment;

import android.os.Bundle;

import com.hywel.applocker.R;
import com.hywel.applocker.model.AppInfo;

import java.util.ArrayList;

/**
 * Created by hywel on 2017/7/3.
 */

public class UserAppFragment extends BaseFragment {

    public static UserAppFragment newInstance(ArrayList<AppInfo> list) {
        UserAppFragment fragment = new UserAppFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("datas", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_applist;
    }

}
