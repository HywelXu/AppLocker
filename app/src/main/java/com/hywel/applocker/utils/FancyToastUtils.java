package com.hywel.applocker.utils;

import com.hywel.applocker.LockerApplication;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Author: Hywel
 * Time: 2019-08-29
 * Function: Toast 辅助类
 * <p>Copyright 2019 GRZQ.</p>
 */
public class FancyToastUtils {

    public static void showNormalToast(String pMsg) {
        TastyToast.makeText(LockerApplication.getInstance(), pMsg, TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
    }

    public static void showWarningToast(String pMsg) {
        TastyToast.makeText(LockerApplication.getInstance(), pMsg, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
    }

    public static void showErrorToast(String pMsg) {
        TastyToast.makeText(LockerApplication.getInstance(), pMsg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    public static void showSuccessToast(String pMsg) {
        TastyToast.makeText(LockerApplication.getInstance(), pMsg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }


}
