package com.example.myhelper.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myhelper.R;
import com.example.myhelper.entity.Customer;

import org.litepal.LitePal;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/11.
 */

public class FilterWindow {


    private static  FilterWindow ourInstance ;
//    private Activity context;
    private WeakReference<Activity> context;
    private View view;
    private String[] types= new String[]{"全部","出库","入库"};
    private String[] orderStates= new String[]{"全部","已完成","未支付","未全部发货"};
    private ArrayList<String> typeList = new ArrayList<>();
    private ArrayList<String> orderStateList = new ArrayList<>();
    private final List<Customer> customerList;
    private PopupWindow window;
    private AlertDialog filterDialog;

    public static FilterWindow getInstance(Activity context) {
        if (ourInstance == null){
            ourInstance = new FilterWindow(context);
        }
        return ourInstance;
    }

    private FilterWindow(Activity ctx) {
//        this.context = context;
        context = new WeakReference<>(ctx);

        Collections.addAll(typeList, types);
        Collections.addAll(orderStateList, orderStates);

        customerList = LitePal.findAll(Customer.class);
    }

    public void setAnchorView(View view){
        this.view = view;
    }

    public void showFilterWindow() {
        // 用于PopupWindow的View
        context.get();
        if (context.get() != null){
//            View contentView = LayoutInflater.from(context.get()).inflate(R.layout.order_filter_layout, null, false);
//            ButterKnife.bind(this,contentView);

//            window = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT, true);
//            window.setOutsideTouchable(false);
//            window.setTouchable(false);
//            window.showAsDropDown(view, 0, 0);
//            window.showAtLocation(context.get().getWindow().getDecorView(), Gravity.RIGHT, 0, 0);




            AlertDialog.Builder builder = new AlertDialog.Builder(context.get());
            View view = LayoutInflater.from(context.get()).inflate(R.layout.order_filter_layout,
                    null);
            ButterKnife.bind(this,view);
            builder.setView(view);
            filterDialog = builder.create();
            filterDialog.setCanceledOnTouchOutside(false);
            Window window = filterDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.x = 0;
            params.y = 0;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            filterDialog.show();
            window.setGravity(Gravity.TOP);

        }

    }
    public PopupWindow getPopuWindow(){
        return window;
    }


}
