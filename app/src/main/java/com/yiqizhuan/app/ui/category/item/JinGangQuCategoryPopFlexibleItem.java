package com.yiqizhuan.app.ui.category.item;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.CategoryDefaultBean;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.webview.WebActivity;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class JinGangQuCategoryPopFlexibleItem extends AbstractFlexibleItem<JinGangQuCategoryPopFlexibleItem.ItemViewHolder> {
    public Context context;
    public CategoryDefaultBean categoryDefaultBean;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public JinGangQuCategoryPopFlexibleItem(Context context, CategoryDefaultBean categoryDefaultBean) {
        this.context = context;
        this.categoryDefaultBean = categoryDefaultBean;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_jin_gang_qu_category_pop;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (categoryDefaultBean != null) {
            GlideUtil.loadImage(categoryDefaultBean.getTopIconUrl(), holder.iv, 50);
            holder.tvName.setText(categoryDefaultBean.getName());
            if (categoryDefaultBean.isSelect()) {
                holder.rlyIv.setBackground(context.getResources().getDrawable(R.drawable.shape_stroke_ff1703));
                holder.tvName.setTextColor(context.getResources().getColor(R.color.white));
                holder.tvName.setBackground(context.getResources().getDrawable(R.drawable.background_conner_ff1804_8dp));
            } else {
                holder.rlyIv.setBackground(null);
                holder.tvName.setTextColor(context.getResources().getColor(R.color.color_191919));
                holder.tvName.setBackground(null);
            }
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null) {
                        onClickListener.onPositiveClick(categoryDefaultBean,position);
                    }
                }
            });
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout lly;
        RelativeLayout rlyIv;
        ImageView iv;
        TextView tvName;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            rlyIv = itemView.findViewById(R.id.rlyIv);
            iv = itemView.findViewById(R.id.iv);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public interface OnClickListener {
        void onPositiveClick(CategoryDefaultBean categoryDefaultBean,int position);
    }
}
