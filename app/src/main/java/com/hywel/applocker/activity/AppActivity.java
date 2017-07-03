package com.hywel.applocker.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.hywel.applocker.R;
import com.hywel.applocker.fragment.SysAppFragment;
import com.hywel.applocker.fragment.UserAppFragment;
import com.hywel.applocker.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 获取手机中所安装的所有应用
 * todo 1.上滑特效 2.获取手机应用集合 3.搜索功能
 */
public class AppActivity extends FragmentActivity {
    @BindView(R.id.app_search_view)
    SearchView mSearchView;
    @BindView(R.id.viewpager_apps)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private List<String> fragmentTitles;
    private List<Fragment> fragments;
    private List<AppInfo> mAppInfos;

    private SysAppFragment mSystemSysAppFragment;
    private UserAppFragment mUserSystemAppListFragment;

    private int sysAppCount;
    private int userAppCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);

        setViewPager();
    }

    private void setViewPager() {
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("系统应用（" + getAppInfos().size() + "）");
        fragmentTitles.add("用户应用（" + getAppInfos().size() + "）");

        fragments = new ArrayList<>();
        mSystemSysAppFragment = SysAppFragment.newInstance(getAppInfos());
        mUserSystemAppListFragment = UserAppFragment.newInstance(getAppInfos());
        fragments.add(mSystemSysAppFragment);
        fragments.add(mUserSystemAppListFragment);

        mViewPager.setAdapter(new AppFragmentAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public List<AppInfo> getAppInfos() {
        mAppInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AppInfo info = new AppInfo();
            info.setAppName("App" + i);
            mAppInfos.add(info);
        }
        return mAppInfos;
    }

    public void handleSearch(View view) {
        Toast.makeText(this, "todo", Toast.LENGTH_SHORT).show();
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
