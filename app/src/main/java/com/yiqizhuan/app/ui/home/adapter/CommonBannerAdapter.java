package com.yiqizhuan.app.ui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.YQZApp;
import com.yiqizhuan.app.bean.BannerBean;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.webview.WebActivity;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-04-24 11:15 PM
 */
public class CommonBannerAdapter extends BannerAdapter<BannerBean, CommonBannerAdapter.ItemViewHolder> {
    Context context;

    public CommonBannerAdapter(Context context, List<BannerBean> datas) {
        super(datas);
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_banner, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindView(ItemViewHolder holder, BannerBean data, int position, int size) {
        GlideUtil.loadImage(data.getMainPhotoUrl(), holder.iv1, 6);
        GlideUtil.loadImage(data.getMainPhotoUrl1(), holder.iv2, 6);
        holder.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broker = new Intent(YQZApp.getContext(), WebActivity.class);
                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + data.getProductId() + "&type=2");
                context.startActivity(broker);
            }
        });
        holder.iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broker = new Intent(YQZApp.getContext(), WebActivity.class);
                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + data.getProductId1() + "&type=2");
                context.startActivity(broker);
            }
        });
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv1;
        ImageView iv2;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv1 = itemView.findViewById(R.id.iv1);
            iv2 = itemView.findViewById(R.id.iv2);
        }
    }

}
