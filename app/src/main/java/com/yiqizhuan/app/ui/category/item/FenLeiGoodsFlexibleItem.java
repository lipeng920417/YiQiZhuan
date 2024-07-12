package com.yiqizhuan.app.ui.category.item;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.SkipActivityUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class FenLeiGoodsFlexibleItem extends AbstractFlexibleItem<FenLeiGoodsFlexibleItem.ItemViewHolder> {
    private Context context;
    private ProductListBean.Detail detailsDTO;

    public FenLeiGoodsFlexibleItem(Context context, ProductListBean.Detail detailsDTO) {
        this.context = context;
        this.detailsDTO = detailsDTO;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_fenlei_goods;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (detailsDTO != null) {
            GlideUtil.loadImage(detailsDTO.getMainImage(), holder.iv);
//            holder.tvName.setText(detailsDTO.getProductName());
            holder.ivTitle.post(() -> {
                int imageViewWidth = holder.ivTitle.getWidth();
                calculateSpaces(imageViewWidth, holder.tvName, detailsDTO);
            });
            StringBuilder tag = new StringBuilder();
            if (detailsDTO.getTags() != null) {
                for (int i = 0; i < detailsDTO.getTags().size(); i++) {
                    tag.append(detailsDTO.getTags().get(i));
                    if (i < detailsDTO.getTags().size() - 1) {
                        tag.append(" | ");
                    }
                }
            }
            if (tag.length() > 0) {
                SpannableString spannableString = new SpannableString(tag.toString());
                // 创建ForegroundColorSpan，并设置颜色
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.color_ff1703));
                spannableString.setSpan(colorSpan, 0, detailsDTO.getTags().get(0).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvShuXing.setText(spannableString);
            } else {
                holder.tvShuXing.setText("");
            }
            if (TextUtils.equals(detailsDTO.getQuota(), "1")) {
                holder.tvMeiRiXianGou.setVisibility(View.VISIBLE);
            } else {
                holder.tvMeiRiXianGou.setVisibility(View.GONE);
            }
            holder.tvMoney.setText(getMoney(detailsDTO));
            holder.tvOriginalPrice.setText("￥" + detailsDTO.getOriginalPrice());
            if (Double.parseDouble(detailsDTO.getSellPrice()) < Double.parseDouble(detailsDTO.getOriginalPrice())) {
                holder.tvOriginalPrice.getPaint().setStrikeThruText(true);
            } else {
                holder.tvOriginalPrice.getPaint().setStrikeThruText(false);
            }

//            if (detailsDTO.getState() == 0) {
//                if (detailsDTO.getGoodsVO().getDeleted() == 1 || detailsDTO.getProductVO().getDeleted() == 1) {
//                    holder.rlyYiXiaJia.setVisibility(View.VISIBLE);
//                    holder.tvYiXiaJia.setText("已下架");
//                } else if (detailsDTO.getGoodsVO().getStatus() == 0) {
//                    holder.rlyYiXiaJia.setVisibility(View.VISIBLE);
//                    holder.tvYiXiaJia.setText("待开售");
//                } else if (detailsDTO.getGoodsVO().getStock() == 0) {
//                    holder.rlyYiXiaJia.setVisibility(View.VISIBLE);
//                    holder.tvYiXiaJia.setText("已售罄");
//                }
//            }
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipActivityUtil.goGoodsDetail(context, detailsDTO.getProductId() + "", detailsDTO.getProductType() + "");
                }
            });
        }
    }

    private SpannableString getMoney(ProductListBean.Detail goodsVODTO) {
        String sellPrice = goodsVODTO.getSellPrice();
        int sellPriceNum = getDecimalPlaces(sellPrice);
        SpannableString spannableString1 = new SpannableString(sellPrice);
        RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(0.7f);
        spannableString1.setSpan(relativeSizeSpan1, sellPrice.length() - sellPriceNum, sellPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString1;
    }

    private SpannableString getDiscount(ProductListBean.Detail goodsVODTO) {
        String sellPrice = goodsVODTO.getDiscount();
        int sellPriceNum = getDecimalPlaces(sellPrice);
        SpannableString spannableString1 = new SpannableString(sellPrice);
        RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(0.7f);
        spannableString1.setSpan(relativeSizeSpan1, sellPrice.length() - sellPriceNum, sellPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString1;
    }

    private String subtractAndRound(double num1, double num2) {
        BigDecimal bigDecimal1 = BigDecimal.valueOf(num1);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(num2);
        BigDecimal result = bigDecimal1.subtract(bigDecimal2);

        // 使用 setScale 方法来设置小数位数和舍入模式
        result = result.setScale(2, RoundingMode.HALF_UP);

        // 格式化结果保留两位小数
        return String.format("%.2f", result.doubleValue());
    }

    private int getDecimalPlaces(String number) {
        if (number.contains(".")) {
            int indexOfDecimal = number.indexOf(".");
            return number.length() - indexOfDecimal - 1;
        } else {
            return 0; // 如果字符串中不包含小数点，则小数位数为0
        }
    }

    private void calculateSpaces(int imageViewWidth, TextView tvName, ProductListBean.Detail detailsDTO) {
        Paint paint = new Paint();
        paint.setTextSize(tvName.getTextSize()); // 设置与TextView相同的字体大小
        String space = " "; // 一个空格
        // 测量空格的宽度
        float spaceWidth = paint.measureText(space);
        // 计算ImageView的宽度等于多少个空格
        float numberOfSpaces = imageViewWidth / spaceWidth;
        int ceilingValue = (int) Math.ceil(numberOfSpaces) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ceilingValue; i++) {
            stringBuilder.append(space);
        }
        stringBuilder.append(detailsDTO.getProductName());
        tvName.setText(stringBuilder.toString());
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        ImageView iv;
        RelativeLayout rlyYiXiaJia;
        TextView tvYiXiaJia;
        TextView tvName;
        TextView tvShuXing;
        TextView tvMeiRiXianGou;
        TextView ivTitle;
        TextView tvOriginalPrice;
        ImageView ivJiaHao;
        ImageView ivJianHao;
        TextView tvMoney;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            iv = itemView.findViewById(R.id.iv);
            rlyYiXiaJia = itemView.findViewById(R.id.rlyYiXiaJia);
            tvYiXiaJia = itemView.findViewById(R.id.tvYiXiaJia);
            tvName = itemView.findViewById(R.id.tvName);
            tvShuXing = itemView.findViewById(R.id.tvShuXing);
            tvMeiRiXianGou = itemView.findViewById(R.id.tvMeiRiXianGou);
            ivTitle = itemView.findViewById(R.id.ivTitle);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            ivJiaHao = itemView.findViewById(R.id.ivJiaHao);
            ivJianHao = itemView.findViewById(R.id.ivJianHao);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }
    }

}
