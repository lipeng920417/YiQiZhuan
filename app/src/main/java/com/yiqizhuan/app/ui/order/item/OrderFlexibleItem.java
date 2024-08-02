package com.yiqizhuan.app.ui.order.item;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.OrderListBean;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.order.view.OrderGoodsView;
import com.yiqizhuan.app.ui.order.view.OrderWuLiuView;
import com.yiqizhuan.app.webview.WebActivity;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class OrderFlexibleItem extends AbstractFlexibleItem<OrderFlexibleItem.ItemViewHolder> {
    private Context context;
    private OrderListBean.OrdersDTO ordersDTO;
    private OrderListenerSure orderListenerSure;
    CountDownTimer countDownTimer;


    public void setOrderListenerSure(OrderListenerSure orderListenerSure) {
        this.orderListenerSure = orderListenerSure;
    }

    public OrderFlexibleItem(Context context, OrderListBean.OrdersDTO ordersDTO) {
        this.context = context;
        this.ordersDTO = ordersDTO;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_order;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (ordersDTO != null) {
            holder.tvOrderNumber.setText(ordersDTO.getOrderNumber());
            holder.tvOrderStatus.setText(ordersDTO.getOrderStatus());
            holder.tvTotalUseCoupon.setText(ordersDTO.getConfirm().getTotalUseCoupon());
            holder.tvTotalPrice.setText(ordersDTO.getConfirm().getTotalPrice());
            holder.llyContent.removeAllViews();
            if (ordersDTO.getProducts() != null && ordersDTO.getProducts().size() > 0) {
                for (OrderListBean.OrdersDTO.ProductsDTO productsDTO : ordersDTO.getProducts()) {
                    OrderGoodsView orderGoodsView = new OrderGoodsView(context);
                    orderGoodsView.initData(productsDTO);
                    holder.llyContent.addView(orderGoodsView);
                }
            }
            if (ordersDTO.getShippers() != null && ordersDTO.getShippers().size() > 0) {
                for (int i = 0; i < ordersDTO.getShippers().size(); i++) {
                    int j = i;
                    if (ordersDTO.getShippers().get(i) != null && ordersDTO.getShippers().get(i).getGoods() != null && ordersDTO.getShippers().get(i).getGoods().size() > 0) {
                        for (OrderListBean.OrdersDTO.ProductsDTO productsDTO : ordersDTO.getShippers().get(i).getGoods()) {
                            OrderGoodsView orderGoodsView = new OrderGoodsView(context);
                            orderGoodsView.initData(productsDTO);
                            holder.llyContent.addView(orderGoodsView);
                        }
                    }

                    if (ordersDTO.getShippers().get(i) != null) {
                        OrderWuLiuView orderWuLiuView = new OrderWuLiuView(context);
                        orderWuLiuView.initData(ordersDTO.getShippers().get(i), new OrderWuLiuView.OrderWuLiuListenerSure() {
                            @Override
                            public void onItem() {
                                Intent intent = new Intent(context, WebActivity.class);
                                intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ORDER_EXPRESS_INFO + "/" + ordersDTO.getId() + "?active=" + j);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onQueRenShouHuo() {
                                if (orderListenerSure != null) {
                                    orderListenerSure.queRenShouHuo(ordersDTO.getShippers().get(j).getShipperNumber(), position);
                                }
                            }
                        });
                        holder.llyContent.addView(orderWuLiuView);
                    }
                }
            }
            holder.viewBtn.setVisibility(View.VISIBLE);
            holder.tvOrderStatus.setVisibility(View.VISIBLE);
            holder.rlyDaiPay.setVisibility(View.GONE);
            //待支付
            if (ordersDTO.getOrderStatusCode() == 1) {
                holder.tvDelete.setVisibility(View.GONE);
                holder.tvZaiCiGouMai.setVisibility(View.GONE);
                holder.tvCancel.setVisibility(View.VISIBLE);
                holder.tvPay.setVisibility(View.VISIBLE);
                holder.tvChaKanWuLiu.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(ordersDTO.getExpiredSeconds()) && Long.parseLong(ordersDTO.getExpiredSeconds()) > 0) {
                    holder.tvOrderStatus.setVisibility(View.GONE);
                    holder.rlyDaiPay.setVisibility(View.VISIBLE);
                    long millisInFuture = Long.parseLong(ordersDTO.getExpiredSeconds()) * 1000L;
                    startCountdown(millisInFuture, holder.tvOrderStatus, holder.rlyDaiPay, holder.tvTime, ordersDTO, position);
                } else {
                    holder.tvOrderStatus.setVisibility(View.VISIBLE);
                    holder.rlyDaiPay.setVisibility(View.GONE);
                }
            }
            //待收货
            else if (ordersDTO.getOrderStatusCode() == 2) {
                holder.tvDelete.setVisibility(View.GONE);
                holder.tvZaiCiGouMai.setVisibility(View.GONE);
                holder.tvCancel.setVisibility(View.GONE);
                holder.tvPay.setVisibility(View.GONE);
                //是否物流 shippers 传0
                if (ordersDTO.getShippers() != null && ordersDTO.getShippers().size() > 0) {
                    holder.tvChaKanWuLiu.setVisibility(View.VISIBLE);
                } else {
                    holder.tvChaKanWuLiu.setVisibility(View.GONE);
                    holder.viewBtn.setVisibility(View.GONE);
                }
            }
            //已完成
            else if (ordersDTO.getOrderStatusCode() == 3) {
                holder.tvDelete.setVisibility(View.VISIBLE);
                holder.tvZaiCiGouMai.setVisibility(View.VISIBLE);
                holder.tvCancel.setVisibility(View.GONE);
                holder.tvPay.setVisibility(View.GONE);
                holder.tvChaKanWuLiu.setVisibility(View.GONE);
            }
            //已取消
            else if (ordersDTO.getOrderStatusCode() == 4) {
                holder.tvDelete.setVisibility(View.VISIBLE);
                holder.tvZaiCiGouMai.setVisibility(View.VISIBLE);
                holder.tvCancel.setVisibility(View.GONE);
                holder.tvPay.setVisibility(View.GONE);
                holder.tvChaKanWuLiu.setVisibility(View.GONE);
            }
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ORDER_DETAIL + "/" + ordersDTO.getId());
                    context.startActivity(intent);
                }
            });
            holder.tvChaKanWuLiu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ORDER_EXPRESS_INFO + "/" + ordersDTO.getId() + "?active=0");
                    context.startActivity(intent);
                }
            });
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderListenerSure != null) {
                        orderListenerSure.delete(ordersDTO, position);
                    }
                }
            });
            holder.tvZaiCiGouMai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderListenerSure != null) {
                        orderListenerSure.zaiCiGouMai(ordersDTO, position);
                    }
                }
            });
            holder.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderListenerSure != null) {
                        orderListenerSure.cancel(ordersDTO, position);
                    }
                }
            });
            holder.tvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderListenerSure != null) {
                        orderListenerSure.pay(ordersDTO, position);
                    }
                }
            });
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        TextView tvOrderNumber;
        RelativeLayout rlyDaiPay;
        TextView tvOrderStatus;
        TextView tvTime;
        LinearLayout llyContent;
        TextView tvTotalUseCoupon;
        TextView tvTotalPrice;
        TextView tvDelete;
        TextView tvZaiCiGouMai;
        TextView tvCancel;
        TextView tvPay;
        TextView tvChaKanWuLiu;
        View viewBtn;


        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            rlyDaiPay = itemView.findViewById(R.id.rlyDaiPay);
            tvTime = itemView.findViewById(R.id.tvTime);
            llyContent = itemView.findViewById(R.id.llyContent);
            tvTotalUseCoupon = itemView.findViewById(R.id.tvTotalUseCoupon);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            tvZaiCiGouMai = itemView.findViewById(R.id.tvZaiCiGouMai);
            tvCancel = itemView.findViewById(R.id.tvCancel);
            tvPay = itemView.findViewById(R.id.tvPay);
            tvChaKanWuLiu = itemView.findViewById(R.id.tvChaKanWuLiu);
            viewBtn = itemView.findViewById(R.id.viewBtn);
        }
    }

    private void startCountdown(long millisInFuture, TextView tvOrderStatus, RelativeLayout rlyDaiPay, TextView tvTime, OrderListBean.OrdersDTO ordersDTO, int position) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                // 每秒更新一次UI
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvTime.setText((minutes + 1) + "分钟");
                Log.d("countDownTimer", (minutes + 1) + "分钟");
            }

            public void onFinish() {
                // 倒计时结束
                tvOrderStatus.setVisibility(View.VISIBLE);
                rlyDaiPay.setVisibility(View.GONE);
                if (orderListenerSure != null) {
                    orderListenerSure.refresh(ordersDTO, position);
                }
            }
        };
        countDownTimer.start();
    }

    public interface OrderListenerSure {
        void delete(OrderListBean.OrdersDTO ordersDTO, int pos);

        void zaiCiGouMai(OrderListBean.OrdersDTO ordersDTO, int pos);

        void cancel(OrderListBean.OrdersDTO ordersDTO, int pos);

        void pay(OrderListBean.OrdersDTO ordersDTO, int pos);

        void queRenShouHuo(String id, int pos);

        void refresh(OrderListBean.OrdersDTO ordersDTO, int pos);
    }

}
