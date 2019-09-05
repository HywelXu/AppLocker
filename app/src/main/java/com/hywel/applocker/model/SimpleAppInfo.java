package com.hywel.applocker.model;

/**
 * Author: Hywel
 * Time: 2019-09-02
 * Function: 保存到本地的 APP 上锁必要信息
 * <p>Copyright 2019 GRZQ.</p>
 */
public class SimpleAppInfo {
    private long id;
    private String appName;//应用名
    private String packageName;//包名
    private boolean isSetUnLock;//是否已校验解锁

    public SimpleAppInfo() {
    }

    public static SimpleAppInfo buildSimpleAppInfo(AppInfo pAppInfo) {
        SimpleAppInfo vSimpleAppInfo = new SimpleAppInfo();
        if (pAppInfo != null) {
            vSimpleAppInfo.setId(pAppInfo.getId());
            vSimpleAppInfo.setAppName(pAppInfo.getAppName());
            vSimpleAppInfo.setPackageName(pAppInfo.getPackageName());
            vSimpleAppInfo.setUnLock(pAppInfo.isSetUnLock());
        }
        return vSimpleAppInfo;
    }

    public SimpleAppInfo(long pId, String pAppName, String pPackageName, boolean pIsSetUnLock) {
        id = pId;
        appName = pAppName;
        packageName = pPackageName;
        isSetUnLock = pIsSetUnLock;
    }

    public boolean isSetUnLock() {
        return isSetUnLock;
    }

    public void setUnLock(boolean pSetUnLock) {
        isSetUnLock = pSetUnLock;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String pAppName) {
        appName = pAppName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String pPackageName) {
        packageName = pPackageName;
    }
}
