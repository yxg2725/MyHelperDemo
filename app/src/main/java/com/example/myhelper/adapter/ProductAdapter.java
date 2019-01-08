package com.example.myhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.entity.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/8.
 */

public class ProductAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Product> mList = new ArrayList<>();


    public ProductAdapter(Context context) {

        this.context = context;
    }

    public void setDatas(List<Product> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.out_in_item, parent, false);
        return new OderDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OderDetailHolder h = (OderDetailHolder) holder;
        h.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class OderDetailHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_total_price)
        TextView tvTotalPrice;
        public OderDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(int position) {
            Product product = mList.get(position);
            tvProductName.setText(product.getName());
            tvCount.setText(product.getCount()+"");
            tvTotalPrice.setText(product.getCount() * product.getCostPrice()+"");
        }
    }
}
