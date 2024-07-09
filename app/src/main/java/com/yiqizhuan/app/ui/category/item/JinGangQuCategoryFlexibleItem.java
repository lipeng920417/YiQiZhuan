package com.yiqizhuan.app.ui.category.item;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.CategoryDefaultBean;
import com.yiqizhuan.app.bean.GetDiscountCategoryBean;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.integral.IntegralCenterActivity;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.webview.WebActivity;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class JinGangQuCategoryFlexibleItem extends AbstractFlexibleItem<JinGangQuCategoryFlexibleItem.ItemViewHolder> {
    public Context context;
    public CategoryDefaultBean categoryDefaultBean;
    public GetDiscountCategoryBean discountCategoryBean;

    public JinGangQuCategoryFlexibleItem(Context context, CategoryDefaultBean categoryDefaultBean, GetDiscountCategoryBean discountCategoryBean) {
        this.context = context;
        this.categoryDefaultBean = categoryDefaultBean;
        this.discountCategoryBean = discountCategoryBean;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_jin_gang_qu_category;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (categoryDefaultBean != null) {
            if ("-1".equals(categoryDefaultBean.getId())) {
                holder.iv.setImageResource(R.mipmap.ic_jifenduihuan);
            } else {
                GlideUtil.loadImage(categoryDefaultBean.getImageUrl(), holder.iv);
            }
            holder.tvName.setText(categoryDefaultBean.getName());
            if (discountCategoryBean != null && discountCategoryBean.getData() != null) {
                if (TextUtils.equals(categoryDefaultBean.getId(), discountCategoryBean.getData().getCategoryId())) {
                    holder.ivLiSheng.setVisibility(View.VISIBLE);
                    GlideUtil.loadImage(discountCategoryBean.getData().getDiscountUrl(), holder.ivLiSheng);
                } else {
                    holder.ivLiSheng.setVisibility(View.GONE);
                }
            } else {
                holder.ivLiSheng.setVisibility(View.GONE);
            }
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("-1".equals(categoryDefaultBean.getId())) {
                        if (TextUtils.isEmpty(MMKVHelper.getString("token", ""))) {
                            LiveEventBus.get("goToLogin").post("");
                        } else {
                            context.startActivity(new Intent(context, IntegralCenterActivity.class));
                        }
                    } else {
                        Intent broker = new Intent(context, WebActivity.class);
                        broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_CATEGORY_LIST + "?categoryId=" + categoryDefaultBean.getId());
                        context.startActivity(broker);
                    }
                }
            });
        }
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
