package com.yiqizhuan.app.ui.search.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * 搜索推荐第一个
 */
public class RecommendFirstFlexibleItem extends AbstractFlexibleItem<RecommendFirstFlexibleItem.ItemViewHolder> {
    Context context;

    public RecommendFirstFlexibleItem(Context context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_search_recommend_first;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv1;
        TextView tvName1;
        ImageView iv2;
        TextView tvName2;
        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv1 = itemView.findViewById(R.id.iv1);
            tvName1 = itemView.findViewById(R.id.tvName1);
            iv2 = itemView.findViewById(R.id.iv2);
            tvName2 = itemView.findViewById(R.id.tvName2);
        }

    }

}
