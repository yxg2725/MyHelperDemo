package com.example.myhelper.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.adapter.RecordAdapter;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;
import com.example.myhelper.utils.DensityUtils;
import com.example.myhelper.utils.DialogUtil;
import com.example.myhelper.utils.FilterWindow;
import com.example.myhelper.utils.GsonUtil;
import com.example.myhelper.utils.ToastUtil;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.myhelper.activity.OrderFilterActivity.RESQUEST_CODE;

public class RecordActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "RecordActivity";

    private static final int REQUEST_CODE = 101;
    @BindView(R.id.segmentTabLayout)
    SegmentTabLayout stl;
    @BindView(R.id.rv)
    SwipeMenuRecyclerView rv;
    @BindView(R.id.tv_total_out)
    TextView tvTotalOut;
    @BindView(R.id.tv_total_income)
    TextView tvTotaIncome;
    private SearchView mSearchView;
    private String[] mTitles = {"全部", "出库", "入库"};
    private List<MyOrder> orderList = new ArrayList<>();
    private RecordAdapter recordAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_record;
    }


    @Override
    protected void init() {
        super.init();
        setToolbar("订单记录", true);
        stl.setTabData(mTitles);

        addRvMenu();

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        stl.setCurrentTab(Integer.valueOf(position));


        int currentTab = stl.getCurrentTab();
        setRvData(currentTab);
        stl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                setRvData(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        initRv();
    }

    private void addRvMenu() {
        // 设置监听器。
        rv.setSwipeMenuCreator(mSwipeMenuCreator);

        // 菜单点击监听。
        rv.setSwipeMenuItemClickListener(mMenuItemClickListener);
    }

    // 创建菜单：
    SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
            int width = DensityUtils.dp2px(RecordActivity.this, 70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(RecordActivity.this).setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                rightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(RecordActivity.this).setBackground(R.drawable.selector_green)
                        .setText("修改\n订单\n状态")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                rightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };
    SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, final int position) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            switch (menuPosition) {
                case 0:

                    DialogUtil.showSelectedConfirmDialog(RecordActivity.this, "selectedConfirm", "删除"
                            , "删除记录并重置库存还是仅删除订单", "删除并重置库存", "仅删除订单",
                            new DialogUtil.OnConfirmListener() {
                                @Override
                                public void onConfirmListener() {
                                    deleteOrderItem(position, true);
                                }

                                @Override
                                public void onNegativeconfirmListener() {
                                    deleteOrderItem(position, false);
                                }
                            });
                    break;
                case 1://修改订单状态
                    String[] items = {"已完成", "未支付", "未全部发货"};
                    DialogUtil.showSingleChoiceDialog(RecordActivity.this, "single", "修改订单状态",
                            items, -1, new DialogUtil.OnSingleConfirmListener() {
                                @Override
                                public void onSingleDialogConfirm(int which) {
                                    //保存状态
                                    MyOrder myOrder = orderList.get(position);

                                    myOrder.setOrderState(which);
                                    myOrder.saveOrUpdate("orderno=?", myOrder.getOrderNo());

                                    //更新列表
                                    setRvData(stl.getCurrentTab());
                                }
                            });
                    break;
            }

        }
    };

    //删除订单
    private void deleteOrderItem(int position, boolean depthDelete) {
        MyOrder myOrder = orderList.get(position);

        MyOrder order = LitePal.where("orderno=?", myOrder.getOrderNo()).findFirst(MyOrder.class);
        if (order != null) {
            if (depthDelete) {

                depthDelete(order);

            } else {
                order.setShow(false);
                order.updateAll("orderno=?", myOrder.getOrderNo());
            }
            setRvData(stl.getCurrentTab());
        }

    }

    //删除订单并重置库存
    public void depthDelete(MyOrder order) {

        SQLiteDatabase db = LitePal.getDatabase();
        db.beginTransaction();

        order.delete();
        //重置库存
        String json = order.getProductDetail();
        List<Product> products = (List<Product>) GsonUtil.parseJsonToList(json, new TypeToken<List<Product>>() {
        }.getType());

        for (Product product : products) {
            Product p = LitePal.where("name=?", product.getName()).findFirst(Product.class);
            if (order.getState() == 0) {
                p.setCount(p.getCount() + product.getCount());
            } else {
                p.setCount(p.getCount() - product.getCount());
            }

            p.saveOrUpdate("name=?", product.getName());
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    private void setRvData(int position) {
        orderList.clear();
        switch (position) {
            case 0:
                List<MyOrder> list = LitePal.where("isshow=?", "1").find(MyOrder.class);
                orderList.addAll(list);
                break;
            case 1:
                List<MyOrder> list1 = LitePal.where("isshow=? and state=?", "1", "0").find(MyOrder.class);
                orderList.addAll(list1);
                break;
            case 2:
                List<MyOrder> list2 = LitePal.where("isshow=? and state=?", "1", "1").find(MyOrder.class);

                orderList.addAll(list2);
                break;

        }

        Collections.reverse(orderList);
        notifyData();

        //统计 总收入和支出
        statistics();
    }

    private void statistics(){
        double out = 0;
        double income = 0;
        for (MyOrder myOrder : orderList) {
            if (myOrder.getState() == 0){//出库
                income = income + myOrder.getTotalPrice();
            }else{
                out = out + myOrder.getTotalCost();
            }
        }

        if (out != 0){
            tvTotalOut.setVisibility(View.VISIBLE);
            tvTotalOut.setText("总支出：￥" + out);
        }else{
            tvTotalOut.setVisibility(View.GONE);
        }

        if (income != 0){
            tvTotaIncome.setVisibility(View.VISIBLE);
            tvTotaIncome.setText("总收入：￥"+ income);
        }else{
            tvTotaIncome.setVisibility(View.GONE);
        }
    }

    private void initRv() {
        notifyData();

    }

    private void notifyData() {
        if (recordAdapter == null) {
            rv.setLayoutManager(new LinearLayoutManager(this));
            recordAdapter = new RecordAdapter(this);
            rv.setAdapter(recordAdapter);
        }
        recordAdapter.setDatas(orderList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        initMenu();

        return super.onCreateOptionsMenu(menu);

    }


    private void initMenu() {
        mSearchView.setQueryHint("客户");
//        mSearchView.setMaxWidth(400);
        mSearchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            Intent intent = new Intent(this, OrderFilterActivity.class);
            startActivityForResult(intent,REQUEST_CODE);

            if (stl.getCurrentTab() != 0){
                stl.setCurrentTab(0);
            }
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        if (stl.getCurrentTab() != 0){
            stl.setCurrentTab(0);
        }
        List<MyOrder> myOrders;
        //查找客户
        if (!TextUtils.isEmpty(query)){
            myOrders = LitePal.where("customername like ?", "%"+query+"%").find(MyOrder.class);
        }else{
            myOrders = LitePal.findAll(MyOrder.class);
        }

//
//        //查找订单状态
//        if (myOrders== null || myOrders.isEmpty()){
//            myOrders =   LitePal.where("orderstate like ?","%"+query+"%").find(MyOrder.class);
//        }
        if (myOrders!= null){
            orderList.clear();
            orderList.addAll(myOrders);
            notifyData();
        }

        statistics();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESQUEST_CODE){
            Bundle bundle = data.getExtras();
            if (bundle != null){

                //过滤数据
                int type = bundle.getInt("type");
                String customer = bundle.getString("customer");
                long startDate = bundle.getLong("startDate");
                long endDate = bundle.getLong("endDate");
                int orderState = bundle.getInt("orderState");


                StringBuilder sql = new StringBuilder("1=1 ");
                List<String> conditions = new ArrayList<>();
//                conditions.add("");
                if(type!= 0){
                    sql.append("and state=? ");
                    conditions.add(String.valueOf(type-1));
                }

            if (!TextUtils.isEmpty(customer)){
                    sql.append("and customername=? ");
                    conditions.add(customer);
                }
                if (startDate != 0){
                    sql.append("and time>=? ");
                    conditions.add(String.valueOf(startDate));
                }

                if (endDate != 0){
                    sql.append("and time<=? ");
                    conditions.add(String.valueOf(endDate));

                }

                if (orderState != 0){
                    sql.append("and orderstate=? ");
                    conditions.add(String.valueOf(type-1));
                }

//                conditions.set(0, sql.toString());
//                String[] conditionArray = (String[]) conditions.toArray();
//                String[] conditionArray = new String[conditions.size()];

                String[] conditionArray = new String[conditions.size()+1];
                conditionArray[0] = sql.toString();
                Log.i(TAG, "onActivityResult: "+ conditionArray[0]);
                for (int i = 1; i < conditions.size()+1; i++) {
                    conditionArray[i] = conditions.get(i-1);
                    Log.i(TAG, "onActivityResult: "+ conditionArray[i]);
                }

//                conditions.toArray(conditionArray);

                List<MyOrder> myOrders = LitePal.where(conditionArray).find(MyOrder.class);
                orderList.clear();
                orderList.addAll(myOrders);
                notifyData();

                statistics();

            }

        }
    }
}
