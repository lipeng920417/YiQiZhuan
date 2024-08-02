package com.yiqizhuan.app.ui.order.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.OrderListBean;
import com.yiqizhuan.app.util.GlideUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class OrderGoodsView extends LinearLayout {
    private Context context;
    ImageView iv;
    TextView tvName;
    TextView tvShuXing;
    ImageView ivTitle;
    TextView tvOriginalPriceL;
    TextView tvOriginalPrice;
    TextView tvMoney;
    TextView tvDiscount;
    TextView tvJiaHao;
    TextView tvJiFen;
    TextView tvNum;

    public OrderGoodsView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public OrderGoodsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public OrderGoodsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_order_goods, this);
        iv = findViewById(R.id.iv);
        tvName = findViewById(R.id.tvName);
        tvShuXing = findViewById(R.id.tvShuXing);
        ivTitle = findViewById(R.id.ivTitle);
        tvOriginalPriceL = findViewById(R.id.tvOriginalPriceL);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);
        tvMoney = findViewById(R.id.tvMoney);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvJiaHao = findViewById(R.id.tvJiaHao);
        tvJiFen = findViewById(R.id.tvJiFen);
        tvNum = findViewById(R.id.tvNum);
    }

    public void initData(OrderListBean.OrdersDTO.ProductsDTO detailsDTO) {
        if (detailsDTO != null) {
            GlideUtil.loadImage(detailsDTO.getGoodsDto().getImageUrl(), iv, 6);
            tvName.setText(detailsDTO.getGoodsDto().getName());
            tvShuXing.setText(detailsDTO.getGoodsDto().getDescription());
            tvDiscount.setVisibility(View.VISIBLE);
            tvJiaHao.setVisibility(View.VISIBLE);
            tvJiFen.setVisibility(View.VISIBLE);
            if (detailsDTO.getProductType() == 1) {
                ivTitle.setImageResource(R.mipmap.ic_zunxianghui_title);
                tvMoney.setText(getMoney(detailsDTO.getGoodsDto()));
                tvDiscount.setText(getDiscount(detailsDTO.getGoodsDto()));
                tvOriginalPriceL.setVisibility(View.VISIBLE);
                tvOriginalPrice.setVisibility(View.VISIBLE);
            }
            //共享
            else if (detailsDTO.getProductType() == 2) {
                ivTitle.setImageResource(R.mipmap.ic_gongxianghui_title);
                tvMoney.setText(detailsDTO.getGoodsDto().getPrice());
                tvDiscount.setVisibility(View.GONE);
                tvJiaHao.setVisibility(View.GONE);
                tvJiFen.setVisibility(View.GONE);
                tvOriginalPriceL.setVisibility(View.GONE);
                tvOriginalPrice.setVisibility(View.GONE);
            }
            //悦享
            else if (detailsDTO.getProductType() == 3) {
                ivTitle.setImageResource(R.mipmap.ic_yuxianghui_title);
                tvMoney.setText(getMoney(detailsDTO.getGoodsDto()));
                tvDiscount.setText(getDiscount(detailsDTO.getGoodsDto()));
                tvOriginalPriceL.setVisibility(View.VISIBLE);
                tvOriginalPrice.setVisibility(View.VISIBLE);
            }
            //畅享
            else if (detailsDTO.getProductType() == 4) {
                ivTitle.setImageResource(R.mipmap.ic_changxianghui_title);
                tvMoney.setText(getMoney(detailsDTO.getGoodsDto()));
                tvDiscount.setText(getDiscount(detailsDTO.getGoodsDto()));
                tvOriginalPriceL.setVisibility(View.VISIBLE);
                tvOriginalPrice.setVisibility(View.VISIBLE);
            }
            tvOriginalPrice.setText("￥" + detailsDTO.getGoodsDto().getPrice());
            tvOriginalPrice.getPaint().setStrikeThruText(true);
            tvNum.setText("x"+detailsDTO.getProductNum());
        }
    }

    private SpannableString getMoney(OrderListBean.OrdersDTO.ProductsDTO.GoodsDtoDTO goodsVODTO) {
        String sellPrice = subtractAndRound(Double.parseDouble(goodsVODTO.getPrice()), Double.parseDouble(goodsVODTO.getDiscount()));
        int sellPriceNum = getDecimalPlaces(sellPrice);
        SpannableString spannableString1 = new SpannableString(sellPrice);
        RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(0.7f);
        spannableString1.setSpan(relativeSizeSpan1, sellPrice.length() - sellPriceNum, sellPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString1;
    }

    private SpannableString getDiscount(OrderListBean.OrdersDTO.ProductsDTO.GoodsDtoDTO goodsVODTO) {
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

}
