package com.yiqizhuan.app.ui.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.views.SquareImageView;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-04-24 11:15 PM
 */
public class DetailBannerAdapter extends BannerAdapter<String, DetailBannerAdapter.ItemViewHolder> {
    Context context;

    public DetailBannerAdapter(Context context, List<String> datas) {
        super(datas);
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_detail_banner, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindView(ItemViewHolder holder, String data, int position, int size) {
        GlideUtil.loadImageBanner(data, holder.iv);

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        SquareImageView iv;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
