package com.example.myhelper.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myhelper.R;
import com.example.myhelper.adapter.OrderAdapter;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InStorageActivity extends BaseActivity {


    @BindView(R.id.tv_in_time)
    TextView tvInTime;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_category_count)
    TextView tvCategoryCount;
    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;
    @BindView(R.id.btn_in_storage)
    Button btnInStorage;
    @BindView(R.id.tv_total_cost)
    TextView tvTotalCost;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private OrderAdapter orderAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_in_storage;
    }

    private List<String> list1 = new ArrayList<>();
    private List<List<Integer>> list2 = new ArrayList<>();
    private List<MyOrder> mList = new ArrayList<>();

    @Override
    protected void init() {
        super.init();
        setToolbar("入库", true);

        setDatas();
    }

    private void setDatas() {
        Date date = new Date();
        String today = formatter.format(date);
        tvInTime.setText(today);

        prepareOptionsPickerDatas();


    }

    private void prepareOptionsPickerDatas() {
        List<Product> products = LitePal.findAll(Product.class);
        for (Product product : products) {
            list1.add(product.getName());
        }
        for (int i = 0; i < products.size(); i++) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int j = 1; j < 100; j++) {
                list.add(j);
            }

            list2.add(list);
        }
    }


    @OnClick({R.id.tv_in_time, R.id.tv_category, R.id.btn_in_storage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_in_time:
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(InStorageActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String dateStr = formatter.format(date);
                        tvInTime.setText(dateStr);
                    }
                }).build();

                //设置选中的日期
                try {
                    Calendar ca = Calendar.getInstance();
                    Date date = formatter.parse(tvInTime.getText().toString());
                    ca.setTime(date);
                    pvTime.setDate(ca);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                pvTime.show();
                break;
            case R.id.tv_category:
                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(InStorageActivity.this, new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int option2, int op3, View v) {

                        String productName = list1.get(options1);
                        Integer count = list2.get(options1).get(option2);
                        tvCategory.setText(productName);

                        updateRv(productName, count);
                        updateBottomView();
                    }
                }).build();
                pvOptions.setPicker(list1, list2);
                pvOptions.show();
                break;
            case R.id.btn_in_storage:
                saveToOderTable();
                break;
        }
    }



    private void updateRv(String name, int count) {
        Product product = LitePal.where("name=?", name).findFirst(Product.class);
        MyOrder myOrder = new MyOrder();
        myOrder.setProductName(name);
        myOrder.setNumber(count);
        myOrder.setState(1);//入库
        myOrder.setTime(tvInTime.getText().toString());
        myOrder.setTotalCost(count * product.getCostPrice());
        mList.add(myOrder);


        if (orderAdapter == null) {
            rv.setLayoutManager(new LinearLayoutManager(this));
            orderAdapter = new OrderAdapter(this);
            rv.setAdapter(orderAdapter);
        }
        orderAdapter.setData(mList);

    }

    private void updateBottomView() {
        int typeCout = 0;
        int number = 0;
        double cost = 0;
        HashSet<String> set = new HashSet<>();

        for (int i = 0; i < mList.size(); i++) {
            MyOrder myOrder = mList.get(i);
            set.add(myOrder.getProductName());
            number = number + myOrder.getNumber();
            cost = cost + myOrder.getTotalCost();
        }
        typeCout = set.size();
        tvCategoryCount.setText(typeCout + "");
        tvTotalCount.setText(number + "");
        tvTotalCost.setText(cost+"");
    }

    private void saveToOderTable() {
        //存入order 表
        LitePal.saveAll(mList);

        //存入product表 更新库存数量
        for (int i = 0; i < mList.size(); i++) {
            MyOrder myOrder = mList.get(i);
            Product pro = LitePal.where("name=?", myOrder.getProductName()).findFirst(Product.class);
            pro.setCount(pro.getCount()+myOrder.getNumber());
            pro.saveOrUpdate("name=?",myOrder.getProductName());
        }

        finish();
    }

}
