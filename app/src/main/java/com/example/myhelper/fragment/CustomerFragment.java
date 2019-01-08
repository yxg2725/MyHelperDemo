package com.example.myhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.activity.AddCustomerActivity;
import com.example.myhelper.adapter.CoustomerAdapter;
import com.example.myhelper.entity.ContactInfo;
import com.example.myhelper.entity.Customer;
import com.example.myhelper.widget.Cheeses;
import com.example.myhelper.widget.QuickIndexBar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/2.
 */

public class CustomerFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "CustomerFragment";

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.qib)
    QuickIndexBar qib;
    @BindView(R.id.tv_show_letter)
    TextView tvShowLetter;
    private SearchView mSearchView;
    private CoustomerAdapter coustomerAdapter;
    private List<ContactInfo> contacts=new ArrayList<ContactInfo>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_customer;
    }

    @Override
    protected void loadData() {
        contacts.clear();
        //查询联系人表
        List<Customer> customers = LitePal.findAll(Customer.class);
        Log.i(TAG, "customers size: " + customers.size());
        for (Customer customer : customers) {
            contacts.add(new ContactInfo(customer.getName()));
        }

        //让ContactInfo具有可排序性,让Collections对ContactInfo集合进行排序
        Collections.sort(contacts);

        if (coustomerAdapter == null) {
            rv.setLayoutManager(new LinearLayoutManager(mActivity));
            coustomerAdapter = new CoustomerAdapter(mActivity);
            rv.setAdapter(coustomerAdapter);
        }
        coustomerAdapter.setDatas(contacts);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        init();
    }


    private void init() {
        qib.setOnLetterChangedListener(new QuickIndexBar.OnLetterChangedListener() {
            private String letter;

            @Override
            public void onLetterChanged(String letter) {
                this.letter = letter;
                tvShowLetter.setVisibility(View.VISIBLE);
                tvShowLetter.setText(letter);
            }

            @Override
            public void onLetterDismiss() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvShowLetter.setVisibility(View.GONE);

                        //Recyclerview  自动滚动到指定位置
                        int position = -1;
                        for (int i = 0; i < contacts.size(); i++) {
                            if (contacts.get(i).getFirstLetter().equals(letter)) {
                                position = i;
                                break;
                            }
                        }

                        if (position != -1) {
                            rv.smoothScrollToPosition(position);
                        }

                    }
                }, 200);
            }
        });



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.category_menu, menu);
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
        if (item.getItemId() == R.id.add) {
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
