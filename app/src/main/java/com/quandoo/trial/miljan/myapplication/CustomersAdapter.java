package com.quandoo.trial.miljan.myapplication;

import android.content.ClipData;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quandoo.trial.miljan.myapplication.model.CustomerModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ListViewHolder>
        implements View.OnTouchListener{

    private List<CustomerModel> mCustomerList;

    CustomersAdapter() {
        mCustomerList = new ArrayList<CustomerModel>();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.customer_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.text.setText(mCustomerList.get(position).getLabel());
        holder.frameLayout.setTag(mCustomerList.get(position).getId());
        holder.frameLayout.setOnTouchListener(this);
    }

    @Override
    public int getItemCount() {
        return mCustomerList.size();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(data, shadowBuilder, v.getTag(), 0);
                } else {
                    v.startDrag(data, shadowBuilder, v.getTag(), 0);
                }
                return true;
        }
        return false;
    }

    List<CustomerModel> getList() {
        return mCustomerList;
    }

    void updateList(List<CustomerModel> customerList) {
        mCustomerList = customerList;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.frame_layout_item)
        FrameLayout frameLayout;

        ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
