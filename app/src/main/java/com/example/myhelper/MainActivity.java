package com.example.myhelper;

import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myhelper.activity.BaseActivity;
import com.example.myhelper.adapter.MyPagerAdapter;
import com.example.myhelper.fragment.StockFragment;
import com.example.myhelper.entity.TabEntity;
import com.example.myhelper.fragment.ChartFragment;
import com.example.myhelper.fragment.CustomerFragment;
import com.example.myhelper.fragment.HomeFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tablayout)
    CommonTabLayout tablayout;
    private SearchView mSearchView;

    private String[] mTitles = {"首页", "库存", "客户", "报表"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    private MyPagerAdapter mAdapter;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();

        setToolbar("",false);
        initTablayout();
        initViewPager();
    }

    private void initViewPager() {
        for (int i = 0; i < mTitles.length; i++) {
            mFragments.add(new HomeFragment());
            mFragments.add(new StockFragment());
            mFragments.add(new CustomerFragment());
            mFragments.add(new ChartFragment());
        }
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(),mTitles,mFragments);
        viewpager.setAdapter(mAdapter);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setOffscreenPageLimit(3);
        viewpager.setCurrentItem(0);
    }

    private void initTablayout() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        tablayout.setTabData(mTabEntities);
        tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }

    public void setCurrentItem(int postion){
        viewpager.setCurrentItem(postion);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        initMenu();
        return super.onCreateOptionsMenu(menu);
    }

    private void initMenu() {
        mSearchView.setQueryHint("查找酵素");
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setFocusableInTouchMode(true);
        mSearchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
