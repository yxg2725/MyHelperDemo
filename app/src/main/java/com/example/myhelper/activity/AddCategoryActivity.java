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
    @BindView(R.id.et_member_price)//会员价
    EditText etMember;
    @BindView(R.id.et_vip_price)
    EditText etVip;
    @BindView(R.id.et_silver_price)
    EditText etSilver;
    @BindView(R.id.et_gold_price)
    EditText etGold;
    @BindView(R.id.et_retail_price)
    EditText etRetailPrice;
//    @BindView(R.id.et_warn_count)
//    EditText etWarnCount;
    @BindView(R.id.et_other)
    EditText etOther;
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
        etMember.setText(""+mProduct.getMemberPrice());
        etVip.setText(""+mProduct.getVipPrice());
        etSilver.setText(""+mProduct.getSilverPrice());
        etGold.setText(""+mProduct.getGoldPrice());
        etRetailPrice.setText(""+mProduct.getRetailPrice());
//        etWarnCount.setText(mProduct.getWanrCount()+"");
        etOther.setText(mProduct.getOther());



    }

    //设置edittext是否可以点击
    private void setEditTextEditable(boolean isClick) {
        etCategoryName.setEnabled(isClick);
        etCost.setEnabled(isClick);
        etMember.setEnabled(isClick);
        etVip.setEnabled(isClick);
        etSilver.setEnabled(isClick);
        etRetailPrice.setEnabled(isClick);
        etGold.setEnabled(isClick);
//        etWarnCount.setEnabled(isClick);
        etOther.setEnabled(isClick);
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
        String memberStr = etMember.getText().toString().trim();
        String vipStr = etVip.getText().toString().trim();
        String retailStr = etRetailPrice.getText().toString().trim();
        String silverStr = etSilver.getText().toString().trim();
        String goldStr = etGold.getText().toString().trim();
        String other = etOther.getText().toString().trim();

        Double cost=0.0;
        Double memberPrice=0.0;
        Double vipPrice=0.0;
        Double silverPrice=0.0;
        Double goldPrice=0.0;
        Double retailPrice=0.0;
        Integer warnCount=0;
        if (!TextUtils.isEmpty(costStr)){
            cost = Double.valueOf(costStr);
        }
        if (!TextUtils.isEmpty(memberStr)){
            memberPrice = Double.valueOf(memberStr);
        }
        if (!TextUtils.isEmpty(vipStr)){
            vipPrice = Double.valueOf(vipStr);
        }
        if (!TextUtils.isEmpty(silverStr)){
            silverPrice = Double.valueOf(silverStr);
        }
        if (!TextUtils.isEmpty(goldStr)){
            goldPrice = Double.valueOf(goldStr);
        }
        if (!TextUtils.isEmpty(retailStr)){
            retailPrice = Double.valueOf(retailStr);
        }
//        if (!TextUtils.isEmpty(warnCountStr)){
//            warnCount = Integer.valueOf(warnCountStr);
//        }

        Product product = new Product();
        product.setName(name);
        product.setCostPrice(cost);
        product.setMemberPrice(memberPrice);
        product.setVipPrice(vipPrice);
        product.setRetailPrice(retailPrice);
        product.setSilverPrice(silverPrice);
        product.setGoldPrice(goldPrice);
        product.setOther(other);
//        product.setWanrCount(warnCount);
//        product.save();
        boolean result = product.saveOrUpdate("name = ?", name);

        if (result){
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
        }


    }

}
