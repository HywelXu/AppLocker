package com.hywel.applocker.model;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by hywel on 2017/7/3.
 * 仿照 PackageInfo 进行序列化
 */

public class AppInfo implements Parcelable, Comparable<AppInfo> {
    private long id;
    private String appName;//应用名
    private String packageName;//包名
    private String versionName;//版本号
    private int appIcon;//应用图标地址

    //        private Bitmap appDrawable;//应用图标
    private ApplicationInfo applicationInfo;
    private boolean isLocked;//是否已锁
    private boolean isRecommedLocked;//是否推荐加锁
    private boolean isSetUnLock;//是否已校验解锁
    private boolean isHighLight;//是否高光

    @Override
    public boolean equals(Object vpO) {
        if (this == vpO) return true;
        if (vpO == null || getClass() != vpO.getClass()) return false;
        AppInfo vvAppInfo = (AppInfo) vpO;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return id == vvAppInfo.id &&
                    appIcon == vvAppInfo.appIcon &&
//                    isLocked == vvAppInfo.isLocked &&
//                    isRecommedLocked == vvAppInfo.isRecommedLocked &&
//                    isSetUnLock == vvAppInfo.isSetUnLock &&
//                    isHighLight == vvAppInfo.isHighLight &&
                    Objects.equals(appName, vvAppInfo.appName) &&
                    Objects.equals(packageName, vvAppInfo.packageName) &&
                    Objects.equals(versionName, vvAppInfo.versionName) &&
                    Objects.equals(applicationInfo, vvAppInfo.applicationInfo);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(id, appName, packageName, versionName, appIcon, applicationInfo, isLocked, isRecommedLocked, isSetUnLock, isHighLight);
        }
        return 0;
    }

    public AppInfo() {
    }


    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }


    protected AppInfo(Parcel in) {
        id = in.readLong();
        appName = in.readString();
        packageName = in.readString();
        versionName = in.readString();
        appIcon = in.readInt();
//        applicationInfo = in.readParcelable(ApplicationInfo.class.getClassLoader());
        applicationInfo = ApplicationInfo.CREATOR.createFromParcel(in);

        isLocked = in.readByte() != 0;
        isRecommedLocked = in.readByte() != 0;
        isSetUnLock = in.readByte() != 0;
        isHighLight = in.readByte() != 0;
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    public boolean isHighLight() {
        return isHighLight;
    }

    public void setHighLight(boolean pHighLight) {
        isHighLight = pHighLight;
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

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(int appIcon) {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(appName);
        dest.writeString(packageName);
        dest.writeString(versionName);
        dest.writeInt(appIcon);
        if (applicationInfo != null) {
            dest.writeInt(1);
            applicationInfo.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeByte((byte) (isLocked ? 1 : 0));
        dest.writeByte((byte) (isRecommedLocked ? 1 : 0));
        dest.writeByte((byte) (isSetUnLock ? 1 : 0));
        dest.writeByte((byte) (isHighLight ? 1 : 0));
    }


    @Override
    public int compareTo(@NonNull AppInfo o) {
        return packageName.compareTo(o.packageName);
    }
}
