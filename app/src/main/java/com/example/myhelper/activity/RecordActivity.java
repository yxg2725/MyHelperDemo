package com.example.myhelper.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myhelper.R;
import com.example.myhelper.adapter.RecordAdapter;
import com.example.myhelper.entity.MyOrder;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordActivity extends BaseActivity implements SearchView.OnQueryTextListener {


    @BindView(R.id.segmentTabLayout)
    SegmentTabLayout stl;
    @BindView(R.id.rv)
    RecyclerView rv;
    private SearchView mSearchView;
    private String[] mTitles = {"全部", "出库", "入库"};
    private List<MyOrder> orderList = new ArrayList<>();
    private RecordAdapter recordAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_record;
    }


    @Override
    protected void init() {
        super.init();
        setToolbar("订单记录", true);
        stl.setTabData(mTitles);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        stl.setCurrentTab(Integer.valueOf(position));


        int currentTab = stl.getCurrentTab();
        setRvData(currentTab);
        stl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                setRvData(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        initRv();
    }

    private void setRvData(int position) {
        orderList.clear();
        switch (position){
            case 0:
                List<MyOrder> list = LitePal.findAll(MyOrder.class);
                orderList.addAll(list);
                break;
            case 1:
                List<MyOrder> list1 = LitePal.where("state=?", "0").find(MyOrder.class);
                orderList.addAll(list1);
                break;
            case 2:
                List<MyOrder> list2 = LitePal.where("state=?", "1").find(MyOrder.class);
                orderList.addAll(list2);
                break;

        }

        notifyData();
    }

    private void initRv() {
        notifyData();

    }
    private void notifyData(){
        if (recordAdapter == null){
            rv.setLayoutManager(new LinearLayoutManager(this));
            recordAdapter = new RecordAdapter(this);
            rv.setAdapter(recordAdapter);
        }
        recordAdapter.setDatas(orderList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        initMenu();

        return super.onCreateOptionsMenu(menu);

    }


    private void initMenu() {
        mSearchView.setQueryHint("客户/订单状态");
//        mSearchView.setMaxWidth(400);
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {

        }
        return super.onOptionsItemSelected(item);

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
