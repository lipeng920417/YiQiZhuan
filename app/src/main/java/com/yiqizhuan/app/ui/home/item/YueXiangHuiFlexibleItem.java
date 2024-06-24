package com.yiqizhuan.app.ui.home.item;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.SkipActivityUtil;
import com.yiqizhuan.app.webview.WebActivity;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class YueXiangHuiFlexibleItem extends AbstractFlexibleItem<YueXiangHuiFlexibleItem.ItemViewHolder> {
    public Context context;
    ProductListBean.Detail detail;

    public YueXiangHuiFlexibleItem(Context context, ProductListBean.Detail detail) {
        this.context = context;
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_yue_xiang_hui;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (detail != null) {
            if (!TextUtils.isEmpty(detail.getMainImage())) {
                GlideUtil.loadImage(detail.getMainImage(), holder.iv);
            }
            holder.tvName.setText("限时￥" + detail.getOriginalPrice());
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent broker = new Intent(context, WebActivity.class);
//                    broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + detail.getProductId() + "&type=3");
//                    context.startActivity(broker);
                    SkipActivityUtil.goGoodsDetail(context, detail.getProductId() , "3");
                }
            });
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        ImageView iv;
        TextView tvName;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            iv = itemView.findViewById(R.id.iv);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

}
