package com.yiqizhuan.app.ui.integral.item;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.HistoryDetailBean;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * 购物type 1 减号，积分兑换type 3
 */
public class IntegralDetailFlexibleItem extends AbstractFlexibleItem<IntegralDetailFlexibleItem.ItemViewHolder> {

    HistoryDetailBean historyDetailBean;
    Context context;

    public IntegralDetailFlexibleItem(Context context, HistoryDetailBean historyDetailBean) {
        this.context = context;
        this.historyDetailBean = historyDetailBean;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_integral_detail;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (historyDetailBean != null) {
            holder.tvTitle.setText(historyDetailBean.getTypeName());
            holder.tvTime.setText(historyDetailBean.getModifyTime());
            if (historyDetailBean.getType() == 1) {
                if (!TextUtils.isEmpty(historyDetailBean.getTotalQuotaAction())) {
                    holder.tvMoney.setText("-￥" + historyDetailBean.getTotalQuotaAction());
                } else {
                    holder.tvMoney.setText("-￥0.00" );
                }
                holder.iv.setImageResource(R.mipmap.ic_zuixianghuigouwu);
            } else {
                if (!TextUtils.isEmpty(historyDetailBean.getTotalQuotaAction())) {
                    holder.tvMoney.setText("+￥" + historyDetailBean.getTotalQuotaAction());
                } else {
                    holder.tvMoney.setText("+￥0.00" );
                }
                holder.iv.setImageResource(R.mipmap.ic_jifenduifu);
            }
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvMoney;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }

    }

}
