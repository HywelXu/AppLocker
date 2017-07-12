package com.hywel.applocker;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.hywel.applocker.model.AppInfo;
import com.hywel.applocker.utils.AndroidTools;

import java.util.ArrayList;

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

    @Override
    public void onCreate() {
        super.onCreate();
        lockerApplication = this;
//        SpUtil.getInstance().init(lockerApplication);

        screenWidth = AndroidTools.getScreenWidth(this);
        screenHeight = AndroidTools.getScreenHeight(this);
    }

    public static LockerApplication getInstance() {
        return lockerApplication;
    }

}
