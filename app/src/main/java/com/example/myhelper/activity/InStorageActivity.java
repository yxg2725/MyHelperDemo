package com.example.myhelper.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myhelper.R;
import com.example.myhelper.adapter.ProductAdapter;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;
import com.example.myhelper.event.MessageEvent;
import com.example.myhelper.utils.DateUtil;
import com.example.myhelper.utils.GsonUtil;
import com.example.myhelper.utils.InputMethodUtils;
import com.example.myhelper.utils.OrderNoCreateFactory;
import com.example.myhelper.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
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
    @BindView(R.id.tv_number)
    EditText tvNumber;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.btn_reset)
    Button btnReset;
    @BindView(R.id.tv_unit_price)
    EditText tvUnitPrice;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private int number;//订单包含多少个产品
    private double totalCost;
    private ProductAdapter productAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_in_storage;
    }

    private List<String> categoryList = new ArrayList<>();
    private List<Product> mList = new ArrayList<>();

    @Override
    protected void init() {
        super.init();
        EventBus.getDefault().register(this);


        setToolbar("入库", true);

        setDatas();

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");
        if (!TextUtils.isEmpty(categoryName)) {
            //设置 分类 默认选中
            tvCategory.setText(categoryName);
            setTvUnitPrice(categoryName);
        }
    }

    private void setTvUnitPrice(String categoryName){
        //设置单价
        Product pro = LitePal.where("name=?", categoryName).findFirst(Product.class);
        tvUnitPrice.setText(pro.getCostPrice()+"");
    }

    private void setDatas() {
        Date date = new Date();
        String today = formatter.format(date);
        tvInTime.setText(today);

        prepareOptionsPickerDatas();
        if (categoryList.size() > 0) {
            tvCategory.setText(categoryList.get(0));
            setTvUnitPrice(categoryList.get(0));
        }


    }

    private void prepareOptionsPickerDatas() {
        List<Product> products = LitePal.findAll(Product.class);
        for (Product product : products) {
            categoryList.add(product.getName());
        }

    }


    @OnClick({R.id.tv_in_time, R.id.tv_category, R.id.btn_in_storage, R.id.btn_sure,R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_in_time:
                InputMethodUtils.hintKeyBoard(this,getCurrentFocus());
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
                InputMethodUtils.hintKeyBoard(this,getCurrentFocus());

                //检查是否添加产品
                if(!checkHasProduct())return;
                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(InStorageActivity.this, new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int option2, int op3, View v) {

                        String productName = categoryList.get(options1);
                        tvCategory.setText(productName);
                        setTvUnitPrice(productName);
                    }
                }).build();
                pvOptions.setPicker(categoryList);
                pvOptions.show();
                break;
            case R.id.btn_sure:
                String productName = tvCategory.getText().toString().trim();
                String numStr = tvNumber.getText().toString().trim();
                int count = Integer.valueOf(numStr);

                if (TextUtils.isEmpty(productName)) {
                    ToastUtil.showToast("请选选择入库类型");
                    return;
                }
                updateRv(productName, count);
                updateBottomView();
                break;
            case R.id.btn_reset:
                mList.clear();
                notifyData();
                updateBottomView();
                break;
            case R.id.btn_in_storage:
                if(!checkCanOut())return;
                //生成order数据
                MyOrder order = createOrder();
                //跳转到核对订单界面
                //跳转到账单详情界面
                Intent intent = new Intent(InStorageActivity.this,OrderDetailActivity.class);
//                intent.putExtra("payState",which);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order",order);
//                        bundle.putSerializable("product",mList);
                bundle.putString("from","InStorageActivity");
                intent.putExtras(bundle);
                startActivity(intent);

//                saveToOderTable();
                break;
        }
    }

    private boolean checkHasProduct() {
        List<Product> products = LitePal.findAll(Product.class);
        if (products != null && !products.isEmpty()){
            return true;
        }

        ToastUtil.showToast("请先添加产品");
        Intent intent = new Intent(this, AddCategoryActivity.class);
        startActivity(intent);

        return false;
    }

    private boolean checkCanOut() {
        //是否选中了产品
        if (mList.isEmpty()){
            ToastUtil.showToast("请添加产品！");
            return false;
        }
        return true;
    }


    private MyOrder createOrder() {
        String productJson = GsonUtil.toJson(mList);
        MyOrder myOrder = new MyOrder();
        myOrder.setProductDetail(productJson);
        myOrder.setNumber(number);//产品个数
        myOrder.setState(1);//入库
        try {
            Date date = DateUtil.parse2Date(tvInTime.getText().toString());
            myOrder.setTime(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myOrder.setTotalCost(totalCost);
        myOrder.setOrderNo(OrderNoCreateFactory.getOrderIdByTime());
        return myOrder;
    }


    private void updateRv(String name, int count) {
        Product product = LitePal.where("name=?", name).findFirst(Product.class);
        product.setCount(count);
        String unitPriceStr = tvUnitPrice.getText().toString().trim();
        product.setCostPrice(Double.valueOf(unitPriceStr));
        product.setDate(tvInTime.getText().toString());
        mList.add(product);
        notifyData();

    }


    private void notifyData(){

        if (productAdapter == null) {
            rv.setLayoutManager(new LinearLayoutManager(this));
            productAdapter = new ProductAdapter(this);
            rv.setAdapter(productAdapter);
        }
        productAdapter.setDatas(mList);
    }

    private void updateBottomView() {
        int typeCout = 0;
        number = 0;
        totalCost = 0;
        HashSet<String> set = new HashSet<>();

        for (int i = 0; i < mList.size(); i++) {
            Product product = mList.get(i);
            set.add(product.getName());
            number = number + product.getCount();
            totalCost = totalCost + product.getCostPrice()*product.getCount();
        }
        typeCout = set.size();
        tvCategoryCount.setText(typeCout + "");
        tvTotalCount.setText(number + "");
        tvTotalCost.setText(totalCost + "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("finish")){
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
