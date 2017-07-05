package com.hywel.applocker;

import android.app.Application;

import com.hywel.applocker.model.AppInfo;
import com.hywel.applocker.utils.AndroidTools;
import com.hywel.applocker.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hywel on 2017/6/30.
 */

public class LockerApplication extends Application {
    public LockerApplication lockerApplication;
    public static int screenWidth;
    public static int screenHeight;

    public static List<AppInfo> allSysAppInfos = new ArrayList<>();
    public static List<AppInfo> allUserAppInfos = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        lockerApplication = this;
        SpUtil.getInstance().init(lockerApplication);

        screenWidth = AndroidTools.getScreenWidth(this);
        screenHeight = AndroidTools.getScreenHeight(this);
    }
}
