package com.yiqizhuan.app.ui.pay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.OrderPayBean;
import com.yiqizhuan.app.databinding.ActivityPayBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.order.MyOrderActivity;
import com.yiqizhuan.app.util.ClickUtil;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.views.dialog.DialogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-04-16 7:52 PM
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {

    ActivityPayBinding binding;
    private static final int SDK_PAY_FLAG = 1;
    private String id;
    private String orderNumber;
    private String totalPrice;
    private String totalUseCoupon;
    private String source;
    private boolean needCash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("收银台");
        actionbarBack.setOnClickListener(this);
        binding.tvBtn.setOnClickListener(this);
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            orderNumber = getIntent().getStringExtra("orderNumber");
            totalPrice = getIntent().getStringExtra("totalPrice");
            totalUseCoupon = getIntent().getStringExtra("totalUseCoupon");
            source = getIntent().getStringExtra("source");
            needCash = getIntent().getBooleanExtra("needCash", false);
        }
        binding.tvOrderNo.setText(orderNumber);
        if (needCash) {
            binding.tvMoney.setText("￥"+totalPrice);
            binding.rlyMoney.setVisibility(View.VISIBLE);
            binding.tvAliPay.setVisibility(View.VISIBLE);
            binding.rlyAliPay.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(totalUseCoupon) && Double.parseDouble(totalUseCoupon) > 0) {
                binding.rlyJiFen.setVisibility(View.VISIBLE);
                binding.tvJiFen.setText(totalUseCoupon);
            } else {
                binding.rlyJiFen.setVisibility(View.GONE);
            }
        } else {
            binding.rlyMoney.setVisibility(View.GONE);
            binding.tvAliPay.setVisibility(View.GONE);
            binding.rlyAliPay.setVisibility(View.GONE);
            binding.rlyJiFen.setVisibility(View.VISIBLE);
            binding.tvJiFen.setText(totalUseCoupon);
        }
        if (BuildConfig.DEBUG) {
            // 设置支付宝SDK运行模式为沙箱模式
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        goPaySuc();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showToast("支付失败");
                    }
                    Log.d("PayActivity", resultInfo);
                    break;
                }
                default:
                    break;
            }
        }
    };


    /**
     * 支付宝支付业务示例
     */
    public void payV2(String payParam) {
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(payParam, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                if (ClickUtil.isRealClick()) {
                    showDialog();
                }
                break;
            case R.id.tvBtn:
                if (ClickUtil.isRealClick()) {
                    orderPay(id);
                }
                break;
        }
    }

    private void showDialog() {
        DialogUtil.build2BtnDialog(this, "确认要离开收银台吗?", "继续支付", "确认离开", true, new DialogUtil.DialogListener2Btn() {
            @Override
            public void onPositiveClick(View v) {
            }

            @Override
            public void onNegativeClick(View v) {
                goOrder();
            }
        }).show();
    }

    private void goPaySuc() {
        Intent intent = new Intent(PayActivity.this, PaySucceedActivity.class);
        intent.putExtra("source", source);
        startActivity(intent);
        finish();
    }

    private void goOrder() {
//        Intent intent = new Intent(this, WebActivity.class);
//        intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ORDER + "?type=0");
//        startActivity(intent);
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void orderPay(String id) {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        String url;
        if (needCash) {
            url = Api.MALL_ORDER_PAY + "?id=" + id + "&channel=alipay";
        } else {
            url = Api.MALL_ORDER_PAY + "?id=" + id;
        }
        OkHttpManager.getInstance().postRequest(url, paramsMap, new BaseCallBack<BaseResult<OrderPayBean>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<OrderPayBean> result) {
                cancelLoading();
                if (needCash) {
                    if (result != null && result.getData() != null) {
                        payV2(result.getData().getPayParam());
                    }
                } else {
                    goPaySuc();
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }
}
