package com.example.myhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.entity.MyOrder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/2.
 */

public class OrderAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final List<MyOrder> mList = new ArrayList<>();



    public OrderAdapter(Context context) {

        this.context = context;

    }

    public void setData(List<MyOrder> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.order_item, null);
        View view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryHolder vieHolder = (CategoryHolder) holder;
        vieHolder.setData(position);
        /*vieHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product",mList.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    class CategoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_total_price)
        TextView tvTotalPrice;
        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            MyOrder myOrder = mList.get(position);
            tvProductName.setText(myOrder.getProductName());
            tvTime.setText(myOrder.getTime());
            tvCount.setText(myOrder.getNumber()+"");
            tvTotalPrice.setText(myOrder.getTotalCost()+"");
        }
    }


}
