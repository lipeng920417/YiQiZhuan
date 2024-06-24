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


public class ShopBottomFlexibleItem extends AbstractFlexibleItem<ShopBottomFlexibleItem.ItemViewHolder> {
    public Context context;
    public List<ProductListBean.Detail> detail;
    public String type;


    public ShopBottomFlexibleItem(Context context, List<ProductListBean.Detail> detail, String type) {
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
        return R.layout.item_home_tab;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (detail != null) {
            if (detail.size() == 1) {
                holder.lly0.setVisibility(View.VISIBLE);
                holder.lly1.setVisibility(View.INVISIBLE);
                holder.lly2.setVisibility(View.INVISIBLE);
                holder.lly3.setVisibility(View.INVISIBLE);
                showView(holder.iv, holder.tvProductName, holder.tvPrice, holder.lly0, detail.get(0));
            } else if (detail.size() == 2) {
                holder.lly0.setVisibility(View.VISIBLE);
                holder.lly1.setVisibility(View.VISIBLE);
                holder.lly2.setVisibility(View.INVISIBLE);
                holder.lly3.setVisibility(View.INVISIBLE);
                showView(holder.iv, holder.tvProductName, holder.tvPrice, holder.lly0, detail.get(0));
                showView(holder.iv1, holder.tvProductName1, holder.tvPrice1, holder.lly1, detail.get(1));
            } else if (detail.size() == 3) {
                holder.lly0.setVisibility(View.VISIBLE);
                holder.lly1.setVisibility(View.VISIBLE);
                holder.lly2.setVisibility(View.VISIBLE);
                holder.lly3.setVisibility(View.INVISIBLE);
                showView(holder.iv, holder.tvProductName, holder.tvPrice, holder.lly0, detail.get(0));
                showView(holder.iv1, holder.tvProductName1, holder.tvPrice1, holder.lly1, detail.get(1));
                showView(holder.iv2, holder.tvProductName2, holder.tvPrice2, holder.lly2, detail.get(2));
            } else if (detail.size() == 4) {
                holder.lly0.setVisibility(View.VISIBLE);
                holder.lly1.setVisibility(View.VISIBLE);
                holder.lly2.setVisibility(View.VISIBLE);
                holder.lly3.setVisibility(View.VISIBLE);
                showView(holder.iv, holder.tvProductName, holder.tvPrice, holder.lly0, detail.get(0));
                showView(holder.iv1, holder.tvProductName1, holder.tvPrice1, holder.lly1, detail.get(1));
                showView(holder.iv2, holder.tvProductName2, holder.tvPrice2, holder.lly2, detail.get(2));
                showView(holder.iv3, holder.tvProductName3, holder.tvPrice3, holder.lly3, detail.get(3));
            }
        }
    }

    private void showView(ImageView iv, TextView tvProductName, TextView tvPrice, LinearLayout lly, ProductListBean.Detail detail1) {
        if (!TextUtils.isEmpty(detail1.getMainImage())) {
            GlideUtil.loadImage(detail1.getMainImage(), iv);
        }
        tvProductName.setText(detail1.getProductName());
        tvPrice.setText("￥"+detail1.getOriginalPrice());
        lly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent broker = new Intent(context, WebActivity.class);
//                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + detail1.getProductId() + "&type=" + type);
//                context.startActivity(broker);
                SkipActivityUtil.goGoodsDetail(context, detail1.getProductId(), type);
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly0;
        ImageView iv;
        TextView tvProductName;
        TextView tvPrice;

        LinearLayout lly1;
        ImageView iv1;
        TextView tvProductName1;
        TextView tvPrice1;

        LinearLayout lly2;
        ImageView iv2;
        TextView tvProductName2;
        TextView tvPrice2;

        LinearLayout lly3;
        ImageView iv3;
        TextView tvProductName3;
        TextView tvPrice3;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly0 = itemView.findViewById(R.id.lly0);
            iv = itemView.findViewById(R.id.iv);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            lly1 = itemView.findViewById(R.id.lly1);
            iv1 = itemView.findViewById(R.id.iv1);
            tvProductName1 = itemView.findViewById(R.id.tvProductName1);
            tvPrice1 = itemView.findViewById(R.id.tvPrice1);

            lly2 = itemView.findViewById(R.id.lly2);
            iv2 = itemView.findViewById(R.id.iv2);
            tvProductName2 = itemView.findViewById(R.id.tvProductName2);
            tvPrice2 = itemView.findViewById(R.id.tvPrice2);

            lly3 = itemView.findViewById(R.id.lly3);
            iv3 = itemView.findViewById(R.id.iv3);
            tvProductName3 = itemView.findViewById(R.id.tvProductName3);
            tvPrice3 = itemView.findViewById(R.id.tvPrice3);
        }

    }

}
