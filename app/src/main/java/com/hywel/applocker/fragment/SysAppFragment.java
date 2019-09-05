package com.hywel.applocker.fragment;

import android.os.Bundle;

import com.hywel.applocker.R;
import com.hywel.applocker.model.AppInfo;

import java.util.ArrayList;

/**
 * Created by hywel on 2017/7/3.
 */

public class SysAppFragment extends BaseFragment {

    public static SysAppFragment newInstance(ArrayList<AppInfo> list) {
        SysAppFragment fragment = new SysAppFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("datas", list);
//        args.putSerializable("datas", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_applist;
    }

}
