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
 * Created by Administrator on 2019/1/2.
 */

public class StockAdapter extends RecyclerView.Adapter {
    private final Context mActivity;

    private List<Product> mList = new ArrayList<>();

    public StockAdapter(Context mActivity) {
        this.mActivity = mActivity;
    }

    public void setDatas(List<Product> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.stock_item, null);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.stock_item,parent,false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryHolder viewHolder = (CategoryHolder) holder;
        viewHolder.setDatas(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class CategoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_count)
        TextView tvCount;
        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDatas(int position) {
            Product product = mList.get(position);
            tvCategoryName.setText(product.getName());
            tvCount.setText("剩"+product.getCount()+"瓶");
        }
    }
}
