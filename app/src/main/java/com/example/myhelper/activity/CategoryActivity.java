package com.example.myhelper.activity;

import android.content.Intent;
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
import com.example.myhelper.adapter.CategoryAdapter;
import com.example.myhelper.entity.Product;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity implements SearchView.OnQueryTextListener {


    @BindView(R.id.rv)
    RecyclerView rv;
    private SearchView mSearchView;
    private CategoryAdapter categoryAdapter;
    private List<Product> mList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_category;
    }

    @Override
    protected void init() {
        super.init();
        setToolbar("品种管理",true);

    }


    private void initRv() {

        if (categoryAdapter == null){
            rv.setLayoutManager(new LinearLayoutManager(this));
            categoryAdapter = new CategoryAdapter(this,mList);
            rv.setAdapter(categoryAdapter);
        }else{
            categoryAdapter.setData(mList,false);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //查询Product数据库
        mList = LitePal.findAll(Product.class);
        initRv();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.category_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        initMenu();
        return super.onCreateOptionsMenu(menu);

    }

    private void initMenu() {
        mSearchView.setQueryHint("查找酵素");
        mSearchView.setMaxWidth(400);
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add){
            Intent intent = new Intent(this, AddCategoryActivity.class);
            startActivity(intent);
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
