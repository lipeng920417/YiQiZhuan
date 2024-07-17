package com.yiqizhuan.app.ui.home.item;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * 没有更多了
 */
public class NoDataFlexibleItem extends AbstractFlexibleItem<NoDataFlexibleItem.ItemViewHolder> {


    public NoDataFlexibleItem(Context context) {
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_no_data;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        holder.bindData();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void bindData() {

        }
    }

}
