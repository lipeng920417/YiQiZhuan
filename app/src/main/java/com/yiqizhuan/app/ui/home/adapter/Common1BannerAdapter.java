package com.yiqizhuan.app.ui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
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
import com.yiqizhuan.app.webview.WebActivity;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-04-24 11:15 PM
 */
public class Common1BannerAdapter extends BannerAdapter<ProductListBean.Detail, Common1BannerAdapter.ItemViewHolder> {
    Context context;
    String type;

    public Common1BannerAdapter(Context context, List<ProductListBean.Detail> detail, String type) {
        super(detail);
        this.context = context;
        this.type = type;
    }

    @Override
    public ItemViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_banner1, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindView(ItemViewHolder holder, ProductListBean.Detail data, int position, int size) {
        if (data != null) {
            if (!TextUtils.isEmpty(data.getMainImage())) {
                GlideUtil.loadImage(data.getMainImage(), holder.iv);
            }
            holder.tv1.setText("超值价￥" + data.getOriginalPrice());
            holder.tv2.setText("￥" + data.getOriginalPrice());
            TextPaint paint = holder.tv2.getPaint();
            paint.setStrikeThruText(true);
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent broker = new Intent(context, WebActivity.class);
                    broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + data.getProductId() + "&type=" + type);
                    context.startActivity(broker);
                }
            });
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        ImageView iv;
        TextView tv1;
        TextView tv2;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            iv = itemView.findViewById(R.id.iv);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
        }
    }

}
