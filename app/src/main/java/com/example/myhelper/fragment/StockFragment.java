package com.example.myhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.activity.InStorageActivity;
import com.example.myhelper.activity.OutStorageActivity;
import com.example.myhelper.adapter.StockAdapter;
import com.example.myhelper.entity.Product;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/2.
 */

public class StockFragment extends BaseFragment {

    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.btn_out_storage)
    Button btnOutStorage;
    @BindView(R.id.btn_in_storage)
    Button btnInStorage;
    private StockAdapter stockAdapter;
    private List<Product> products;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stock;
    }

    @Override
    protected void loadData() {
        initData();
    }


    private void initData() {
        queryStorage();
        setData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setData() {
        int count = 0;
        for (Product product : products) {
            count = count + product.getCount();
        }
        tvTotalCount.setText(count+"瓶");
    }

    private void queryStorage() {
        products = LitePal.findAll(Product.class);
        if (stockAdapter == null) {
            rv.setLayoutManager(new LinearLayoutManager(mActivity));
            stockAdapter = new StockAdapter(mActivity);
            rv.setAdapter(stockAdapter);
        }
        stockAdapter.setDatas(products);
    }

    @OnClick({R.id.btn_out_storage, R.id.btn_in_storage})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_out_storage:
                intent = new Intent(mActivity, OutStorageActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_in_storage:

                intent = new Intent(mActivity, InStorageActivity.class);
                startActivity(intent);
                break;
        }


    }
}
