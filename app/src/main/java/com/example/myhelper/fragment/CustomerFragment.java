package com.example.myhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.activity.AddCategoryActivity;
import com.example.myhelper.activity.AddCustomerActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/2.
 */

public class CustomerFragment extends BaseFragment implements SearchView.OnQueryTextListener {


    private SearchView mSearchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView textView = new TextView(mActivity);
        textView.setText("客户");
        textView.setGravity(Gravity.CENTER);
        return textView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.category_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        initMenu();
    }


    private void initMenu() {
        mSearchView.setQueryHint("查找客户");
        mSearchView.setMaxWidth(400);
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add){
            Intent intent = new Intent(mActivity, AddCustomerActivity.class);
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
