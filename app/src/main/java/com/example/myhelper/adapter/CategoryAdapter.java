package com.example.myhelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.activity.AddCategoryActivity;
import com.example.myhelper.activity.CategoryActivity;
import com.example.myhelper.entity.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/2.
 */

public class CategoryAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final List<Product> mList = new ArrayList<>();
    private boolean showCount = false;


    public CategoryAdapter(Context context, List<Product> list) {

        this.context = context;
        if (list != null && !list.isEmpty()){
            mList.clear();
            mList.addAll(list);
        }

    }

    public void setData(List<Product> list,boolean showCount){
        mList.clear();
        mList.addAll(list);
        this.showCount = showCount;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.category_item, null);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryHolder vieHolder = (CategoryHolder) holder;
        vieHolder.setData(position);
        vieHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product",mList.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }


    class CategoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_product_src)
        ImageView ivProductSrc;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_effect)
        TextView tvEffect;
        @BindView(R.id.tv_price_title)
        TextView tvPriceTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_count_title)
        TextView tvCountTitle;
        @BindView(R.id.tv_count)
        TextView tvCount;
        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            Product product = mList.get(position);

            tvProductName.setText(product.getName());
            tvEffect.setText(product.getEffect());
            tvPrice.setText("￥"+product.getRetailPrice());
            tvCount.setText(product.getCount()+"");

            if (!showCount){
                tvCountTitle.setVisibility(View.GONE);
                tvCount.setVisibility(View.GONE);
            }


        }
    }



}
