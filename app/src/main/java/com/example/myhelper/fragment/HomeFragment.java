package com.example.myhelper.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.myhelper.MainActivity;
import com.example.myhelper.R;
import com.example.myhelper.activity.CategoryActivity;
import com.example.myhelper.activity.InStorageActivity;
import com.example.myhelper.activity.OutStorageActivity;
import com.example.myhelper.activity.RecordActivity;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;
import com.example.myhelper.utils.CharManager;
import com.example.myhelper.utils.DateUtil;
import com.example.myhelper.utils.DensityUtils;
import com.example.myhelper.utils.ToastUtil;
import com.example.myhelper.widget.MonPickerDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.chart)
    PieChart mPieChart;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    private int mYear;
    private int mMonth;
    //当月总收入
    private double totalIncome = 0;
    //当月总成本
    private double totalCost = 0;
    //图表数据
    private ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void loadData() {
        //查找酵素种类
        List<Product> products = LitePal.findAll(Product.class);
        tvCategoryCount.setText(products.size() + "");

        //库存数量
        int count = 0;
        for (Product product : products) {
            count = count + product.getCount();
        }
        tvStockCount.setText(count + "");

        //今日入库
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date);
        List<MyOrder> inOrders = null;
        try {
            Date date1 = DateUtil.parse2Date(dateStr);

            inOrders = LitePal.where("time>=? and state=?", String.valueOf(date1.getTime()), "1").find(MyOrder.class);


            int inCount = 0;
            for (MyOrder order : inOrders) {
                inCount = inCount + order.getNumber();
            }

            tvTodayInCount.setText(inCount + "");

            //今日出库
            List<MyOrder> outOrders = LitePal.where("time>=? and state=?", String.valueOf(date1.getTime()), "0").find(MyOrder.class);
            int outCount = 0;
            for (MyOrder order : outOrders) {
                outCount = outCount + order.getNumber();
            }

            tv_TodayOutCount.setText(outCount + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }



        //当月查询收入支出
        findInCome();

        updateIncomeChart();
    }

    private void updateIncomeChart(){
//        CharManager charManager = CharManager.getInstance(mActivity, mPieChart);
        CharManager charManager = new CharManager(mActivity,mPieChart);

        //设置中间文字
        double profit = totalIncome - totalCost;
        String centerText = "本月利润\n"+profit+"元";
        charManager.setCenterText(centerText);

        //重新设置数据
        entries.clear();
        entries.add(new PieEntry(Double.valueOf(totalIncome).intValue(), "总收入"));
        entries.add(new PieEntry(Double.valueOf(totalCost).intValue(), "总支出"));

        mPieChart.notifyDataSetChanged();
//        mPieChart.invalidate();
        charManager.setData(entries);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        tvYear.setText(mYear+"年");
        tvMonth.setText(mMonth+1+"月");



    }



    @OnClick({R.id.ll_category, R.id.ll_count, R.id.ll_today_in, R.id.ll_today_out, R.id.ll_in_storage,
            R.id.ll_out_storage, R.id.ll_stock, R.id.ll_record,R.id.ll_date})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_category:
                intent = new Intent(mActivity, CategoryActivity.class);
                startActivity(intent);

                break;
            case R.id.ll_count:
//                intent = new Intent(mActivity, StockActivity.class);
//                startActivity(intent);
                //替换为库存fragment
                MainActivity activity = (MainActivity) mActivity;
                activity.setCurrentItem(1);
                break;
            case R.id.ll_today_in:
                intent = new Intent(mActivity, RecordActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
            case R.id.ll_today_out:
                intent = new Intent(mActivity, RecordActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
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
                ToastUtil.showToast("后续开发...");
                break;
            case R.id.ll_record://出入库记录
                intent = new Intent(mActivity, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_date://选择年月
                onCreateDialog();
                break;
        }
    }


    private void onCreateDialog() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), mDateSetListener, yy, mm, dd) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                LinearLayout mSpinners = (LinearLayout) findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
                if (mSpinners != null) {
                    NumberPicker mMonthSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
                    NumberPicker mYearSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
                    mSpinners.removeAllViews();
                    if (mMonthSpinner != null) {
                        mSpinners.addView(mMonthSpinner);
                    }
                    if (mYearSpinner != null) {
                        mSpinners.addView(mYearSpinner);
                    }
                }
                View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
                if(dayPickerView != null){
                    dayPickerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                super.onDateChanged(view, year, month, day);

            }
        };
        dlg.setTitle("请选择要查询的年月");
        dlg.show();

    }



    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;

            tvYear.setText(year+"年");
            tvMonth.setText(monthOfYear+1+"月");

            //查询收支
            findInCome();
            //展示
            updateIncomeChart();
        }
    };

    private void findInCome() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear,mMonth,1);

        long startTime = calendar.getTimeInMillis();
        calendar.set(mYear,mMonth+1,1);
        long endTime = calendar.getTimeInMillis();

        totalIncome = 0;
        totalCost = 0;

        List<MyOrder> myOrders = LitePal.where("state=0 and time>=? and time<=?", String.valueOf(startTime)
                , String.valueOf(endTime)).find(MyOrder.class);

        for (MyOrder myOrder : myOrders) {
            totalIncome = totalIncome+ myOrder.getTotalPrice();
            totalCost = totalCost+ myOrder.getTotalCost();
        }
    }
}
