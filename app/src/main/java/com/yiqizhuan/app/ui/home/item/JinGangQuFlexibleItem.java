package com.yiqizhuan.app.ui.home.item;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.integral.IntegralCenterActivity;
import com.yiqizhuan.app.views.dialog.DialogUtil;
import com.yiqizhuan.app.webview.WebActivity;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class JinGangQuFlexibleItem extends AbstractFlexibleItem<JinGangQuFlexibleItem.ItemViewHolder> {
    public Context context;
    public int id;
    public String name;
    public String categoryId;

    public JinGangQuFlexibleItem(Context context, int id, String name, String categoryId) {
        this.context = context;
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_jin_gang_qu;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        holder.iv.setImageResource(id);
        holder.tvName.setText(name);
        if ("15".equals(categoryId)) {
            holder.ivLiSheng.setVisibility(View.VISIBLE);
        } else {
            holder.ivLiSheng.setVisibility(View.GONE);
        }
        holder.lly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("-1".equals(categoryId)) {
//                    DialogUtil.build1BtnDialog(context, "敬请期待", "我知道了", true, new DialogUtil.DialogListener1Btn() {
//                        @Override
//                        public void onPositiveClick(View v) {
//
//                        }
//                    }).show();
                    context.startActivity(new Intent(context, IntegralCenterActivity.class));
                } else {
                    Intent broker = new Intent(context, WebActivity.class);
                    broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_CATEGORY_LIST + "?categoryId=" + categoryId);
                    context.startActivity(broker);
                }
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout lly;
        ImageView iv;
        TextView tvName;
        ImageView ivLiSheng;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            iv = itemView.findViewById(R.id.iv);
            tvName = itemView.findViewById(R.id.tvName);
            ivLiSheng = itemView.findViewById(R.id.ivLiSheng);
        }
    }

}
