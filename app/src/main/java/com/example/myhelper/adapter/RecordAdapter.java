package com.example.myhelper.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.activity.OrderDetailActivity;
import com.example.myhelper.entity.MyOrder;
import com.example.myhelper.entity.Product;
import com.example.myhelper.utils.AnimationUtils;
import com.example.myhelper.utils.GsonUtil;
import com.example.myhelper.utils.RichTextUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/7.
 */

public class RecordAdapter extends RecyclerView.Adapter {
    private final Context context;

    private List<MyOrder> mList = new ArrayList<>();
    private int[] clickState;

    public RecordAdapter(Context context) {

        this.context = context;
    }

    public void setDatas(List<MyOrder> list) {
        mList.clear();
        mList.addAll(list);
        clickState = new int[mList.size()];
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_item, parent, false);
        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final RecordHolder h = (RecordHolder) holder;
        h.setData(position);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到点单详情
                Intent intent = new Intent(context, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order",mList.get(position));
                bundle.putString("from","RecordActivity");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class RecordHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order_type)
        TextView tvOrderType;
        @BindView(R.id.tv_customer_title)
        TextView tvCustomerTitle;
        @BindView(R.id.tv_customer)
        TextView tvCustomer;
        @BindView(R.id.tv_product)
        TextView tvProduct;
        @BindView(R.id.tv_order_state)
        TextView tvOrderState;
        @BindView(R.id.tv_total_count)
        TextView tvCount;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;

        public RecordHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }


        public void setData(int position) {
            MyOrder myOrder = mList.get(position);
            tvOrderType.setText(myOrder.getState()==0?"出库":"入库");
            if (myOrder.getState()==0){
                tvOrderType.setText("出库");
                tvOrderType.setTextColor(context.getResources().getColor(R.color.text_color4));
                tvCustomer.setText(myOrder.getCustomerName());
//                tvPrice.setText("合计："+myOrder.getTotalPrice());
                tvPrice.setText("合计："+myOrder.getActualPayment());

            }else{
                tvOrderType.setText("入库");
                tvCustomerTitle.setVisibility(View.GONE);
                tvCustomer.setVisibility(View.GONE);
                tvPrice.setText("合计："+myOrder.getTotalCost());
            }

            if (myOrder.getOrderState() == 0){
                tvOrderState.setText("已完成");
                tvOrderState.setTextColor(context.getResources().getColor(R.color.text_color3));
//                String html="<font color='#075606'>已完成</font> <br>";
//                RichTextUtils.settext(tvOrderState,html);

            }else if(myOrder.getOrderState() == 1){
                tvOrderState.setText("未支付");
                tvOrderState.setTextColor(context.getResources().getColor(R.color.text_color1));
            }else{
                tvOrderState.setText("未全部发货");
                tvOrderState.setTextColor(context.getResources().getColor(R.color.text_color2));
            }

            tvOrderTime.setText(myOrder.getTime());

            tvCount.setText("共"+myOrder.getNumber()+"件");

            String productDetail = myOrder.getProductDetail();
            List<Product> productList = (List<Product>) GsonUtil.parseJsonToList(productDetail, new TypeToken<List<Product>>() {
            }.getType());

            StringBuilder sb = new StringBuilder();
            for (Product product : productList) {
                sb.append(product.getName()+"x"+product.getCount()+"  ");
            }


            tvProduct.setText(sb.toString());


//            if(clickState[position] == 1){
//                rvDetail.setVisibility(View.VISIBLE);
//                rvDetail.setLayoutManager(new LinearLayoutManager(context));
//                ProductAdapter productAdapter = new ProductAdapter(context);
//                rvDetail.setAdapter(productAdapter);
//                productAdapter.setDatas(productList);
//                ivMore.setImageResource(R.drawable.arrow_up);
//            }else{
//                rvDetail.setVisibility(View.GONE);
//                ivMore.setImageResource(R.drawable.icon_unfold);
//            }


        }
    }
}
