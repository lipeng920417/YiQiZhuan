package com.yiqizhuan.app.ui.integral.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.GetHistoryExchange;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class ExchangeProjectFlexibleItem extends AbstractFlexibleItem<ExchangeProjectFlexibleItem.ItemViewHolder> {

    GetHistoryExchange.PointsInfo pointsInfo;
    int type;

    public ExchangeProjectFlexibleItem(Context context, GetHistoryExchange.PointsInfo pointsInfo, int type) {
        this.pointsInfo = pointsInfo;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_exchange_project;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (type == 0) {
            holder.tvTitle.setText("第N期兑换积分");
            holder.ivYiDuiHuan.setVisibility(View.GONE);
            holder.llyJiFen.setVisibility(View.GONE);
            holder.tvDaiShiFang.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setText("第一期兑换积分");
            holder.ivYiDuiHuan.setVisibility(View.VISIBLE);
            holder.llyJiFen.setVisibility(View.VISIBLE);
            holder.tvDaiShiFang.setVisibility(View.GONE);
            if (pointsInfo != null) {
                if (pointsInfo.getState() == 1) {
                    if (pointsInfo.getPointContractAuditVOs() != null && pointsInfo.getPointContractAuditVOs().size() > 0) {
                        holder.tvJiFen.setText(pointsInfo.getPointContractAuditVOs().get(0).getCyclePoints());
                        holder.tvTime.setText(pointsInfo.getPointContractAuditVOs().get(0).getExchangeDate());
                    }
                    holder.ivYiDuiHuan.setImageResource(R.mipmap.ic_yiduihuan);
                } else if (pointsInfo.getState() == 0) {
                    holder.ivYiDuiHuan.setImageResource(R.mipmap.ic_shenhezhong);
                    holder.tvJiFen.setText(pointsInfo.getContractPoints());
                    holder.tvTime.setText("——");
                } else if (pointsInfo.getState() == 2) {
                    holder.ivYiDuiHuan.setImageResource(R.mipmap.ic_weitongguo);
                    holder.tvJiFen.setText(pointsInfo.getContractPoints());
                    holder.tvTime.setText("——");
                }
            }
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivYiDuiHuan;
        private LinearLayout llyJiFen;
        private TextView tvJiFen;
        private TextView tvTime;
        private TextView tvDaiShiFang;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivYiDuiHuan = itemView.findViewById(R.id.ivYiDuiHuan);
            llyJiFen = itemView.findViewById(R.id.llyJiFen);
            tvJiFen = itemView.findViewById(R.id.tvJiFen);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDaiShiFang = itemView.findViewById(R.id.tvDaiShiFang);
        }

    }

}
