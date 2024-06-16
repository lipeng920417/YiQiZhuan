package com.yiqizhuan.app.ui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyliao.liveeventbus.LiveEventBus;
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
public class BannerZhuanQuAdapter extends BaseBannerAdapter<Integer> {
    Context context;
    String[] stringArray = new String[] {"3C数码", "珠宝首饰", "顶级箱包", "手表奢饰","大牌包包", "养生保健", "美白护肤", "时尚家居"};

    public BannerZhuanQuAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected void bindData(BaseViewHolder<Integer> holder, Integer data, int position, int pageSize) {
        LinearLayout lly = holder.findViewById(R.id.lly);
        ImageView iv = holder.findViewById(R.id.iv);
        TextView tv = holder.findViewById(R.id.tv);
        iv.setImageResource(data);
        tv.setText(stringArray[position]);
        lly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveEventBus.get("goZunXiangHui").post("");
            }
        });
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_banner_zhuanqu;
    }

}
