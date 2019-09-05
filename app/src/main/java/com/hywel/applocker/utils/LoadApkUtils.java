package com.hywel.applocker.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.model.AppInfo;
import com.hywel.applocker.model.SimpleAppInfo;

import java.util.ArrayList;
import java.util.HashSet;
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
     * 获取手机所有的应用
     *
     * @return PackageInfo
     */
    public static List<PackageInfo> getAllApplications(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // 获取所有的安装程序
        return packageManager.getInstalledPackages(0);
    }

    /**
     * 获取 sp 中所有应用的包名
     *
     * @param context
     * @return
     */
    public static List<String> getLockedAppPackNames(Context context) {
        List<String> allAppPackNames = new ArrayList<>();

        List<SimpleAppInfo> lockedPackNames = SpUtil.getInstance().getLockedPackNameList();
        for (int i = 0; i < lockedPackNames.size(); i++) {
            allAppPackNames.add(lockedPackNames.get(i).getPackageName());
        }
        return allAppPackNames;
    }

    /**
     * 初始化系统和用户应用列表
     *
     * @param pPackageManager PackageManager
     */
    public static void initSysAndUserAppLists(PackageManager pPackageManager) {
        List<PackageInfo> applications = LoadApkUtils.getAllApplications(LockerApplication.getInstance().getContext());
        for (int i = 0; i < applications.size(); i++) {
            PackageInfo info = applications.get(i);
            // 获取到程序的包名
            String packageName = info.packageName;
            // 获取到版本号
            String versionName = info.versionName;

            AppInfo appInfo = new AppInfo();
            ApplicationInfo applicationInfo = info.applicationInfo;
            // 获取程序名
            String appName = applicationInfo.loadLabel(pPackageManager).toString();
            // 获取到程序图标
            Drawable icon = applicationInfo.loadIcon(pPackageManager);
//                int uid = applicationInfo.uid;

            appInfo.setId((long) (1 + 2 * i));
//            appInfo.setAppIcon(icon);
            appInfo.setAppName(appName);
//                appInfo.setAppDrawable(((BitmapDrawable) icon).getBitmap());
            appInfo.setApplicationInfo(applicationInfo);
            appInfo.setPackageName(packageName);
            appInfo.setVersionName(versionName);
//                List<String> lockedAppPackNames = getLockedAppPackNames(context);
            HashSet<String> packNames = SpUtil.getInstance().getPackNameSet();
            if (packNames.contains(packageName)) {
                appInfo.setLocked(true);
            } else {
                appInfo.setLocked(false);
            }
            //拼接 list
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                LockerApplication.allSysAppInfos.add(appInfo);
                LockerApplication.allSysSimpleAppInfos.add(SimpleAppInfo.buildSimpleAppInfo(appInfo));
            } else {
                //将本 APP 剔除
                if (!LockerApplication.getInstance().getPackageName().equals(packageName)) {
                    LockerApplication.allUserAppInfos.add(appInfo);
                    LockerApplication.allUserSimpleAppInfos.add(SimpleAppInfo.buildSimpleAppInfo(appInfo));
                }
            }
        }
    }

//    private static Bitmap drawable2Bitmap(Drawable drawable) {
//        int w = drawable.getIntrinsicWidth();
//        int h = drawable.getIntrinsicHeight();
//        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//    }

    private static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        @SuppressLint("RestrictedApi") Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获取手机所有的应用
     *
     * @return AppInfo
     */
    public static List<AppInfo> getAllApps(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> applications = packageManager.getInstalledPackages(0);
        for (PackageInfo info : applications) {
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
//            appInfo.setAppIcon(icon);
//                appInfo.setAppDrawable(((BitmapDrawable) icon).getBitmap());
            appInfo.setApplicationInfo(applicationInfo);
            appInfo.setPackageName(packageName);
            appInfo.setVersionName(versionName);
            appInfos.add(appInfo);
        }
        return appInfos;
    }

    /**
     * 获取所有用户安装的应用
     */
    public static ArrayList<PackageInfo> getAllUserApps(Context context) {
        ArrayList<PackageInfo> apps = new ArrayList<>();
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
    public static ArrayList<AppInfo> getAllUserAppInfos(Context context) {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
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
//                appInfo.setAppIcon(icon);

//                appInfo.setAppDrawable(((BitmapDrawable) icon).getBitmap());
                appInfo.setApplicationInfo(applicationInfo);
                appInfo.setPackageName(packageName);
                appInfo.setVersionName(versionName);
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }


    public static List<String> getAllAppPackNames(Context context) {
        List<String> allAppPackNames = new ArrayList<>();
        List<AppInfo> allSysAppInfos = getAllApps(context);
        if (!AndroidTools.isListValidate(allSysAppInfos)) return null;
        for (AppInfo info : allSysAppInfos) {
            allAppPackNames.add(info.getPackageName());
        }
        return allAppPackNames;
    }

    /**
     * 获取所有系统自带应用的应用信息
     */
    public static ArrayList<AppInfo> getAllSysAppInfos(Context context) {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
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
//                appInfo.setAppDrawable(((BitmapDrawable) icon).getBitmap());
                appInfo.setApplicationInfo(applicationInfo);
                appInfo.setPackageName(packageName);
//                appInfo.setAppIcon(icon);
                appInfo.setVersionName(versionName);
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }

    /**
     * 获取所有系统自带应用
     */
    public static ArrayList<PackageInfo> getAllSysApps(Context context) {
        ArrayList<PackageInfo> apps = new ArrayList<>();
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

    /**
     * 根据应用名称或包名查询，返回 APP 位置
     *
     * @param pAppInfos APP 列表
     * @param pS        应用名称或包名
     * @return APP 位置
     */
    public static int findTargetAppInApps(List<?> pAppInfos, String pS) {

        if (AndroidTools.isListValidate(pAppInfos)) {
            for (int vI = 0; vI < pAppInfos.size(); vI++) {
                Object vO = pAppInfos.get(vI);
                String vAppName = "";
                String vPackageName = "";
                if (vO instanceof AppInfo) {
                    AppInfo vAppInfo = (AppInfo) vO;
                    vAppName = vAppInfo.getAppName();
                    vPackageName = vAppInfo.getPackageName();
                } else if (vO instanceof SimpleAppInfo) {
                    SimpleAppInfo vSimpleAppInfo = (SimpleAppInfo) vO;
                    vAppName = vSimpleAppInfo.getAppName();
                    vPackageName = vSimpleAppInfo.getPackageName();
                }

                if (pS.equals(vAppName) || pS.equals(vPackageName)) {
                    return vI;
                }
            }
        }

        return -1;
    }

}
