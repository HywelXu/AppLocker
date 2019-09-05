package com.hywel.applocker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.hywel.applocker.activity.BaseActivity;
import com.hywel.applocker.model.AppInfo;
import com.hywel.applocker.model.SimpleAppInfo;
import com.hywel.applocker.service.LockService;
import com.hywel.applocker.service.LockServiceConnection;
import com.hywel.applocker.utils.AndroidTools;
import com.hywel.applocker.utils.AppFrontBackHelper;
import com.hywel.applocker.utils.FancyToastUtils;
import com.hywel.applocker.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hywel on 2017/6/30.
 */

public class LockerApplication extends Application {
    public static LockerApplication lockerApplication;
    private Context mContext;
    public static int screenWidth;
    public static int screenHeight;

    public static ArrayList<AppInfo> allSysAppInfos = new ArrayList<>();
    public static ArrayList<SimpleAppInfo> allSysSimpleAppInfos = new ArrayList<>();
    public static ArrayList<AppInfo> allUserAppInfos = new ArrayList<>();
    public static ArrayList<SimpleAppInfo> allUserSimpleAppInfos = new ArrayList<>();
    //sp 中锁定的应用
    public static List<SimpleAppInfo> mLockedSimpleAppInfos;

    private SQLiteDatabase mDatabase;
    private static List<BaseActivity> activityList; //acticity管理

    @Override
    public void onCreate() {
        super.onCreate();
        lockerApplication = this;
        mContext = getApplicationContext();
//        SpUtil.getInstance().init(lockerApplication);
        activityList = new ArrayList<>();
        mLockedSimpleAppInfos = SpUtil.getInstance().getLockedPackNameList();
        screenWidth = AndroidTools.getScreenWidth(this);
        screenHeight = AndroidTools.getScreenHeight(this);

        AppFrontBackHelper helper = new AppFrontBackHelper();
        helper.register(LockerApplication.this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //应用切到前台处理
                FancyToastUtils.showSuccessToast("APP 在前台");
            }

            @Override
            public void onBack() {
                //应用切到后台处理
                FancyToastUtils.showSuccessToast("APP 在后台");
                bindLockService();
            }
        });
    }

    protected void bindLockService() {
//        startService(new Intent(AppActivity.this, LockService.class));
        //启动应用锁定服务
        LockServiceConnection connection = new LockServiceConnection();
        bindService(new Intent(this, LockService.class), connection, Context.BIND_AUTO_CREATE);
    }

    public Context getContext() {
        return mContext;
    }

    public static LockerApplication getInstance() {
        return lockerApplication;
    }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public void clearAllActivity() {
        try {
            for (BaseActivity activity : activityList) {
                if (activity != null)
                    activity.clear();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
