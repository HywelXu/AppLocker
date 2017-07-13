package com.hywel.applocker;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.hywel.applocker.activity.BaseActivity;
import com.hywel.applocker.model.AppInfo;
import com.hywel.applocker.utils.AndroidTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hywel on 2017/6/30.
 */

public class LockerApplication extends Application {
    public static LockerApplication lockerApplication;
    public static int screenWidth;
    public static int screenHeight;

    public static ArrayList<AppInfo> allSysAppInfos = new ArrayList<>();
    public static ArrayList<AppInfo> allUserAppInfos = new ArrayList<>();
    private SQLiteDatabase mDatabase;
    private static List<BaseActivity> activityList; //acticity管理

    @Override
    public void onCreate() {
        super.onCreate();
        lockerApplication = this;
//        SpUtil.getInstance().init(lockerApplication);
        activityList = new ArrayList<>();
        screenWidth = AndroidTools.getScreenWidth(this);
        screenHeight = AndroidTools.getScreenHeight(this);
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
