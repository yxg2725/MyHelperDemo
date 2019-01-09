package com.example.myhelper.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.adapter.OrderDetailAdapter;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;
import com.example.myhelper.event.MessageEvent;
import com.example.myhelper.utils.GsonUtil;
import com.example.myhelper.widget.SawtoothBlackView;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends BaseActivity {


    @BindView(R.id.tv_order_title)
    TextView tvOrderTitle;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_customer)
    TextView tvCustomer;
    @BindView(R.id.tv_order_total_price)
    TextView tvOrderTotalPrice;
    @BindView(R.id.tv_send_total_price)
    TextView tvSendTotalPrice;
    @BindView(R.id.tv_order_state)
    TextView tvOrderState;
    @BindView(R.id.rv_order_detail)
    RecyclerView rvOrderDetail;
    @BindView(R.id.ll_customer)
    LinearLayout llCustomer;
    @BindView(R.id.ll_send_price)
    LinearLayout llSendPrice;
    private MyOrder mOrder;
    private String from;
    private OrderDetailAdapter orderDetailAdapter;
    private List<Product> products;
    private int payState;//已支付  未支付

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void init() {
        super.init();
        setToolbar("订单详情", true);


        getIntentDatas();
        setdatas();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        if ("RecordActivity".equals(from)){
            menu.clear();
        }else{
            getMenuInflater().inflate(R.menu.order_detail_menu,menu);
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.commit){

            //保存到db
            if ("OutStorageActivity".equals(from)){
                saveToOderTable(false);
            }else if("InStorageActivity".equals(from)){
                saveToOderTable(true);
            }

            EventBus.getDefault().post(new MessageEvent("finish"));


        }
        return super.onOptionsItemSelected(item);

    }

    private void saveToOderTable(boolean isAdd) {


        SQLiteDatabase database = LitePal.getDatabase();
        database.beginTransaction();

        //存入product表 更新库存数量
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Product pro = LitePal.where("name=?", product.getName()).findFirst(Product.class);
            if (isAdd){
                pro.setCount(pro.getCount() + product.getCount());
            }else{
                pro.setCount(pro.getCount() - product.getCount());
            }

            pro.saveOrUpdate("name=?", product.getName());
        }
        mOrder.save();

        database.setTransactionSuccessful();
        database.endTransaction();


        finish();
    }


    private void setdatas() {
        if (mOrder != null){

            if ("OutStorageActivity".equals(from)){

                tvOrderTitle.setText("出库订单核对");
                setOutData();

            }else if("InStorageActivity".equals(from)){
                tvOrderTitle.setText("入库订单核对");
                setInData();
            }else if("RecordActivity".equals(from)){
                if (mOrder.getState() == 0) {//出库
                    tvOrderTitle.setText("出库订单详情");
                    setOutData();
                }else if (mOrder.getState() == 1){//入库
                    tvOrderTitle.setText("入库订单详情");
                    setInData();
                }
            }

            String productDetail = mOrder.getProductDetail();
            products = (List<Product>) GsonUtil.parseJsonToList(productDetail, new TypeToken<List<Product>>() {
            }.getType());

            initOrderDetail();



        }
    }

    private void setOutData(){
        tvOrderDate.setText(mOrder.getTime());
        tvCustomer.setText(mOrder.getCustomerName());
        tvOrderTotalPrice.setText(mOrder.getActualPayment()+"");
        tvSendTotalPrice.setText(mOrder.getTotalPrice()+"");

        if (payState == 0){
            if (mOrder.getTotalPrice()>=mOrder.getActualPayment()){
                tvOrderState.setText("已完成");
                mOrder.setOrderState(0);
            }else{
                tvOrderState.setText("未全部发货");
                mOrder.setOrderState(2);
            }
        }else{
            tvOrderState.setText("未支付");
            mOrder.setOrderState(1);
        }

    }


    private void setInData(){
        tvOrderDate.setText(mOrder.getTime());
        tvOrderTotalPrice.setText(mOrder.getTotalCost()+"");//订单总金额
        llCustomer.setVisibility(View.GONE);
        llSendPrice.setVisibility(View.GONE);
        tvOrderState.setText("已完成");
        mOrder.setOrderState(0);

    }

    private void initOrderDetail() {
        rvOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        orderDetailAdapter = new OrderDetailAdapter(this);
        rvOrderDetail.setAdapter(orderDetailAdapter);
        orderDetailAdapter.setDatas(products,mOrder.getState());
    }


    public void getIntentDatas() {

        Intent intent = getIntent();
//        from = intent.getStringExtra("from");
//        payState = intent.getIntExtra("payState",0);
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mOrder = (MyOrder) bundle.get("order");
            from = bundle.getString("from");
            payState = bundle.getInt("payState");
        }


    }
}
