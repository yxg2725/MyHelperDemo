package com.example.myhelper.activity;

import android.os.Bundle;
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
    @BindView(R.id.et_photo)
    EditText etPhoto;
    @BindView(R.id.tv_agent_level)
    TextView tvAgentLevel;

    private List<String> agentLevels = new ArrayList<>();
    private MenuItem menuSaveitem;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_customer;
    }

    @Override
    protected void init() {
        super.init();
        setToolbar("添加客户", true);
        agentLevels.add("普通客户");
        agentLevels.add("一级代理");
        agentLevels.add("二级代理");
        tvAgentLevel.setText(agentLevels.get(0));
    }



    @OnClick(R.id.tv_agent_level)
    public void onViewClicked() {

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
//        setState(isShowSave);

        return super.onCreateOptionsMenu(menu);
    }

    private void setState(boolean isShowSave) {
        if(isShowSave){
            menuSaveitem.setTitle("保存");
        }else{
            menuSaveitem.setTitle("编辑");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save){

            saveCustomer();
            /*if(isShowSave){
                saveCategory();
                finish();
            }else {
                isShowSave = true;
            }
            setState(isShowSave);
            setEditTextEditable(isShowSave);*/

        }


        return super.onOptionsItemSelected(item);

    }

    private void saveCustomer() {
        String name = etCustomerName.getText().toString().trim();
        String wx = etWx.getText().toString().trim();
        String photo = etPhoto.getText().toString().trim();
        String agnetLevel = tvAgentLevel.getText().toString();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setWx(wx);
        customer.setPhone(photo);
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
