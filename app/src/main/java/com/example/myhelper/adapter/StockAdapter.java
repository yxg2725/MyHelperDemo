package com.example.myhelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.myhelper.R;

/**
 * Created by Administrator on 2019/1/2.
 */

public class StockAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.stock_item,null);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    static class CategoryHolder extends RecyclerView.ViewHolder{

        public CategoryHolder(View itemView) {
            super(itemView);
        }
    }
}
