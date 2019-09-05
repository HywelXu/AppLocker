package com.hywel.applocker.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

public class LockServiceConnection implements ServiceConnection {

    private IMyBinder iMyBinder;
    private List<String> packageList=new ArrayList<>();

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        iMyBinder = (IMyBinder) iBinder;
        iMyBinder.setPackageNames(packageList);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }
}