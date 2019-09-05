package com.hywel.applocker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.R;
import com.hywel.applocker.fragment.SysAppFragment;
import com.hywel.applocker.fragment.UserAppFragment;
import com.hywel.applocker.model.AppInfo;
import com.hywel.applocker.model.SimpleAppInfo;
import com.hywel.applocker.service.LockService;
import com.hywel.applocker.service.LockServiceConnection;
import com.hywel.applocker.utils.AndroidTools;
import com.hywel.applocker.utils.BusinessHelper;
import com.hywel.applocker.utils.LoadApkUtils;
import com.hywel.applocker.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 获取手机中所安装的所有应用
 * 1.上滑特效 2.获取手机应用集合 3.搜索功能
 */
public class AppActivity extends BaseActivity {
    @BindView(R.id.viewpager_apps)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.edit_search_tv)
    EditText mEditSearchTV;

    private List<String> fragmentTitles;
    private List<Fragment> fragments;

    private SysAppFragment mSystemSysAppFragment;
    private UserAppFragment mUserAppListFragment;

    private int sysAppCount;
    private int userAppCount;
    private boolean hasSearched;
    private ArrayList<AppInfo> mAllSysAppList;
    private ArrayList<AppInfo> mAllUserAppList;
    private LockServiceConnection connection;


    @Override
    protected int setRightTitleBarIcon() {
        return R.mipmap.ic_setting;
    }

    @Override
    protected void onRightMenuClicked(View view) {
        gotoSetting();
    }

    /**
     * 进入设置页
     */
    public void gotoSetting() {
        BusinessHelper.getInstance().transferPageWithAnim(AppActivity.this, SettingActivity.class, R.anim.slide_in_from_right, R.anim.slide_out_from_left);
    }

    @Override
    protected void makeActions() {
//        startService(new Intent(AppActivity.this, LockService.class));
        connection = new LockServiceConnection();
        bindService(new Intent(this, LockService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void renderData() {
        setAppList();
        setViewPager();
    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
        BusinessHelper.getInstance().transferPageWithAnim(AppActivity.this, PwdVerifyActivity.class, R.anim.drop_in, R.anim.drop_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_app;
    }

    private void setAppList() {
        mAllSysAppList = LockerApplication.allSysAppInfos;
        mAllUserAppList = LockerApplication.allUserAppInfos;
        sysAppCount = mAllSysAppList.size();
        userAppCount = mAllUserAppList.size();

        //渲染已锁定应用的状态
        List<SimpleAppInfo> vLockedPackNameList = SpUtil.getInstance().getLockedPackNameList();
        AndroidTools.renderLockedElements(LockerApplication.allUserAppInfos, vLockedPackNameList);
        AndroidTools.renderLockedElements(LockerApplication.allSysAppInfos, vLockedPackNameList);
    }

    /**
     * 设置 ViewPager
     */
    private void setViewPager() {
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("用户应用（" + userAppCount + "）");
        fragmentTitles.add("系统应用（" + sysAppCount + "）");

        fragments = new ArrayList<>();
        mSystemSysAppFragment = SysAppFragment.newInstance(mAllSysAppList);
        mUserAppListFragment = UserAppFragment.newInstance(mAllUserAppList);
        fragments.add(mUserAppListFragment);
        fragments.add(mSystemSysAppFragment);

        mViewPager.setAdapter(new AppFragmentAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public static final int[] drawables = {R.drawable.sell_selector, R.drawable.rent_selector};

    public View getTabView(CharSequence text, int index) {
        View item = View.inflate(this, R.layout.item_maptab, null);
        TextView textView = item.findViewById(R.id.tabNameId);
        textView.setBackgroundResource(drawables[index]);
        textView.setText(text);
        return item;
    }

    /**
     * 处理搜索功能
     */
    public void handleSearch(View view) {
        hasSearched = true;
        animSearchLayout(hasSearched);
        searchMethod();
    }

    int clearSearchFlags = 0;

    private void searchMethod() {
        mEditSearchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchFlags++;
                if (clearSearchFlags == 2) {
                    mEditSearchTV.setText("");
                    clearSearchFlags = 0;
                }
            }
        });

        mEditSearchTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clearSearchFlags = 0;
                String vS = s.toString().trim();
                if (!TextUtils.isEmpty(vS)) {
                    performSearchingApp(vS);
                }
            }
        });
    }

    /**
     * 执行搜索
     *
     * @param pS 输入的内容
     */
    private void performSearchingApp(String pS) {
        boolean vVisible = mUserAppListFragment.isVisible();
        //当前界面在「用户应用」界面
        if (vVisible) {
            int vTargetAppInApps = LoadApkUtils.findTargetAppInApps(mAllUserAppList, pS);
            if (vTargetAppInApps > 0) {
                mUserAppListFragment.highLightItem(vTargetAppInApps);
            }
        } else {//当前界面在「系统应用」界面
            int vTargetAppInApps = LoadApkUtils.findTargetAppInApps(mAllSysAppList, pS);
            if (vTargetAppInApps > 0) {
                mSystemSysAppFragment.highLightItem(vTargetAppInApps);
            }
        }
    }

    /**
     * 用以切换区域配色
     *
     * @param hasSearched 是否点击过搜索按钮
     */
    private void animSearchLayout(boolean hasSearched) {
        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.drop_in);
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.drop_out);
        if (hasSearched) {
            mHTitleHeaderView.setPswPanelText("搜索应用");
            mHTitleHeaderView.setTitleBarLayoutBgColorRes(R.drawable.shape_password_panel_header_search_mode);
            mEditSearchTV.setBackgroundResource(R.drawable.bg_frame_search_dark);
//            mEditSearchTV.startAnimation(animationIn);
        } else {
            mHTitleHeaderView.setPswPanelText("加密应用");
            mHTitleHeaderView.setTitleBarLayoutBgColorRes(R.drawable.shape_password_panel_header_applayout);
            mEditSearchTV.setBackgroundResource(R.drawable.bg_frame_search);
//            mEditSearchTV.startAnimation(animationOut);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (hasSearched) {
                    animSearchLayout(false);
                    hasSearched = false;
//                    AndroidTools.showKeyboard(this, mEditSearchTV);
                } else {
                    onBackPressed();
                }
                break;

        }
        return true;

    }


    public void onBackArrowClicked(View view) {
        onBackPressed();
    }

    private class AppFragmentAdapter extends FragmentPagerAdapter {

        AppFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

}
