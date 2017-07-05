package com.hywel.applocker.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.hywel.applocker.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hywel on 2017/7/5.
 */

public class LoadApkUtils {
    /**
     * 获取手机中的所有应用的信息
     *
     * @param context context
     */
    public static void getInstalledApplications(Context context) {
        // 获取到包的管理者
        PackageManager packageManager = context.getPackageManager();
        // 获取所有的安装程序
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        // 遍历获取到每个应用程序的信息
        for (PackageInfo packageInfo : installedPackages) {
            // 获取到程序的包名
            String packageName = packageInfo.packageName;
            // 获取到版本号
            String versionName = packageInfo.versionName;

            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            // 获取程序名
            String appName = applicationInfo.loadLabel(packageManager).toString();
            // 获取到程序图标
            Drawable icon = applicationInfo.loadIcon(packageManager);

            // 获取程序的所有标签 用来获取 以下信息
            int flags = applicationInfo.flags;
            // 判断是不是用户程序
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                // 系统程序
            } else {
                // 用户程序
            }
            // 判断是不是安装在哪
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
                // 内存卡
            } else {
                // 手机内存
            }
        }
    }

    /**
     * 获取所有用户安装的应用
     */
    public static List<PackageInfo> getAllUserApps(Context context) {
        List<PackageInfo> apps = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> applications = packageManager.getInstalledPackages(0);
        for (PackageInfo info : applications) {
            if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {
                apps.add(info);
            }
        }
        return apps;
    }

    /**
     * 获取所有用户安装应用的应用信息
     */
    public static List<AppInfo> getAllUserAppInfos(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> applications = packageManager.getInstalledPackages(0);
        for (PackageInfo info : applications) {
            if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {
                AppInfo appInfo = new AppInfo();

                // 获取到程序的包名
                String packageName = info.packageName;
                // 获取到版本号
                String versionName = info.versionName;

                ApplicationInfo applicationInfo = info.applicationInfo;
                // 获取程序名
                String appName = applicationInfo.loadLabel(packageManager).toString();
                // 获取到程序图标
                Drawable icon = applicationInfo.loadIcon(packageManager);
//                int uid = applicationInfo.uid;

                appInfo.setAppName(appName);
                appInfo.setAppDrawable(icon);
                appInfo.setPackageName(packageName);
                appInfo.setVersionName(versionName);
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }

    /**
     * 获取所有系统自带应用的应用信息
     */
    public static List<AppInfo> getAllSysAppInfos(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> applications = packageManager.getInstalledPackages(0);
        for (PackageInfo info : applications) {
            if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) > 0) {
                AppInfo appInfo = new AppInfo();

                // 获取到程序的包名
                String packageName = info.packageName;
                // 获取到版本号
                String versionName = info.versionName;

                ApplicationInfo applicationInfo = info.applicationInfo;
                // 获取程序名
                String appName = applicationInfo.loadLabel(packageManager).toString();
                // 获取到程序图标
                Drawable icon = applicationInfo.loadIcon(packageManager);
//                int uid = applicationInfo.uid;

                appInfo.setAppName(appName);
                appInfo.setAppDrawable(icon);
                appInfo.setPackageName(packageName);
                appInfo.setVersionName(versionName);
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }

    /**
     * 获取所有系统自带应用
     */
    public static List<PackageInfo> getAllSysApps(Context context) {
        List<PackageInfo> apps = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> applications = packageManager.getInstalledPackages(0);
        for (PackageInfo info : applications) {
            if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) > 0) {
                apps.add(info);
            }
        }
        return apps;
    }

    /**
     * 通过应用名获取包名
     *
     * @param name 应用名
     * @return 包名
     */
    public static String getPackageNameByAppName(Context context, @NonNull String name) {
        try {
            if (TextUtils.isEmpty(name)) {
                return null;
            }
            // 获取到包的管理者
            PackageManager packageManager = context.getPackageManager();
            // 获取所有的安装程序
            List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
            // 遍历获取到每个应用程序的信息
            for (PackageInfo packageInfo : installedPackages) {
                // 获取程序名
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                if (name.equals(appName)) {
                    return packageInfo.packageName;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 启动app
     *
     * @param context        context
     * @param appPackageName 应用包名
     */
    public static void startAPP(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "没有安装", Toast.LENGTH_SHORT).show();
        }
    }
}
