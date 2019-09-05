package com.hywel.applocker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.model.SimpleAppInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class SpUtil {
    private static SpUtil mInstance;
    private static final String SP_NAME = "hywel_app_locker";

    private SharedPreferences mPref;
    private List<SimpleAppInfo> lockedNames = new ArrayList<>();

    //============sp key=====================

    public static final String SP_KEY_APP_IS_FIRST_LOCK_APP = "app_is_first_lock_app";
    private static final String SP_KEY_APP_LOCKER_IS_OPEN = "app_locker_is_open";
    private static final String SP_KEY_APP_IS_FIRST_IN = "app_is_first_in";
    private static final String SP_KEY_APP_VERIFY_CODE = "app_verify_code";
    private static final String SP_KEY_LOCKED_PACK_NAME = "locked_pack_name";
    private static final String SP_KEY_LOCKED_PACKAGE_NAME = "locked_package_name";

    private SpUtil() {
        mPref = LockerApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public synchronized static SpUtil getInstance() {
        if (null == mInstance) {
            synchronized (SpUtil.class) {
                if (null == mInstance) {
                    mInstance = new SpUtil();
                }
            }
        }
        return mInstance;
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
        HashSet<String> packNames = getPackNameSet();
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
        HashSet<String> packNames = getPackNameSet();
        Gson gson = new Gson();
        if (packNames.contains(packName)) packNames.remove(packName);
        putString(SP_KEY_LOCKED_PACK_NAME, gson.toJson(packNames));
    }

    /**
     * 获取已上锁的所有 App 的包名
     *
     * @return 所有 App 的包名set
     */
    public HashSet<String> getPackNameSet() {
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
    public void saveLockedPackNameItem(SimpleAppInfo dataValue) {
        Gson gson = new Gson();
        lockedNames = getLockedPackNameList();
        if (!AndroidTools.isListValidate(lockedNames)) {
            lockedNames = new ArrayList<>();
            lockedNames.add(dataValue);
        } else {
            int vContainTheItem = isContainTheItem(lockedNames, dataValue);
            if (vContainTheItem < 0) {//之前不存在
                lockedNames.add(dataValue);
            }
        }
//        putString(SP_KEY_LOCKED_PACKAGE_NAME, gson.toJson(lockedNames));
        Editor edit = mPref.edit();
        edit.putString(SP_KEY_LOCKED_PACKAGE_NAME, gson.toJson(lockedNames));
        edit.commit();
    }

    /**
     * 获取要上锁的应用包名集合
     *
     * @return 要上锁的应用包名集合
     */
    public List<SimpleAppInfo> getLockedPackNameList() {
        List<SimpleAppInfo> vAppInfos = new ArrayList<>();
        Gson gson = new Gson();
        try {
            String vJson = getString(SP_KEY_LOCKED_PACKAGE_NAME, "");
            vAppInfos = gson.fromJson(vJson,
                    new TypeToken<ArrayList<SimpleAppInfo>>() {
                    }.getType());
            Log.e("LockService", "vJson: " + vJson);
//            Log.e("LockService", "vAppInfos: " + vAppInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vAppInfos;
    }

    /**
     * 恢复锁定应用的锁定状态
     */
    public static void resetLockedPackageState() {
        List<SimpleAppInfo> vLockedPackNameList = SpUtil.getInstance().getLockedPackNameList();
        if (AndroidTools.isListValidate(vLockedPackNameList)) {
            for (SimpleAppInfo vSimpleAppInfo : vLockedPackNameList) {
                vSimpleAppInfo.setUnLock(false);
            }
            SpUtil.getInstance().saveRestAppInfos(vLockedPackNameList);
        }
    }

    /**
     * 移除不需要上锁的应用包名
     *
     * @param value 不需要上锁的应用包名
     */
    public void removeLockedPackNameItem(SimpleAppInfo value) {
        List<SimpleAppInfo> vLockedPackNameList = getLockedPackNameList();
        int containTheItem = isContainTheItem(vLockedPackNameList, value);
        Log.e("LockService", "containTheItem: " + containTheItem);
        if (containTheItem >= 0 && containTheItem < vLockedPackNameList.size()) {
            vLockedPackNameList.remove(containTheItem);
            saveRestAppInfos(vLockedPackNameList);
        }
    }

    /**
     * 重新保存下应用列表
     *
     * @param pAppInfoList 应用列表
     */
    public void saveRestAppInfos(List<SimpleAppInfo> pAppInfoList) {
        Gson vGson = new Gson();
        Editor edit = mPref.edit();
        edit.putString(SP_KEY_LOCKED_PACKAGE_NAME, vGson.toJson(pAppInfoList));
        edit.commit();
    }

    /**
     * 是否包含某个已上锁的应用包名
     *
     * @param lockedNames 上锁的应用包名集合
     * @param info        应用包名
     * @return 此应用在集合中的位置
     */
    public int isContainTheItem(List<SimpleAppInfo> lockedNames, SimpleAppInfo info) {
        for (int i = 0; i < lockedNames.size(); i++) {
            if (lockedNames.get(i).getPackageName().equals(info.getPackageName())) {
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
        List<SimpleAppInfo> lockedPackNames = getLockedPackNameList();
        for (int i = 0; i < lockedPackNames.size(); i++) {
            if (lockedPackNames.get(i).getId() == id) {
                Log.d("SpUtil", "id: " + id);
                return true;
            }
        }
        return false;
    }
}
