package com.yiqizhuan.app.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.R;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-06-16 8:26 PM
 */
public class AutoPollAdapter extends RecyclerView.Adapter<AutoPollAdapter.BaseViewHolder> {
    private final List<Integer> mData;
    String[] stringArray = new String[]{"3C数码", "珠宝首饰", "顶级箱包", "手表奢饰", "大牌包包", "养生保健", "美白护肤", "时尚家居"};


    public AutoPollAdapter(List<Integer> list) {
        this.mData = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_zhuanqu1, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.iv.setImageResource(mData.get(position % mData.size()));
        holder.tv.setText(stringArray[position % mData.size()]);
        holder.lly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveEventBus.get("goZunXiangHui").post("");
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        ImageView iv;
        TextView tv;

        public BaseViewHolder(View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}