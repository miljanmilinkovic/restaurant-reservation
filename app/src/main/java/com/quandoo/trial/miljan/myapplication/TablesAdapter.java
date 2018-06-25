package com.quandoo.trial.miljan.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quandoo.trial.miljan.myapplication.presenter.ReservationPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.ListViewHolder>
        implements DropListener {

    private boolean[] mTableArray;
    private ReservationPresenter mPresenter;

    TablesAdapter(ReservationPresenter presenter) {
        mPresenter = presenter;
        mTableArray = new boolean[0];
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.table_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.text.setText(mTableArray[position] ? "R" : "");
        holder.frameLayout.setBackgroundResource(mTableArray[position] ? R.drawable.purple_circle : R.drawable.white_circle);
        if (mTableArray[position]) {
            holder.frameLayout.setOnDragListener(null);
        } else {
            holder.frameLayout.setTag(position);
            holder.frameLayout.setOnDragListener(new DragListener(this));
        }
    }

    @Override
    public int getItemCount() {
        return mTableArray.length;
    }

    boolean[] getArray() {
        return mTableArray;
    }

    void updateArray(boolean[] tableArray) {
        mTableArray = tableArray;
    }

    DragListener getDragInstance() {
        return new DragListener(this);
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

    public void dropClientAtTable(int table, int customer) {
        mPresenter.reserveTable(table, customer);
    }
}
