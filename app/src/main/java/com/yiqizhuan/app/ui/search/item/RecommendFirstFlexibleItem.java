package com.yiqizhuan.app.ui.search.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.SkipActivityUtil;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * 搜索推荐第一个
 */
public class RecommendFirstFlexibleItem extends AbstractFlexibleItem<RecommendFirstFlexibleItem.ItemViewHolder> {
    Context context;
    ProductListBean.Detail detailsDTO1;
    ProductListBean.Detail detailsDTO2;

    public RecommendFirstFlexibleItem(Context context, ProductListBean.Detail detailsDTO1, ProductListBean.Detail detailsDTO2) {
        this.context = context;
        this.detailsDTO1 = detailsDTO1;
        this.detailsDTO2 = detailsDTO2;
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
        if (detailsDTO1 != null) {
            GlideUtil.loadImage(detailsDTO1.getMainImage(), holder.iv1, 5);
            holder.tvName1.setText(detailsDTO1.getProductName());
            holder.rly1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipActivityUtil.goGoodsDetail(context, detailsDTO1.getProductId() + "", detailsDTO1.getProductType() + "");
                }
            });
        }
        if (detailsDTO2 != null) {
            GlideUtil.loadImage(detailsDTO2.getMainImage(), holder.iv2, 5);
            holder.tvName2.setText(detailsDTO2.getProductName());
            holder.rly2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipActivityUtil.goGoodsDetail(context, detailsDTO2.getProductId() + "", detailsDTO2.getProductType() + "");
                }
            });
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rly1;
        ImageView iv1;
        TextView tvName1;

        RelativeLayout rly2;
        ImageView iv2;
        TextView tvName2;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rly1 = itemView.findViewById(R.id.rly1);
            iv1 = itemView.findViewById(R.id.iv1);
            tvName1 = itemView.findViewById(R.id.tvName1);
            rly2 = itemView.findViewById(R.id.rly2);
            iv2 = itemView.findViewById(R.id.iv2);
            tvName2 = itemView.findViewById(R.id.tvName2);
        }
    }

}
