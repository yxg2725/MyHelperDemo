package com.example.myhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.activity.CategoryActivity;
import com.example.myhelper.activity.InStorageActivity;
import com.example.myhelper.activity.OutStorageActivity;
import com.example.myhelper.activity.StockActivity;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2019/1/2.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.tv_category_count)
    TextView tvCategoryCount;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.tv_stock_count)
    TextView tvStockCount;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.ll_count)
    LinearLayout llCount;
    @BindView(R.id.tv_today_in_count)
    TextView tvTodayInCount;
    @BindView(R.id.tv_today_in)
    TextView tvTodayIn;
    @BindView(R.id.ll_today_in)
    LinearLayout llTodayIn;
    @BindView(R.id.tv_today_out_count)
    TextView tv_TodayOutCount;
    @BindView(R.id.tv_today_out)
    TextView tvTodayOut;
    @BindView(R.id.ll_today_out)
    LinearLayout llTodayOut;
    @BindView(R.id.ll_in_storage)
    LinearLayout llInStorage;
    @BindView(R.id.ll_out_storage)
    LinearLayout llOutStorage;
    @BindView(R.id.ll_stock)
    LinearLayout llStock;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    Unbinder unbinder;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;

    }




    @Override
    public void onResume() {
        super.onResume();
        //查找酵素种类
        List<Product> products = LitePal.findAll(Product.class);
        tvCategoryCount.setText(products.size()+"");

        //库存数量
        int count = 0;
        for (Product product : products) {
            count = count + product.getCount();
        }
        tvStockCount.setText(count+"");

        //今日入库
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date);
        List<MyOrder> inOrders = LitePal.where("time=? and state=?", dateStr, "1").find(MyOrder.class);
        int inCount = 0;
        for (MyOrder order : inOrders) {
            inCount = inCount + order.getNumber();
        }

        tvTodayInCount.setText(inCount+"");

        //今日出库


        List<MyOrder> outOrders = LitePal.where("time=? and state=?", dateStr, "0").find(MyOrder.class);
        int outCount = 0;
        for (MyOrder order : outOrders) {
            outCount = outCount + order.getNumber();
        }

        tv_TodayOutCount.setText(outCount+"");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_category, R.id.ll_count, R.id.ll_today_in, R.id.ll_today_out, R.id.ll_in_storage, R.id.ll_out_storage, R.id.ll_stock, R.id.ll_record})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.ll_category:
                intent = new Intent(mActivity, CategoryActivity.class);
                startActivity(intent);

                break;
            case R.id.ll_count:
                intent = new Intent(mActivity, StockActivity.class);
                startActivity(intent);

                break;
            case R.id.ll_today_in:
                break;
            case R.id.ll_today_out:
                break;
            case R.id.ll_in_storage:
                intent = new Intent(mActivity, InStorageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_out_storage:
                intent = new Intent(mActivity, OutStorageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_stock:
                break;
            case R.id.ll_record:
                break;
        }
    }
}
