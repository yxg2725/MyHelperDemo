package com.example.myhelper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myhelper.R;
import com.example.myhelper.adapter.OrderAdapter;
import com.example.myhelper.entity.Customer;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;
import com.example.myhelper.utils.DialogUtil;
import com.example.myhelper.utils.ToastUtil;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OutStorageActivity extends BaseActivity{


    private static final int REQUESTCODE_OUTSTORAGE = 11;
    @BindView(R.id.tv_out_time)
    TextView tvOutTime;
    @BindView(R.id.tv_category_name)
    TextView tvCategoryName;
    @BindView(R.id.tv_customer)
    TextView tvCustomer;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_category_count)
    TextView tvCategoryCount;
    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.btn_out)
    Button btnOut;
    @BindView(R.id.tv_number)
    EditText tvNumber;
    @BindView(R.id.tv_unit_price)
    EditText tvUnitPrice;
    @BindView(R.id.tv_unit_total_price)
    EditText tvUnitTotalPrice;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    private List<String> categorylist = new ArrayList<>();
    private List<String> customerlist = new ArrayList<>();
    private List<MyOrder> mList = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private String productName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_out_storage;
    }

    @Override
    protected void init() {
        super.init();
        setToolbar("出库", true);
        setDatas();

        tvNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    Integer number = Integer.valueOf(s.toString());
                    String unitPriceStr = tvUnitPrice.getText().toString().trim();
                    Integer unitPrice = Integer.valueOf(unitPriceStr);
                    tvUnitTotalPrice.setText(number*unitPrice+"");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvUnitPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(s)) {
                    Integer unitPriceStr = Integer.valueOf(s.toString());
                    Integer unitPrice = Integer.valueOf(unitPriceStr);
                    String num = tvNumber.getText().toString().trim();
                    Integer number = Integer.valueOf(num);

                    tvUnitTotalPrice.setText(number*unitPrice+"");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void setDatas() {
        Date date = new Date();
        String today = formatter.format(date);
        tvOutTime.setText(today);

        prepareOptionsPickerDatas();


    }

    private void prepareOptionsPickerDatas() {
        List<Product> products = LitePal.findAll(Product.class);
        List<Customer> customers = LitePal.findAll(Customer.class);
        for (Product product : products) {
            categorylist.add(product.getName());
        }
        for (Customer customer:customers){
            customerlist.add(customer.getName());
        }

    }


    @OnClick({R.id.tv_out_time, R.id.tv_category_name, R.id.tv_customer, R.id.btn_out,R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_out_time:
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String dateStr = formatter.format(date);
                        tvOutTime.setText(dateStr);
                    }
                }).build();

                //设置选中的日期
                try {
                    Calendar ca = Calendar.getInstance();
                    Date date = formatter.parse(tvOutTime.getText().toString());
                    ca.setTime(date);
                    pvTime.setDate(ca);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                pvTime.show();
                break;
            case R.id.tv_category_name:
                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int option2, int op3, View v) {

                        String productName = categorylist.get(options1);
                        tvCategoryName.setText(productName);


                    }
                }).build();
                pvOptions.setPicker(categorylist);
                pvOptions.show();
                break;
            case R.id.tv_customer:
                //条件选择器
                OptionsPickerView customerOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int option2, int op3, View v) {

                        String customerName = customerlist.get(options1);
                        tvCustomer.setText(customerName);


                    }
                }).build();
                customerOptions.setPicker(customerlist);
                customerOptions.show();
                break;
            case R.id.btn_sure:

                //检查库存量够不够
                productName = tvCategoryName.getText().toString().trim();
                String numStr = tvNumber.getText().toString().trim();
                Integer count = Integer.valueOf(numStr);
                String unitTotalPriceStr = tvUnitTotalPrice.getText().toString().trim();
                Integer unitotalPrice = Integer.valueOf(unitTotalPriceStr);
                String customer = tvCustomer.getText().toString();
                Product product = LitePal.where("name=?", productName).findFirst(Product.class);
                if (TextUtils.isEmpty(customer)) {
                    ToastUtil.showToast("请选择客户");
                    return;
                }
                if (product.getCount()< count) {
                    DialogUtil.showConfirmDialog(this,"confirm","提示","库存不足，请先入库！",new DialogConfirmClickListener());
                    return;
                }

                updateRv(productName, count,unitotalPrice);
                updateBottomView();
                break;
            case R.id.btn_out:
                saveToOderTable();
                break;
        }
    }

    private void updateRv(String name, Integer count,Integer unitotalPrice) {
        Product product = LitePal.where("name=?", name).findFirst(Product.class);
        MyOrder myOrder = new MyOrder();
        myOrder.setProductName(name);
        myOrder.setNumber(count);
        myOrder.setState(0);//出库
        myOrder.setTime(tvOutTime.getText().toString());
        myOrder.setCustomerName(tvCustomer.getText().toString());//客户
        myOrder.setTotalCost(count * product.getCostPrice());//总成本
        myOrder.setTotalPrice(unitotalPrice);//总收入
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
        double totalPrice = 0;
        HashSet<String> set = new HashSet<>();

        for (int i = 0; i < mList.size(); i++) {
            MyOrder myOrder = mList.get(i);
            set.add(myOrder.getProductName());
            number = number + myOrder.getNumber();
            cost = cost + myOrder.getTotalCost();
            totalPrice = totalPrice+ myOrder.getTotalPrice();
        }
        typeCout = set.size();
        tvCategoryCount.setText(typeCout + "");
        tvTotalCount.setText(number + "");
        tvTotalPrice.setText(totalPrice+"");
    }

    private void saveToOderTable() {
        //存入order 表
        LitePal.saveAll(mList);

        //存入product表 更新库存数量
        for (int i = 0; i < mList.size(); i++) {
            MyOrder myOrder = mList.get(i);
            Product pro = LitePal.where("name=?", myOrder.getProductName()).findFirst(Product.class);
            pro.setCount(pro.getCount() - myOrder.getNumber());
            pro.saveOrUpdate("name=?", myOrder.getProductName());
        }

        finish();
    }


    public class DialogConfirmClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            //跳转到入库界面
            Intent intent = new Intent(OutStorageActivity.this, InStorageActivity.class);
            intent.putExtra("categoryName",productName);
            startActivity(intent);
        }
    }

}
