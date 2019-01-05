package com.example.myhelper.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myhelper.R;
import com.example.myhelper.adapter.StockAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    private SearchView mSearchView;
    private StockAdapter stockAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_stock;
    }

    @Override
    protected void init() {
        super.init();
        setToolbar("库存",true);
        initRv();
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter = new StockAdapter();
        rv.setAdapter(stockAdapter);
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
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
