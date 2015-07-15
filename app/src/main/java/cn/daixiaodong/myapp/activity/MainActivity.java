package cn.daixiaodong.myapp.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.fragment.CollectFragment;
import cn.daixiaodong.myapp.fragment.HomeFragment;
import cn.daixiaodong.myapp.fragment.MessageFragment;
import cn.daixiaodong.myapp.fragment.SettingsFragment;


/**
 * 应用主界面
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private int mCurrentPosition = R.id.navigation_item_1;

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;

    @ViewById(R.id.id_layout_drawer_layout)
    DrawerLayout mDrawerLayout;


    @ViewById(R.id.id_nv_navigation)
    NavigationView mViewNav;

    private ArrayList<Fragment> mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            initData();
            Log.i("tag", "savedInstanceState == null");
        } else {
            mFragments = (ArrayList<Fragment>) savedInstanceState.getSerializable("list");
            Log.i("tag", savedInstanceState.toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("tag", "onSaveInstanceState");
        outState.putSerializable("list", mFragments);
    }

    @AfterViews
    void init() {

        // initData();
        initToolbar();
        initNav();
        setCurrentFragment("首页", R.id.navigation_item_1);
        Log.i("tag", "Activity被创建了");
        //Log.i("Fragment",)

    }

    private void initData() {
        mFragments = new ArrayList<>();
    }

    private void initNav() {
        mViewNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (mCurrentPosition != menuItem.getItemId()) {

                    setCurrentFragment(menuItem.getTitle().toString(), menuItem.getItemId());
                    setCurrentTitle(menuItem.getTitle().toString());
                    mCurrentPosition = menuItem.getItemId();
                    menuItem.setChecked(true);
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void setCurrentTitle(String s) {
        mViewToolbar.setTitle(s);
    }

    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mViewToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mViewToolbar.setTitleTextColor(Color.WHITE);
    }


    private void setCurrentFragment(String title, int itemId) {

        Log.i("tag", "setCurrentFragment");
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment mCurrentFragment = fragmentManager.findFragmentByTag(title);
        // 将集合中的fragment隐藏
        for (Fragment fragment : mFragments) {

            fragmentManager.beginTransaction().hide(fragment).commit();

        }
        if (mCurrentFragment == null) {
            switch (itemId) {
                // 首页
                case R.id.navigation_item_1:
                    Log.i("tag", "case 0");
                    mCurrentFragment = new HomeFragment();
                    break;
                // 收藏
                case R.id.navigation_item_2:
                    Log.i("tag", "case 1");
                    mCurrentFragment = new CollectFragment();
                    break;
                // 消息
                case R.id.navigation_item_3:
                    Log.i("tag", "case 2");
                    mCurrentFragment = new MessageFragment();
                    break;

                // 设置
                case R.id.navigation_item_4:
                    Log.i("tag", "case 3");
                    mCurrentFragment = new SettingsFragment();
                    break;

            }

            // 将mCurrentFragment对象引用压入fragment管理栈
            fragmentManager.beginTransaction().add(R.id.id_layout_content_container, mCurrentFragment, title).commit();
            // 存入集合中
            mFragments.add(mCurrentFragment);
        } else {

            // 显示找到的fragment视图
            fragmentManager.beginTransaction().show(mCurrentFragment).commit();
        }
    }

}
