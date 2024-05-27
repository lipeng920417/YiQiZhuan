package com.yiqizhuan.app.ui.home.item;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.YQZApp;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.webview.WebActivity;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

public class HeaderFlexibleItem extends AbstractFlexibleItem<HeaderFlexibleItem.ItemViewHolder> {

    Context context;
    ProductListBean.Detail detail;
    String type;

    public HeaderFlexibleItem(Context context, ProductListBean.Detail detail, String type) {
        this.context = context;
        this.detail = detail;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_header;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if ("3".equals(type)){
            holder.iv.setImageResource(R.mipmap.ic_tab_header_left);
        }else {
            holder.iv.setImageResource(R.mipmap.ic_tab_header_right);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }

    }

}
