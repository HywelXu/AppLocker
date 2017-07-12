package com.hywel.applocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.R;
import com.hywel.applocker.fragment.SysAppFragment;
import com.hywel.applocker.fragment.UserAppFragment;
import com.hywel.applocker.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机中所安装的所有应用
 * todo 1.上滑特效 2.获取手机应用集合 3.搜索功能
 */
public class AppActivity extends BaseActivity {
    SearchView mSearchView;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    TextView mEditSearchTV;

    private List<String> fragmentTitles;
    private List<Fragment> fragments;

    private SysAppFragment mSystemSysAppFragment;
    private UserAppFragment mUserAppListFragment;

    private int sysAppCount;
    private int userAppCount;
    private boolean hasSearched;
    private ArrayList<AppInfo> allSysAppInfos = new ArrayList<>();
    private ArrayList<AppInfo> allUserAppInfos = new ArrayList<>();


    @Override
    protected void setRightTitleBar() {
        mRightImageView.setImageResource(R.mipmap.setting);
        mRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSetting();
            }
        });
    }

    /**
     * 进入设置页
     */
    public void gotoSetting() {
        startActivity(new Intent(AppActivity.this, SettingActivity.class));
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
    }

    @Override
    protected void makeActions() {

    }

    @Override
    protected void renderData() {
        setAppList();
        setViewPager();
    }

    @Override
    protected void renderView(Bundle savedInstanceState) {
        injectView();
        mSearchView = (SearchView) findViewById(R.id.app_search_view);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_apps);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mEditSearchTV = (TextView) findViewById(R.id.edit_search_tv);
        mPswPanelHeader.setBackgroundResource(R.drawable.shape_password_panel_header_applayout);
        mPswPanelText.setText(getText(R.string.password_panel_all_apps_to_lock_tip));
    }

    @Override
    public int getInjectLayoutId() {
        return R.layout.activity_app;
    }

    private void setAppList() {
        //        allSysAppInfos = LoadApkUtils.getAllSysAppInfos(this);
//        allUserAppInfos = LoadApkUtils.getAllUserAppInfos(this);
        allSysAppInfos = LockerApplication.allSysAppInfos;
        allUserAppInfos = LockerApplication.allUserAppInfos;
        sysAppCount = allSysAppInfos.size();
        userAppCount = allUserAppInfos.size();
    }

    /**
     * 设置 ViewPager
     */
    private void setViewPager() {
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("系统应用（" + sysAppCount + "）");
        fragmentTitles.add("用户应用（" + userAppCount + "）");

        fragments = new ArrayList<>();
        mSystemSysAppFragment = SysAppFragment.newInstance(allSysAppInfos);
        mUserAppListFragment = UserAppFragment.newInstance(allUserAppInfos);
        fragments.add(mSystemSysAppFragment);
        fragments.add(mUserAppListFragment);

        mViewPager.setAdapter(new AppFragmentAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }


    /**
     * 处理搜索功能
     */
    public void handleSearch(View view) {
        hasSearched = true;
        Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
        animSearchLayout(hasSearched);
        searchMethod();
    }

    private void searchMethod() {
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(AppActivity.this, "query" + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)) {
//                    Toast.makeText(AppActivity.this, "newText" + newText, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(AppActivity.this, "newText is null", Toast.LENGTH_SHORT).show();
//                }
                return false;
            }
        });
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
            if (mSearchView.getVisibility() == View.GONE) {
                mSearchView.setVisibility(View.VISIBLE);
            }
            mPswPanelText.setText("搜索应用");
            mPswPanelHeader.setBackgroundResource(R.drawable.shape_password_panel_header_search_mode);
            mSearchView.setBackgroundResource(R.drawable.bg_frame_search_dark);
            mSearchView.startAnimation(animationIn);
            mEditSearchTV.startAnimation(animationOut);
            mEditSearchTV.setVisibility(View.GONE);
        } else {
            if (mEditSearchTV.getVisibility() == View.GONE) {
                mEditSearchTV.setVisibility(View.VISIBLE);
            }
            mPswPanelText.setText("加密应用");
            mPswPanelHeader.setBackgroundResource(R.drawable.shape_password_panel_header_applayout);
            mSearchView.setBackgroundResource(R.drawable.bg_frame_search);
            mSearchView.startAnimation(animationOut);
            mEditSearchTV.startAnimation(animationIn);
            mSearchView.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (hasSearched) {
                    animSearchLayout(false);
                    hasSearched = false;
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
