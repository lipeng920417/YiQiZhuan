package com.yiqizhuan.app.ui.remit.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.webview.WebActivity;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;


/**
 * @author LiPeng
 * @create 2024-06-15 2:35 PM
 */
public class BannerAdapter extends BaseBannerAdapter<ProductListBean.Detail> {
    Context context;
    String[] stringArray = new String[] {"高奢优品", "奢华逸品", "豪华佳品", "尊享名品","顶级瑰品", "高端精品", "高奢臻品", "名贵珍品"};

    public BannerAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected void bindData(BaseViewHolder<ProductListBean.Detail> holder, ProductListBean.Detail data, int position, int pageSize) {
        LinearLayout lly = holder.findViewById(R.id.lly);
        ImageView iv = holder.findViewById(R.id.iv);
        TextView tvPrice = holder.findViewById(R.id.tvPrice);
        TextView tvName = holder.findViewById(R.id.tvName);

        if (data != null) {
            if (!TextUtils.isEmpty(data.getMainImage())) {
                GlideUtil.loadImage(data.getMainImage(), iv);
            }
            int  price = (int) Math.floor(Double.valueOf(data.getOriginalPrice()) * 0.18);
            tvPrice.setText("直降" + price + "元");
            if (position < 8) {
                tvName.setText(stringArray[position]);
            }
            lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent broker = new Intent(context, WebActivity.class);
                    broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + data.getProductId() + "&type=1");
                    context.startActivity(broker);
                }
            });
        }

    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_banner_remit;
    }

}
