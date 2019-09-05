package com.hywel.applocker.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.AnimRes;

import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.service.AppConstants;
import com.hywel.applocker.service.FindApkService;

/**
 * Author: Hywel
 * Time: 2019-09-03
 * Function:
 * <p>Copyright 2019 GRZQ.</p>
 */
public class BusinessHelper {

    private static BusinessHelper mBusinessHelper;
    private static Intent serviceIntent;

    private BusinessHelper() {
    }

    public static BusinessHelper getInstance() {
        synchronized (BusinessHelper.class) {
            if (mBusinessHelper == null) {
                mBusinessHelper = new BusinessHelper();
            }
        }
        return mBusinessHelper;
    }

    public void startApkService() {
        serviceIntent = new Intent(LockerApplication.getInstance(), FindApkService.class);
        LockerApplication.getInstance().startService(serviceIntent);
    }

    public void stopApkService() {
        if (null != serviceIntent) {
            LockerApplication.getInstance().stopService(serviceIntent);
        }
    }

    public void transferPage(Activity pFromActivity, Class<?> pToActivity) {
        pFromActivity.startActivity(new Intent(pFromActivity, pToActivity));
    }

    public void transferPageThenFinish(Activity pFromActivity, Class<?> pToActivity) {
        pFromActivity.startActivity(new Intent(pFromActivity, pToActivity));
        pFromActivity.finish();
    }

    public void transferPageWithAnimThenFinish(Activity pFromActivity, Class<?> pToActivity, @AnimRes int pAnimIn, @AnimRes int pAnimOut) {
        pFromActivity.startActivity(new Intent(pFromActivity, pToActivity));
        pFromActivity.overridePendingTransition(pAnimIn, pAnimOut);
        pFromActivity.finish();
    }

    public void transferPageWithAnim(Activity pFromActivity, Class<?> pToActivity, @AnimRes int pAnimIn, @AnimRes int pAnimOut) {
        pFromActivity.startActivity(new Intent(pFromActivity, pToActivity));
        pFromActivity.overridePendingTransition(pAnimIn, pAnimOut);
    }

    public void sendPwdLockReceiver(String mPackageName) {
        Intent vIntent = new Intent();
        vIntent.putExtra(AppConstants.LOCK_PACKAGE_NAME, mPackageName);
        vIntent.setAction(AppConstants.PWD_LOCK_BROAD_ACTION);
        LockerApplication.getInstance().sendBroadcast(vIntent);
    }

}
