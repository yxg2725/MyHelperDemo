package com.example.myhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.entity.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/9.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter {

    private List<Product> mList = new ArrayList<>();
    private Context context;
    private double total = 0;//总金额
    private int totalCount = 0;//总数量
    private int state;//出库 0  入库1

    public OrderDetailAdapter(Context context) {
        this.context = context;

    }


    public void setDatas(List<Product> list, int state) {
        this.state = state;
        this.mList.clear();
        this.mList.addAll(list);

        for (Product product : mList) {
            if (state == 0){
                total = total + product.getCount()*product.getRetailPrice();
            }else if(state == 1){
                total = total + product.getCount()*product.getCostPrice();
            }

            totalCount = totalCount + product.getCount();
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_detail_item, parent, false);
        return new OrderDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderDetailHolder h = (OrderDetailHolder) holder;
        h.setData(position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class OrderDetailHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_total_price)
        TextView tvTotalPrice;
        @BindView(R.id.view_division)
        View viewDivision;
        @BindView(R.id.tv_total)
        TextView tvTotal;
        @BindView(R.id.tv_total_count)
        TextView tvTotalCount;
        @BindView(R.id.ll_total)
        LinearLayout llTotal;

        public OrderDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(int position) {
            Product product = mList.get(position);
            tvProductName.setText(product.getName());
            tvCount.setText(product.getCount()+"");

            if (state==0){
                tvTotalPrice.setText(product.getCount()*product.getRetailPrice()+"");
            }else if(state==1){
                tvTotalPrice.setText(product.getCount()*product.getCostPrice()+"");
            }

            tvDate.setText(product.getDate());
            tvTotal.setText("总计：￥"+total);
            tvTotalCount.setText("总数量："+totalCount);
            if (position == 0){
                viewDivision.setVisibility(View.GONE);
                llTotal.setVisibility(View.GONE);
            }else{
                if (mList.get(position-1).getDate().equals(product.getDate())){
                    viewDivision.setVisibility(View.GONE);
                    llTotal.setVisibility(View.GONE);
                }else{
                    llTotal.setVisibility(View.VISIBLE);
                    viewDivision.setVisibility(View.VISIBLE);
                }
            }




            if (position != mList.size()-1){
                viewDivision.setVisibility(View.GONE);
                tvTotal.setVisibility(View.GONE);
            }else{//最后一个
                llTotal.setVisibility(View.VISIBLE);
                viewDivision.setVisibility(View.VISIBLE);
            }
        }
    }
}
