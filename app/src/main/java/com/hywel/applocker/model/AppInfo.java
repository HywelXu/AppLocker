package com.hywel.applocker.model;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hywel on 2017/7/3.
 */

public class AppInfo implements Parcelable {
    private Long id;
    private String appName;//应用名
    private String packageName;//包名
    private String versionName;//版本号
    private String appIcon;//应用图标地址

    //        private Bitmap appDrawable;//应用图标
    private ApplicationInfo applicationInfo;
    private boolean isLocked;//是否已锁
    private boolean isRecommedLocked;//是否推荐加锁
    private boolean isSetUnLock;

    public AppInfo() {
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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


    public boolean getIsLocked() {
        return this.isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean getIsRecommedLocked() {
        return this.isRecommedLocked;
    }

    public void setIsRecommedLocked(boolean isRecommedLocked) {
        this.isRecommedLocked = isRecommedLocked;
    }

    public boolean getIsSetUnLock() {
        return this.isSetUnLock;
    }

    public void setIsSetUnLock(boolean isSetUnLock) {
        this.isSetUnLock = isSetUnLock;
    }


    public AppInfo(Long id, String appName, String packageName, String versionName, String appIcon,
                   boolean isLocked, boolean isRecommedLocked, boolean isSetUnLock) {
        this.id = id;
        this.appName = appName;
        this.packageName = packageName;
        this.versionName = versionName;
        this.appIcon = appIcon;
        this.isLocked = isLocked;
        this.isRecommedLocked = isRecommedLocked;
        this.isSetUnLock = isSetUnLock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.appName);
        dest.writeString(this.packageName);
        dest.writeString(this.versionName);
        dest.writeString(this.appIcon);
        dest.writeParcelable(this.applicationInfo, flags);
        dest.writeByte(this.isLocked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRecommedLocked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSetUnLock ? (byte) 1 : (byte) 0);
    }

    protected AppInfo(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.appName = in.readString();
        this.packageName = in.readString();
        this.versionName = in.readString();
        this.appIcon = in.readString();
        this.applicationInfo = in.readParcelable(ApplicationInfo.class.getClassLoader());
        this.isLocked = in.readByte() != 0;
        this.isRecommedLocked = in.readByte() != 0;
        this.isSetUnLock = in.readByte() != 0;
    }

    public static final Parcelable.Creator<AppInfo> CREATOR = new Parcelable.Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };
}
