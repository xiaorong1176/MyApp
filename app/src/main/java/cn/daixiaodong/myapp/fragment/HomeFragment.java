package cn.daixiaodong.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.CreateDreamActivity_;
import cn.daixiaodong.myapp.adapter.ViewPagerAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;


/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    TabLayout tabLayout;

    ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init();  无效
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    void init() {
        tabLayout = (TabLayout) getActivity().findViewById(R.id.id_tl_tab_layout);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.id_vp_view_pager);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_create:
                CreateDreamActivity_.intent(getActivity()).start();
                break;
        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        adapter.addFragment(new DreamListFragment(), "首页");
        adapter.addFragment(new JoinListFragment(), "我参加的");
        adapter.addFragment(new CreateListFragment(), "我发起的");
        viewPager.setAdapter(adapter);
    }

}
