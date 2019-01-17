package com.example.myhelper.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myhelper.R;
import com.example.myhelper.entity.Customer;
import com.example.myhelper.utils.DateUtil;
import com.example.myhelper.utils.ToastUtil;

import org.litepal.LitePal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderFilterActivity extends BaseActivity {

    public static final int RESQUEST_CODE = 102;
    @Nullable
    @BindView(R.id.tv_type)
    TextView tvType;
    @Nullable
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @Nullable
    @BindView(R.id.tv_customer)
    TextView tvCustomer;
    @Nullable
    @BindView(R.id.ll_customer)
    LinearLayout llCustomer;
    @Nullable
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @Nullable
    @BindView(R.id.ll_start_date)
    LinearLayout llStartDate;
    @Nullable
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @Nullable
    @BindView(R.id.ll_end_state)
    LinearLayout llEndState;
    @Nullable
    @BindView(R.id.tv_order_state)
    TextView tvOrderState;
    @Nullable
    @BindView(R.id.ll_order_state)
    LinearLayout llOrderState;

    private String[] types= new String[]{"全部","出库","入库"};
    private String[] orderStates= new String[]{"全部","已完成","未支付","未全部发货"};
    private ArrayList<String> typeList = new ArrayList<>();
    private ArrayList<String> orderStateList = new ArrayList<>();
    private  List<String> customerList = new ArrayList<>();
    private Bundle bundle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_filter;
    }


    @Override
    protected void init() {
        super.init();
        setToolbar("订单筛选",true);
        Collections.addAll(typeList, types);
        Collections.addAll(orderStateList, orderStates);

        List<Customer> customers = LitePal.findAll(Customer.class);
        for (Customer customer : customers) {
                customerList.add(customer.getName());

        }

        bundle = new Bundle();
    }

    @OnClick({ R.id.ll_type, R.id.ll_customer, R.id.ll_start_date, R.id.ll_end_state, R.id.ll_order_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.ll_type://全部 出库  入库

                    OptionsPickerView pvOptionsType = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

                        @Override
                        public void onOptionsSelect(int options1, int option2, int op3, View v) {
                            tvType.setText(typeList.get(options1));
                            bundle.putInt("type",options1);
                        }
                    }).build();
                pvOptionsType.setPicker(typeList);
                pvOptionsType.show();

                break;
            case R.id.ll_customer:
                //选择客户

                    OptionsPickerView customerOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

                        @Override
                        public void onOptionsSelect(int options1, int option2, int op3, View v) {

                            tvCustomer.setText(customerList.get(options1));
                            bundle.putString("customer",customerList.get(options1));

                        }
                    }).build();
                    customerOptions.setPicker(customerList);
                    customerOptions.show();

                break;
            case R.id.ll_start_date:
                //时间选择器

                    TimePickerView pvTimeStart = new TimePickerBuilder(this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            tvStartDate.setText(DateUtil.timestamp2ymd(date.getTime()));
                            bundle.putLong("startDate",date.getTime());

                        }
                    }).build();

                    //设置选中的日期
                    try {
                        Calendar ca = Calendar.getInstance();
                        Date date = DateUtil.parse2Date(tvStartDate.getText().toString());
                        ca.setTime(date);
                        pvTimeStart.setDate(ca);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                pvTimeStart.show();

                break;
            case R.id.ll_end_state:
                //时间选择器

                    TimePickerView pvTimeEnd = new TimePickerBuilder(this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            tvEndDate.setText(DateUtil.timestamp2ymd(date.getTime()));
                            bundle.putLong("endDate",date.getTime());
                        }
                    }).build();

                    //设置选中的日期
                    try {
                        Calendar ca = Calendar.getInstance();
                        Date date = DateUtil.parse2Date(tvEndDate.getText().toString());
                        ca.setTime(date);
                        pvTimeEnd.setDate(ca);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                pvTimeEnd.show();
                break;
            case R.id.ll_order_state:
                    OptionsPickerView pvOptionsOrderState = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

                        @Override
                        public void onOptionsSelect(int options1, int option2, int op3, View v) {
                            tvType.setText(orderStateList.get(options1));
                            bundle.putInt("orderState",options1);
                        }
                    }).build();
                pvOptionsOrderState.setPicker(orderStateList);
                pvOptionsOrderState.show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_category_menu,menu);
        MenuItem item = menu.findItem(R.id.save);
        item.setTitle("确定");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.save){
            Intent intent = new Intent(this,RecordActivity.class);
            intent.putExtras(bundle);
            setResult(RESQUEST_CODE,intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
