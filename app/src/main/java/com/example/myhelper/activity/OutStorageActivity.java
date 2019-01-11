package com.example.myhelper.activity;

import android.content.DialogInterface;
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
import com.example.myhelper.entity.Customer;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;
import com.example.myhelper.event.MessageEvent;
import com.example.myhelper.utils.DateUtil;
import com.example.myhelper.utils.DialogUtil;
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
    EditText tvTotalPrice;
    @BindView(R.id.btn_out)
    Button btnOut;
    @BindView(R.id.tv_number)
    EditText tvNumber;
    @BindView(R.id.tv_unit_price)
    EditText tvUnitPrice;
//    @BindView(R.id.tv_unit_total_price)
//    EditText tvUnitTotalPrice;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.btn_reset)
    Button btnReset;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    private List<String> categorylist = new ArrayList<>();
    private List<String> customerlist = new ArrayList<>();
    private ArrayList<Product> mList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private String productName;
    private double totalPrice;
    private int number;

    @Override
    public int getLayoutId() {
        return R.layout.activity_out_storage;
    }

    @Override
    protected void init() {
        super.init();
        EventBus.getDefault().register(this);

        setToolbar("出库", true);
        setDatas();



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

        if (categorylist.size() > 0) {
            tvCategoryName.setText(categorylist.get(0));
        }

    }


    @OnClick({R.id.tv_out_time, R.id.tv_category_name, R.id.tv_customer, R.id.btn_out,R.id.btn_sure,R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_out_time:
                //时间选择器
                InputMethodUtils.hintKeyBoard(this,getCurrentFocus());

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
                //选择产品
                InputMethodUtils.hintKeyBoard(this,getCurrentFocus());

                if(!checkHasProduct())return;

                OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int option2, int op3, View v) {

                        String productName = categorylist.get(options1);
                        tvCategoryName.setText(productName);
                        //更新单价
                        updateUnitPrice(tvCustomer.getText().toString(),productName);

                    }
                }).build();
                pvOptions.setPicker(categorylist);
                pvOptions.show();
                break;
            case R.id.tv_customer:
                //选择客户
                InputMethodUtils.hintKeyBoard(this,getCurrentFocus());

                //查询是否存在客户
                if(!checkhasCustomer())return;


                OptionsPickerView customerOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int option2, int op3, View v) {

                        String customerName = customerlist.get(options1);
                        tvCustomer.setText(customerName);
                        //更新单价
                        updateUnitPrice(customerName,tvCategoryName.getText().toString());

                    }
                }).build();
                customerOptions.setPicker(customerlist);
                customerOptions.show();

                break;
            case R.id.btn_sure:

                if(!checkoutSelectedCustomer())return;

                //检查库存量够不够
                productName = tvCategoryName.getText().toString().trim();
                String numStr = tvNumber.getText().toString().trim();
                Integer count = Integer.valueOf(numStr);

                if (!checkStorage(productName,count)) {
                    DialogUtil.showConfirmDialog(this,"confirm","提示","库存不足，请先入库！",new DialogConfirmClickListener());
                    return;
                }

                updateRv(productName, count);
                updateBottomView();
                break;
            case R.id.btn_reset://重置
                mList.clear();
                notifyData();
                updateBottomView();
                break;
            case R.id.btn_out:
                if(!checkCanOut())return;

                String[] items = {"已付款","未付款"};
                DialogUtil.showSingleChoiceDialog(this, "single", "是否付款", items, 0, new DialogUtil.OnSingleConfirmListener() {
                    @Override
                    public void onSingleDialogConfirm(int which) {
                        final MyOrder order = createOrder(which);
                        toOrderDetailActivity(order,which);
                    }
                });
                break;
        }
    }

    private void updateUnitPrice(String customerName,String productName) {
        Customer customer = LitePal.where("name=?", customerName).findFirst(Customer.class);
        Product product = LitePal.where("name=?", productName).findFirst(Product.class);

        if (customer == null || product == null)return;

        double price = 0;
        switch (customer.getLevel()){
            case 0:
                price = product.getRetailPrice();
                break;
            case 1:
                price = product.getMemberPrice();
                break;
            case 2:
                price = product.getVipPrice();
                break;
            case 3:
                price = product.getSilverPrice();
                break;
            case 4:
                price = product.getGoldPrice();
                break;
        }
        tvUnitPrice.setText(price+"");
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

    private boolean checkhasCustomer() {
        List<Customer> customers = LitePal.findAll(Customer.class);
        if (customers != null && !customers.isEmpty()){
            return true;
        }

        ToastUtil.showToast("请先添加客户");
        //跳转到添加客户界面
        Intent intent = new Intent(this, AddCustomerActivity.class);
        startActivity(intent);
        return false;
    }

    private void toOrderDetailActivity(MyOrder order,int which){
        //跳转到账单详情界面
        Intent intent = new Intent(OutStorageActivity.this,OrderDetailActivity.class);
//                        intent.putExtra("payState",which);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order",order);
//                        bundle.putSerializable("product",mList);
        bundle.putString("from","OutStorageActivity");
        bundle.putInt("payState",which);
        intent.putExtras(bundle);
//                        intent.putExtra("from","OutStorageActivity");
        startActivity(intent);


    }

    private MyOrder createOrder(int which) {
        String productJson = GsonUtil.toJson(mList);
        MyOrder myOrder = new MyOrder();
        myOrder.setProductDetail(productJson);
        myOrder.setNumber(number);
        myOrder.setState(0);//出库
        try {
            Date date = DateUtil.parse2Date(tvOutTime.getText().toString());
            myOrder.setTime(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myOrder.setCustomerName(tvCustomer.getText().toString());//客户
//        myOrder.setTotalCost(count * product.getCostPrice());//总成本
        myOrder.setTotalPrice(totalPrice);//总收入
        Double actualPrice = Double.valueOf(tvTotalPrice.getText().toString());
        myOrder.setActualPayment(actualPrice);//实际支付的金额
        myOrder.setOrderNo(OrderNoCreateFactory.getOrderIdByTime());
        return myOrder;
    }

    /**
     * 检查库存是否足够
     * @param productName
     * @param count
     * @return
     */
    private boolean checkStorage(String productName,int count){
        Product product = LitePal.where("name=?", productName).findFirst(Product.class);
        if(product.getCount()< count){
            return false;
        }
        return true;
    }

    private boolean checkCanOut() {
        //是否选中了客户
       if (!checkoutSelectedCustomer())return false;

        //是否选中了产品
        if (mList.isEmpty()){
            ToastUtil.showToast("请添加产品！");
            return false;
        }
        return true;
    }

    private boolean checkoutSelectedCustomer(){
        if (TextUtils.isEmpty(tvCustomer.getText().toString())){
            ToastUtil.showToast("请选择客户！");
            return false;
        }
        return true;
    }

    private void updateRv(String name, Integer count) {
        Product product = LitePal.where("name=?", name).findFirst(Product.class);
        product.setCount(count);
        product.setDate(tvOutTime.getText().toString());
        String retailPriceStr = tvUnitPrice.getText().toString().trim();

        product.setRetailPrice(Double.valueOf(retailPriceStr));
        product.setCostPrice(Double.valueOf(retailPriceStr));
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
        double cost = 0;
        totalPrice = 0;
        HashSet<String> set = new HashSet<>();

        for (int i = 0; i < mList.size(); i++) {
            Product product = mList.get(i);
            set.add(product.getName());
            number = number + product.getCount();
//            cost = cost + product.getTotalCost();
            totalPrice = totalPrice + product.getCount()*product.getRetailPrice();
        }
        typeCout = set.size();
        tvCategoryCount.setText(typeCout + "");
        tvTotalCount.setText(number + "");
        tvTotalPrice.setText(totalPrice +"");
    }

    /*private void saveToOderTable() {





        SQLiteDatabase database = LitePal.getDatabase();
        database.beginTransaction();

        //存入product表 更新库存数量
        for (int i = 0; i < mList.size(); i++) {
            Product product = mList.get(i);
            Product pro = LitePal.where("name=?", product.getName()).findFirst(Product.class);
            pro.setCount(pro.getCount() - product.getCount());
            pro.saveOrUpdate("name=?", product.getName());
        }
//        myOrder.save();

        database.setTransactionSuccessful();
        database.endTransaction();


        finish();
    }*/


    public class DialogConfirmClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            //跳转到入库界面
            Intent intent = new Intent(OutStorageActivity.this, InStorageActivity.class);
            intent.putExtra("categoryName",productName);
            startActivity(intent);
        }
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
