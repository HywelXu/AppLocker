package com.hywel.applocker;

import android.app.Application;

import com.hywel.applocker.utils.AndroidTools;

/**
 * Created by hywel on 2017/6/30.
 */

public class LockerApplication extends Application {
    public LockerApplication lockerApplication;
    public static int screenWidth;
    public static int screenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        lockerApplication = this;

        screenWidth = AndroidTools.getScreenWidth(this);
        screenHeight = AndroidTools.getScreenHeight(this);
    }

}
