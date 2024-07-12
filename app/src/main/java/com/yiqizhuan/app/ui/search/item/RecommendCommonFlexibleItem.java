package com.yiqizhuan.app.ui.search.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * 搜索推荐常规
 */
public class RecommendCommonFlexibleItem extends AbstractFlexibleItem<RecommendCommonFlexibleItem.ItemViewHolder> {
    Context context;
    ProductListBean.Detail detailsDTO1;

    public RecommendCommonFlexibleItem(Context context, ProductListBean.Detail detailsDTO1) {
        this.context = context;
        this.detailsDTO1 = detailsDTO1;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_search_recommend_common;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (detailsDTO1 != null) {
            GlideUtil.loadImage(detailsDTO1.getMainImage(), holder.iv1, 5);
            if (position == 0) {
                holder.iv.setVisibility(View.VISIBLE);
                holder.iv.setImageResource(R.mipmap.ic_recommend_num1);
                holder.tvNum.setVisibility(View.GONE);
            } else if (position == 1) {
                holder.iv.setVisibility(View.VISIBLE);
                holder.iv.setImageResource(R.mipmap.ic_recommend_num2);
                holder.tvNum.setVisibility(View.GONE);
            } else {
                holder.iv.setVisibility(View.GONE);
                holder.tvNum.setVisibility(View.VISIBLE);
                holder.tvNum.setText((position + 1) + "");
            }
            holder.tvName.setText(detailsDTO1.getProductName());
            StringBuilder tag = new StringBuilder();
            if (detailsDTO1.getTags() != null) {
                for (int i = 0; i < detailsDTO1.getTags().size(); i++) {
                    tag.append(detailsDTO1.getTags().get(i));
                    if (i < detailsDTO1.getTags().size() - 1) {
                        tag.append(" · ");
                    }
                }
            }
            if (tag.length() > 0) {
                holder.tvTags.setText(tag.toString());
                holder.tvTags.setVisibility(View.VISIBLE);
            } else {
                holder.tvTags.setText("");
                holder.tvTags.setVisibility(View.GONE);
            }
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipActivityUtil.goGoodsDetail(context, detailsDTO1.getProductId() + "", detailsDTO1.getProductType() + "");
                }
            });
        }

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        ImageView iv1;

        ImageView iv;
        TextView tvNum;
        TextView tvName;
        TextView tvTags;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            iv1 = itemView.findViewById(R.id.iv1);
            iv = itemView.findViewById(R.id.iv);
            tvNum = itemView.findViewById(R.id.tvNum);
            tvName = itemView.findViewById(R.id.tvName);
            tvTags = itemView.findViewById(R.id.tvTags);
        }
    }

}
