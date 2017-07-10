package com.hywel.applocker.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by hywel on 2017/7/3.
 */

public class AppInfo implements Serializable {
    private long id;
    private String appName;//应用名
    private String packageName;//包名
    private String versionName;//版本号
    private String appIcon;//应用图标地址
    private Drawable appDrawable;//应用图标
    private boolean isLocked;//是否已锁
    private boolean isRecommedLocked;//是否推荐加锁
    private boolean isSetUnLock;

    public AppInfo() {
    }

    public AppInfo(String appName, String appIcon, boolean isLocked) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.isLocked = isLocked;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getAppDrawable() {
        return appDrawable;
    }

    public void setAppDrawable(Drawable appDrawable) {
        this.appDrawable = appDrawable;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isRecommedLocked() {
        return isRecommedLocked;
    }

    public void setRecommedLocked(boolean recommedLocked) {
        isRecommedLocked = recommedLocked;
    }

    public boolean isSetUnLock() {
        return isSetUnLock;
    }

    public void setSetUnLock(boolean setUnLock) {
        isSetUnLock = setUnLock;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", appIcon='" + appIcon + '\'' +
                ", appDrawable=" + appDrawable +
                ", isLocked=" + isLocked +
                ", isRecommedLocked=" + isRecommedLocked +
                ", isSetUnLock=" + isSetUnLock +
                '}';
    }
}
