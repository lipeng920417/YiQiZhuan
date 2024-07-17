package com.yiqizhuan.app.ui.home.item;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.detail.GoodsDetailActivity;
import com.yiqizhuan.app.util.BigDecimalUtil;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.SkipActivityUtil;
import com.yiqizhuan.app.webview.WebActivity;

import java.util.List;
import java.util.Random;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class JinRiFlexibleItem extends AbstractFlexibleItem<JinRiFlexibleItem.ItemViewHolder> {
    public Context context;
    ProductListBean.Detail detail;

    public JinRiFlexibleItem(Context context, ProductListBean.Detail detail) {
        this.context = context;
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_jin_ri;
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
            holder.tvName.setText(detail.getProductName());
            int num = new Random().nextInt(3) + 2;
            if (num >= 4) {
                holder.rlyHot1.setVisibility(View.GONE);
                holder.rlyHot2.setVisibility(View.GONE);
                holder.rlyHot3.setVisibility(View.VISIBLE);
            } else if (num == 3) {
                holder.rlyHot1.setVisibility(View.GONE);
                holder.rlyHot2.setVisibility(View.VISIBLE);
                holder.rlyHot3.setVisibility(View.GONE);
            } else {
                holder.rlyHot1.setVisibility(View.VISIBLE);
                holder.rlyHot2.setVisibility(View.GONE);
                holder.rlyHot3.setVisibility(View.GONE);
            }
            holder.tvLabel.setText(num + "000+" + "人正在抢购");
            holder.tvMoney.setText(detail.getOriginalPrice());
            holder.tvZhiJiang.setText(BigDecimalUtil.round(Double.valueOf(detail.getOriginalPrice()) * 0.13, 1) + "");
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent broker = new Intent(context, WebActivity.class);
//                    broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + detail.getProductId() + "&type=" + detail.getProductType());
//                    context.startActivity(broker);
                    SkipActivityUtil.goGoodsDetail(context, detail.getProductId(), detail.getProductType());
                }
            });
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        ImageView iv;
        TextView tvName;
        RelativeLayout rlyHot1;
        RelativeLayout rlyHot2;
        RelativeLayout rlyHot3;
        TextView tvLabel;
        TextView tvMoney;
        TextView tvZhiJiang;


        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            iv = itemView.findViewById(R.id.iv);
            tvName = itemView.findViewById(R.id.tvName);
            rlyHot1 = itemView.findViewById(R.id.rlyHot1);
            rlyHot2 = itemView.findViewById(R.id.rlyHot2);
            rlyHot3 = itemView.findViewById(R.id.rlyHot3);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            tvZhiJiang = itemView.findViewById(R.id.tvZhiJiang);
        }
    }

}
