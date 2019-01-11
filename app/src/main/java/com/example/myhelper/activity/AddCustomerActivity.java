package com.example.myhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.myhelper.R;
import com.example.myhelper.entity.Customer;
import com.example.myhelper.utils.InputMethodUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCustomerActivity extends BaseActivity {


    @BindView(R.id.et_customer_name)
    EditText etCustomerName;
    @BindView(R.id.et_wx)
    EditText etWx;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_other)
    EditText etOther;
    @BindView(R.id.tv_agent_level)
    TextView tvAgentLevel;

    private List<String> agentLevels = new ArrayList<>();
    private MenuItem menuSaveitem;
    private Customer customer;
    private boolean isShowSave = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_customer;
    }

    @Override
    protected void init() {
        super.init();
        setToolbar("添加客户", true);
        agentLevels.add("普通客户");//0
        agentLevels.add("会员");//1
        agentLevels.add("贵宾");//2
        agentLevels.add("银钻");//3
        agentLevels.add("金钻");//4
        tvAgentLevel.setText(agentLevels.get(0));

        Intent intent = getIntent();
        String customerName = intent.getStringExtra("customer_name");
        if (!TextUtils.isEmpty(customerName)){
            customer = LitePal.where("name=?", customerName).findFirst(Customer.class);
                isShowSave = false;
                setDatas();
                setEditTextEditable(false);

        }
    }
    private void setDatas() {
        etCustomerName.setText(customer.getName());
        etWx.setText(customer.getWx());
        etPhone.setText(customer.getPhone());
        tvAgentLevel.setText(agentLevels.get(customer.getLevel()));
        etAddress.setText(customer.getAddress());
        etOther.setText(customer.getOther());



    }

    //设置edittext是否可以点击
    private void setEditTextEditable(boolean isClick) {
        etCustomerName.setEnabled(isClick);
        etWx.setEnabled(isClick);
        etPhone.setEnabled(isClick);
        tvAgentLevel.setEnabled(isClick);
        etAddress.setEnabled(isClick);
        etOther.setEnabled(isClick);
    }



    @OnClick(R.id.tv_agent_level)
    public void onViewClicked() {

        InputMethodUtils.hintKeyBoard(this,getCurrentFocus());

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int op3, View v) {

                String agentLevtl = agentLevels.get(options1);
                tvAgentLevel.setText(agentLevtl);

            }
        }).build();
        pvOptions.setPicker(agentLevels);
        pvOptions.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_category_menu,menu);
        menuSaveitem = menu.findItem(R.id.save);
        setState(isShowSave);

        return super.onCreateOptionsMenu(menu);
    }

    private void setState(boolean isShowSave) {
        if(isShowSave){
            menuSaveitem.setTitle("保存");
            toolbar.setTitle("添加客户");
        }else{
            menuSaveitem.setTitle("编辑");
            toolbar.setTitle("客户详情");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save){


            if(isShowSave){
                saveCustomer();
                finish();
            }else {
                isShowSave = true;
            }
            setState(isShowSave);
            setEditTextEditable(isShowSave);

        }


        return super.onOptionsItemSelected(item);

    }

    private void saveCustomer() {
        String name = etCustomerName.getText().toString().trim();
        String wx = etWx.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String other = etOther.getText().toString().trim();
        String agnetLevel = tvAgentLevel.getText().toString();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setWx(wx);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setOther(address);
        int level = agentLevels.indexOf(agnetLevel);
        customer.setLevel(level);
        boolean result = customer.saveOrUpdate("name=?", name);
        if (result){
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
        }
        finish();

    }
}
