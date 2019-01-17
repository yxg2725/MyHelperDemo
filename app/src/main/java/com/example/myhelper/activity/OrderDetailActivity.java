package com.example.myhelper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.adapter.OrderDetailAdapter;
import com.example.myhelper.entity.Customer;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;
import com.example.myhelper.event.MessageEvent;
import com.example.myhelper.utils.DateUtil;
import com.example.myhelper.utils.DialogUtil;
import com.example.myhelper.utils.GsonUtil;
import com.example.myhelper.widget.SawtoothBlackView;
import com.google.gson.Gson;
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
    @BindView(R.id.src_order_state)
    ImageView iconOrderState;
    private MyOrder mOrder;
    private String from;
    private OrderDetailAdapter orderDetailAdapter;
    private List<Product> products;
    private int payState;//已支付  未支付
    private List<Product> mProductList = new ArrayList<>();
    private int newOrderFlag;//0 表示新点单 1表示合并原来订单
    private MyOrder oldOrder;

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
        for (int i = 0; i < mProductList.size(); i++) {
            Product product = mProductList.get(i);
            Product pro = LitePal.where("name=?", product.getName()).findFirst(Product.class);
            if (isAdd){
                pro.setCount(pro.getCount() + product.getCount());
            }else{
                pro.setCount(pro.getCount() - product.getCount());
            }

            pro.saveOrUpdate("name=?", product.getName());
        }
        if (newOrderFlag == 0){//新的单独订单
            mOrder.save();
        }else{//合并订单
            oldOrder.delete();
            mOrder.save();
        }


        database.setTransactionSuccessful();
        database.endTransaction();


        finish();
    }


    private void setdatas() {
        mProductList.clear();
        String productDetail = mOrder.getProductDetail();
        products = (List<Product>) GsonUtil.parseJsonToList(productDetail, new TypeToken<List<Product>>() {
        }.getType());

        mProductList.addAll(products);


        if (mOrder != null){

            if ("OutStorageActivity".equals(from)){

                tvOrderTitle.setText("出库订单核对");

                //查询该客户是否存在未发货完成的订单
                oldOrder = LitePal.where("customername=? and orderstate=?", mOrder.getCustomerName(),String.valueOf(2)).findFirst(MyOrder.class);
                if (oldOrder != null){
                    DialogUtil.showSelectedConfirmDialog(this, "selectedConfirm",
                            "提示", "该客户存在未全部发货完成的订单，是否将该次记录合并到上次订单","合并上次订单","新生成订单"
                            , new DialogUtil.OnConfirmListener() {
                                @Override
                                public void onConfirmListener() {
                                    //合并order
                                    String oldJson = oldOrder.getProductDetail();
                                    List<Product> oldProducts = (List<Product>) GsonUtil.parseJsonToList(oldJson, new TypeToken<List<Product>>() {
                                    }.getType());

                                    //更新订单总金额
                                    mOrder.setActualPayment(oldOrder.getActualPayment());
                                    //更新发货总金额
                                    mOrder.setTotalPrice(oldOrder.getTotalPrice()+mOrder.getTotalPrice());

                                    setOutData(false);
                                    mProductList.clear();
                                    mProductList.addAll(oldProducts);
                                    mProductList.addAll(products);

                                    //更新所有产品
                                    mOrder.setProductDetail(GsonUtil.toJson(mProductList));

                                    //更新订单状态
                                    if (mOrder.getOrderState() != 1){
                                        if (mOrder.getTotalPrice() >= mOrder.getActualPayment()){
                                            mOrder.setOrderState(0);
                                        }else{
                                            mOrder.setOrderState(2);
                                        }
                                    }



                                    initOrderDetail();
                                    newOrderFlag = 1;
                                }

                                @Override
                                public void onNegativeconfirmListener() {
                                    //不处理
                                    setOutData(false);
                                    initOrderDetail();
                                    newOrderFlag = 0;
                                }

                            });
                }else{


                    //设置订单状态
                    if (payState == 1 ){//未付款
                        mOrder.setOrderState(1);
                    }else{//已付款
                        if (mOrder.getTotalPrice() >= mOrder.getActualPayment()){
                            mOrder.setOrderState(0);
                        }else{
                            mOrder.setOrderState(2);
                        }
                    }


                    setOutData(false);
                    initOrderDetail();
                    newOrderFlag = 0;
                }



            }else if("InStorageActivity".equals(from)){
                tvOrderTitle.setText("入库订单核对");
                setInData(false);
                mOrder.setOrderState(0);
                initOrderDetail();
            }else if("RecordActivity".equals(from)){
                if (mOrder.getState() == 0) {//出库
                    tvOrderTitle.setText("出库订单详情");

                    setOutData(true);
                }else if (mOrder.getState() == 1){//入库
                    tvOrderTitle.setText("入库订单详情");
                    iconOrderState.setVisibility(View.VISIBLE);
                    setInData(true);
                }

                initOrderDetail();
            }





        }
    }

    private void setOutData(boolean isRecord){

        tvOrderDate.setText(DateUtil.timestamp2ymd(mOrder.getTime()));
        tvCustomer.setText(mOrder.getCustomerName());
        tvOrderTotalPrice.setText(mOrder.getActualPayment()+"");
        tvSendTotalPrice.setText(mOrder.getTotalPrice()+"");

        if(isRecord){
            if (mOrder.getOrderState() == 0){
                tvOrderState.setText("已完成");
                iconOrderState.setVisibility(View.VISIBLE);
            }else if(mOrder.getOrderState() == 1){
                tvOrderState.setText("未支付");
            }else{
                tvOrderState.setText("未全部发货");
            }
        }


    }


    private void setInData(boolean showState){
        tvOrderDate.setText(DateUtil.timestamp2ymd(mOrder.getTime()));
        tvOrderTotalPrice.setText(mOrder.getTotalCost()+"");//订单总金额
        llCustomer.setVisibility(View.GONE);
        llSendPrice.setVisibility(View.GONE);

        if (showState){
            tvOrderState.setText("已完成");
        }

//        mOrder.setOrderState(0);

    }

    private void initOrderDetail() {
        rvOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        orderDetailAdapter = new OrderDetailAdapter(this);
        rvOrderDetail.setAdapter(orderDetailAdapter);
        orderDetailAdapter.setDatas(mProductList,mOrder.getState());
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
