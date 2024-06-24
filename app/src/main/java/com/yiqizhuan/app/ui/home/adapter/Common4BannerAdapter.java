package com.yiqizhuan.app.ui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-04-24 11:15 PM
 */
public class Common4BannerAdapter extends BannerAdapter<List<ProductListBean.Detail>, Common4BannerAdapter.ItemViewHolder> {
    Context context;

    public Common4BannerAdapter(Context context, List<List<ProductListBean.Detail>> detail) {
        super(detail);
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_banner4, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindView(ItemViewHolder holder, List<ProductListBean.Detail> data, int position, int size) {
        if (data != null && data.size() > 3) {
            showView(holder.iv, holder.tvProductName, holder.tvPrice, holder.lly, data.get(0));
            showView(holder.iv1, holder.tvProductName1, holder.tvPrice1, holder.lly1, data.get(1));
            showView(holder.iv2, holder.tvProductName2, holder.tvPrice2, holder.lly2, data.get(2));
            showView(holder.iv3, holder.tvProductName3, holder.tvPrice3, holder.lly3, data.get(3));
        }
    }

    private void showView(ImageView iv, TextView tvProductName, TextView tvPrice, LinearLayout lly, ProductListBean.Detail detail) {
        if (detail != null) {
            if (!TextUtils.isEmpty(detail.getMainImage())) {
                GlideUtil.loadImage(detail.getMainImage(), iv);
            }
            tvProductName.setText(detail.getProductName());
            tvPrice.setText("￥" + detail.getOriginalPrice());
            lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent broker = new Intent(context, WebActivity.class);
//                    broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + detail.getProductId() + "&type=" + detail.getProductType());
//                    context.startActivity(broker);
                    SkipActivityUtil.goGoodsDetail(context, detail.getProductId()  , detail.getProductType());
                }
            });
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
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

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
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
