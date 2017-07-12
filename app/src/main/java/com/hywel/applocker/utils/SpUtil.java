package com.hywel.applocker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hywel.applocker.model.AppInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class SpUtil {
    private static SpUtil mInstance;
    private static final String SP_NAME = "hywel_app_locker";

    private Context mContext;
    private SharedPreferences mPref;
    private List<AppInfo> lockedNames = new ArrayList<>();

    //============sp key=====================

    public static final String SP_KEY_APP_IS_FIRST_LOCK_APP = "app_is_first_lock_app";
    private static final String SP_KEY_APP_LOCKER_IS_OPEN = "app_locker_is_open";
    private static final String SP_KEY_APP_IS_FIRST_IN = "app_is_first_in";
    private static final String SP_KEY_APP_VERIFY_CODE = "app_verify_code";
    private static final String SP_KEY_LOCKED_PACK_NAME = "locked_pack_name";
    private static final String SP_KEY_LOCKED_PACKAGE_NAME = "locked_package_name";

    private SpUtil() {
    }

    private SpUtil(Context mContext) {
        this.mContext = mContext;
        mPref = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public synchronized static SpUtil getInstance(Context context) {
        if (null == mInstance) {
            synchronized (SpUtil.class) {
                if (null == mInstance) {
                    mInstance = new SpUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * @param context
     * @deprecated
     */
    //在AppBase里面初始化
    public void init(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (mPref == null) {
//            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            mPref = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        //=================================
        //解决 Gson 异常
        // <p>
        // java.lang.SecurityException: Can't make method constructor accessible
        //</p>
        //=================================
//        GsonBuilder builder = new GsonBuilder();
//        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
//        builder.excludeFieldsWithoutExposeAnnotation();
//        Gson gson = builder.create();

    }

    public void putString(String key, String value) {
        Editor editor = mPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        Editor editor = mPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        Editor editor = mPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = mPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return mPref.getBoolean(key, def);
    }

    public String getString(String key) {
        return mPref.getString(key, "");
    }

    public String getString(String key, String def) {
        return mPref.getString(key, def);
    }

    public long getLong(String key) {
        return mPref.getLong(key, 0);
    }

    public long getLong(String key, int defInt) {
        return mPref.getLong(key, defInt);
    }

    public int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    public long getInt(String key, int defInt) {
        return mPref.getInt(key, defInt);
    }

    public boolean contains(String key) {
        return mPref.contains(key);
    }


    public void remove(String key) {
        Editor editor = mPref.edit();
        editor.remove(key);
        editor.apply();
    }

    public void removeValue(String key, String value) {
        Editor editor = mPref.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clear() {
        Editor editor = mPref.edit();
        editor.clear();
        editor.commit();
    }

    public void saveIsFirstIn(boolean isFirstIn) {
        putBoolean(SP_KEY_APP_IS_FIRST_IN, isFirstIn);
    }

    public boolean isFirstIn() {
        return getBoolean(SP_KEY_APP_IS_FIRST_IN, true);
    }

    public void saveLockerIsOpen(boolean isOpen) {
        putBoolean(SP_KEY_APP_LOCKER_IS_OPEN, isOpen);
    }

    public boolean isLockerOpen() {
        return getBoolean(SP_KEY_APP_LOCKER_IS_OPEN, true);
    }

    /**
     * 存储解锁密码
     *
     * @param code
     */
    public void savePasswordPanelVerifyCode(String code) {
        putString(SP_KEY_APP_VERIFY_CODE, code);
    }

    /**
     * 获取存储密码
     *
     * @return
     */
    public String getPasswordPanelVerifyCode() {
        return getString(SP_KEY_APP_VERIFY_CODE);
    }

    /**
     * 保存已锁定 App 的包名
     *
     * @param packName 包名
     */
    public void savePackName(String packName) {
        HashSet<String> packNames = getPackNames();
        Gson gson = new Gson();
        packNames.add(packName);
        putString(SP_KEY_LOCKED_PACK_NAME, gson.toJson(packNames));
    }

    /**
     * 删除要解锁的 App 的包名
     *
     * @param packName 包名
     */
    public void delPackName(String packName) {
        HashSet<String> packNames = getPackNames();
        Gson gson = new Gson();
        if (packNames.contains(packName)) packNames.remove(packName);
        putString(SP_KEY_LOCKED_PACK_NAME, gson.toJson(packNames));
    }

    /**
     * 获取已上锁的所有 App 的包名
     *
     * @return 所有 App 的包名set
     */
    public HashSet<String> getPackNames() {
        HashSet<String> packNames = new HashSet<>();
        Gson gson = new Gson();
        String string = getString(SP_KEY_LOCKED_PACK_NAME);
        if (!TextUtils.isEmpty(string)) {
            packNames = gson.fromJson(string,
                    new TypeToken<HashSet<String>>() {
                    }.getType());
        }
        return packNames;
    }

    /**
     * 储存要上锁的应用包名
     *
     * @param dataValue 要上锁的应用包名
     */
    public void saveLockedPackNameItem(AppInfo dataValue) {
//        GsonBuilder builder = new GsonBuilder();
//        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
//        builder.excludeFieldsWithoutExposeAnnotation();
//        Gson gson = builder.create();
        Gson gson = new Gson();

        lockedNames = getLockedPackNames();
        if (!AndroidTools.isListValidate(lockedNames)) {
            lockedNames = new ArrayList<>();
        }
        List<String> lockedAppPackNames = new ArrayList<>();
        for (AppInfo info : lockedNames) {
            lockedAppPackNames.add(info.getPackageName());
        }
        if (!lockedAppPackNames.contains(dataValue.getPackageName())) {
            lockedNames.add(dataValue);
        }
        putString(SP_KEY_LOCKED_PACKAGE_NAME, gson.toJson(lockedNames));
    }

    /**
     * 获取要上锁的应用包名集合
     *
     * @return 要上锁的应用包名集合
     */
    public List<AppInfo> getLockedPackNames() {
//        GsonBuilder builder = new GsonBuilder();
//        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
//        builder.excludeFieldsWithoutExposeAnnotation();
//        Gson gson = builder.create();
        Gson gson = new Gson();
        try {
            String lockedPackNameJson = getString(SP_KEY_LOCKED_PACKAGE_NAME, "");
            if (!TextUtils.isEmpty(lockedPackNameJson)) {
                lockedNames = gson.fromJson(lockedPackNameJson,
                        new TypeToken<List<AppInfo>>() {
                        }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lockedNames;
    }

    /**
     * 移除不需要上锁的应用包名
     *
     * @param value 不需要上锁的应用包名
     */
    public void removeLockedPackNameItem(AppInfo value) {
        lockedNames = getLockedPackNames();
        List<String> lockedAppPackNames = new ArrayList<>();
        for (AppInfo info : lockedNames) {
            lockedAppPackNames.add(info.getPackageName());
        }
        int containTheItem = isContainTheItem(lockedAppPackNames, value);
        if (containTheItem >= 0 && containTheItem < lockedNames.size())
            lockedNames.remove(containTheItem);
    }

    /**
     * 是否包含某个已上锁的应用包名
     *
     * @param lockedNames 上锁的应用包名集合
     * @param info        应用包名
     * @return 此应用在集合中的位置
     */
    public int isContainTheItem(List<String> lockedNames, AppInfo info) {
        for (int i = 0; i < lockedNames.size(); i++) {
            if (lockedNames.get(i).equals(info.getPackageName())) {
                Log.d("SpUtil", "id: " + lockedNames.get(i));
                return i;
            }
        }
        return -1;
    }

    /**
     * 某个 APP 是否已被锁定
     *
     * @param id APP 的 id
     * @return
     */
    public boolean isTheAppLocked(long id) {
        List<AppInfo> lockedPackNames = getLockedPackNames();
        for (int i = 0; i < lockedPackNames.size(); i++) {
            if (lockedPackNames.get(i).getId() == id) {
                Log.d("SpUtil", "id: " + id);
                return true;
            }
        }
        return false;
    }
}
