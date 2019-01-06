package com.example.myhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myhelper.R;
import com.example.myhelper.entity.ContactInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoustomerAdapter extends RecyclerView.Adapter {

    private List<ContactInfo> mList = new ArrayList<>();
    private final Context mActivity;

    public CoustomerAdapter(Context mActivity) {

        this.mActivity = mActivity;
    }

    public void setDatas(List<ContactInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.lv_contact_list_item, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContactHolder viewHolder = (ContactHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_first_letter)
        TextView tvFirstLetter;
        @BindView(R.id.tv_name)
        TextView tvName;
        public ContactHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(int position) {
            ContactInfo contactInfo = mList.get(position);
            tvFirstLetter.setText(contactInfo.getFirstLetter());
            tvName.setText(contactInfo.getName());

            if (position == 0) {
                tvFirstLetter.setVisibility(View.VISIBLE);
            } else {
                //如果当前条目对应的首字母和上一个一致,则让首字母隐藏
                ContactInfo lastInfo = mList.get(position - 1);
                if (lastInfo.getFirstLetter().equals(contactInfo.getFirstLetter())) {
                    tvFirstLetter.setVisibility(View.GONE);
                } else {
                    tvFirstLetter.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
