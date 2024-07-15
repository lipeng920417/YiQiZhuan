package com.yiqizhuan.app.ui.category.item;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.CategoryDefaultBean;
import com.yiqizhuan.app.util.GlideUtil;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class FenLeiFlexibleItem extends AbstractFlexibleItem<FenLeiFlexibleItem.ItemViewHolder> {
    public Context context;
    private CategoryDefaultBean categoryDefaultBean;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public FenLeiFlexibleItem(Context context, CategoryDefaultBean categoryDefaultBean) {
        this.context = context;
        this.categoryDefaultBean = categoryDefaultBean;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_fen_lei;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        holder.tvName.setText(categoryDefaultBean.getName());
        if (categoryDefaultBean.isSelect()) {
            holder.view.setVisibility(View.VISIBLE);
            holder.tvName.setTextColor(context.getResources().getColor(R.color.color_ff1703));
            holder.lly.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.view.setVisibility(View.INVISIBLE);
            holder.tvName.setTextColor(context.getResources().getColor(R.color.color_191919));
            holder.lly.setBackgroundColor(context.getResources().getColor(R.color.color_f5f6fa));
        }
        if (TextUtils.equals(categoryDefaultBean.getId(), "-1")) {
            holder.tvBiaoQian.setVisibility(View.GONE);
        } else {
            holder.tvBiaoQian.setVisibility(View.GONE);
        }
        holder.lly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onPositiveClick(position, categoryDefaultBean);
                }
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        View view;
        TextView tvBiaoQian;
        TextView tvName;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            view = itemView.findViewById(R.id.view);
            tvBiaoQian = itemView.findViewById(R.id.tvBiaoQian);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public interface OnClickListener {
        void onPositiveClick(int position, CategoryDefaultBean categoryDefaultBean);
    }
}
