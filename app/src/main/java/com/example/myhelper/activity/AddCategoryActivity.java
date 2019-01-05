package com.example.myhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myhelper.R;
import com.example.myhelper.entity.Product;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

import butterknife.BindView;

public class AddCategoryActivity extends BaseActivity {


    @BindView(R.id.et_category_name)
    EditText etCategoryName;
    @BindView(R.id.et_cost)
    EditText etCost;
    @BindView(R.id.et_cost2)
    EditText etCost2;
    @BindView(R.id.et_cost1)
    EditText etCost1;
    @BindView(R.id.et_retail_price)
    EditText etRetailPrice;
    @BindView(R.id.et_warn_count)
    EditText etWarnCount;
    @BindView(R.id.et_effect)
    EditText etEffect;
    private Product mProduct;
    private boolean isShowSave = true;
    private MenuItem menuSaveitem;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_category;
    }

    @Override
    protected void init() {
        super.init();

        setToolbar("添加品种",true);


        Intent fromIntent = getIntent();
        Bundle bundle = fromIntent.getExtras();
        if (bundle != null){
            mProduct = (Product) bundle.get("product");
            if (mProduct != null){//从列表界面过来的
                isShowSave = false;
                setDatas();
                setEditTextEditable(false);
            }
        }


    }

    private void setDatas() {
        etCategoryName.setText(mProduct.getName());
        etCost.setText(""+mProduct.getCostPrice());
        etCost1.setText(""+mProduct.getPrice1());
        etCost2.setText(""+mProduct.getPrice2());
        etRetailPrice.setText(""+mProduct.getRetailPrice());
        etWarnCount.setText(mProduct.getWanrCount()+"");
        etEffect.setText(mProduct.getEffect());



    }

    //设置edittext是否可以点击
    private void setEditTextEditable(boolean isClick) {
        etCategoryName.setEnabled(isClick);
        etCost.setEnabled(isClick);
        etCost1.setEnabled(isClick);
        etCost2.setEnabled(isClick);
        etRetailPrice.setEnabled(isClick);
        etWarnCount.setEnabled(isClick);
        etEffect.setEnabled(isClick);
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
        }else{
            menuSaveitem.setTitle("编辑");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save){

            if(isShowSave){
                saveCategory();
                finish();
            }else {
                isShowSave = true;
            }
            setState(isShowSave);
            setEditTextEditable(isShowSave);

        }


        return super.onOptionsItemSelected(item);

    }

    private void saveCategory() {
        String name = etCategoryName.getText().toString().trim();
        String costStr = etCost.getText().toString().trim();
        String costStr1 = etCost1.getText().toString().trim();
        String costStr2 = etCost2.getText().toString().trim();
        String retailStr = etRetailPrice.getText().toString().trim();
        String warnCountStr = etWarnCount.getText().toString().trim();
        String effect = etEffect.getText().toString().trim();

        Double cost=0.0;
        Double cost1=0.0;
        Double cost2=0.0;
        Double retailPrice=0.0;
        Integer warnCount=0;
        if (!TextUtils.isEmpty(costStr)){
            cost = Double.valueOf(costStr);
        }
        if (!TextUtils.isEmpty(costStr1)){
            cost1 = Double.valueOf(costStr1);
        }
        if (!TextUtils.isEmpty(costStr2)){
            cost2 = Double.valueOf(costStr2);
        }
        if (!TextUtils.isEmpty(retailStr)){
            retailPrice = Double.valueOf(retailStr);
        }
        if (!TextUtils.isEmpty(warnCountStr)){
            warnCount = Integer.valueOf(warnCountStr);
        }

        Product product = new Product();
        product.setName(name);
        product.setCostPrice(cost);
        product.setPrice1(cost1);
        product.setPrice2(cost2);
        product.setRetailPrice(retailPrice);
        product.setEffect(effect);
        product.setWanrCount(warnCount);
//        product.save();
        boolean result = product.saveOrUpdate("name = ?", name);

        if (result){
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
        }


    }

}
