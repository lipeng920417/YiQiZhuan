package com.yiqizhuan.app.ui.detail.item;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.YQZApp;
import com.yiqizhuan.app.bean.GoodsDetailBean;
import com.yiqizhuan.app.views.flowlayout.FlowLayout;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

public class AttributeFlexibleItem extends AbstractFlexibleItem<AttributeFlexibleItem.ItemViewHolder> {
    private GoodsDetailBean.Attributes attributes;
    Context context;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AttributeFlexibleItem(Context context, GoodsDetailBean.Attributes attributes) {
        this.context = context;
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_attribute;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        holder.tv.setText(attributes.getAttrName());
        if (holder.flowLayout != null) {
            holder.flowLayout.removeAllViews();
        }
        for (GoodsDetailBean.AttrDescription attrDescription : attributes.getAttrDescription()) {
            final View itemLayout = View.inflate(YQZApp.getContext(), R.layout.flow_item_attr, null);
            TextView tv = itemLayout.findViewById(R.id.tv);
            tv.setText(attrDescription.getAttrValue());
            if (attrDescription.getSelect() == 2) {
                tv.setTextColor(context.getResources().getColor(R.color.white));
                tv.setBackground(context.getResources().getDrawable(R.drawable.background_conner_bdbdbd_12dp));
            } else if (attrDescription.getSelect() == 1) {
                tv.setTextColor(context.getResources().getColor(R.color.color_fa2c19));
                tv.setBackground(context.getResources().getDrawable(R.drawable.background_conner12_fee9e8_st_fa2c19));
            } else if (attrDescription.getSelect() == 0) {
                tv.setTextColor(context.getResources().getColor(R.color.color_333333));
                tv.setBackground(context.getResources().getDrawable(R.drawable.shape_stroke_333333_conner_12));
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (attrDescription.getSelect() == 2) {

                    } else if (attrDescription.getSelect() == 1) {
                        if (onClickListener != null) {
                            onClickListener.onPositiveClick(false, attrDescription, position);
                        }
                    } else if (attrDescription.getSelect() == 0) {
                        if (onClickListener != null) {
                            onClickListener.onPositiveClick(true, attrDescription, position);
                        }
                    }
                }
            });
            holder.flowLayout.addView(itemLayout);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        FlowLayout flowLayout;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            flowLayout = itemView.findViewById(R.id.flowLayout);
        }
    }

    public interface OnClickListener {
        void onPositiveClick(boolean b, GoodsDetailBean.AttrDescription attrDescription, int pos);
    }

}
