package com.yiqizhuan.app.ui.order.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.OrderListBean;


public class OrderWuLiuView extends LinearLayout {
    private Context context;
    LinearLayout lly;
    TextView tvStatus;
    TextView tvAcceptStation;
    TextView tvQueRenShouHuo;
    OrderWuLiuListenerSure orderWuLiuListenerSure;


    public OrderWuLiuView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public OrderWuLiuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public OrderWuLiuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_order_wuliu, this);
        lly = findViewById(R.id.lly);
        tvStatus = findViewById(R.id.tvStatus);
        tvAcceptStation = findViewById(R.id.tvAcceptStation);
        tvQueRenShouHuo = findViewById(R.id.tvQueRenShouHuo);
        lly.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderWuLiuListenerSure != null) {
                    orderWuLiuListenerSure.onItem();
                }
            }
        });
        tvQueRenShouHuo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderWuLiuListenerSure != null) {
                    orderWuLiuListenerSure.onQueRenShouHuo();
                }
            }
        });

    }

    public void initData(OrderListBean.OrdersDTO.ShippersDTO shippersDTO, OrderWuLiuListenerSure orderWuLiuListenerSure) {
        this.orderWuLiuListenerSure = orderWuLiuListenerSure;
        if (shippersDTO != null) {
            tvStatus.setText(shippersDTO.getStatus());
            if (shippersDTO.getTrace() != null && shippersDTO.getTrace().getTraces() != null && shippersDTO.getTrace().getTraces().size() > 0) {
                tvAcceptStation.setText(shippersDTO.getTrace().getTraces().get(0).getAcceptStation());
            }
            if (shippersDTO.isShowTakeOver()) {
                tvQueRenShouHuo.setVisibility(VISIBLE);
            } else {
                tvQueRenShouHuo.setVisibility(GONE);
            }
        }
    }

    public interface OrderWuLiuListenerSure {
        void onItem();

        void onQueRenShouHuo();

    }


}
