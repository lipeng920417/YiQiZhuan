package com.yiqizhuan.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.CaptchaSendBean;
import com.yiqizhuan.app.bean.LoginMessage;
import com.yiqizhuan.app.databinding.ActivityLoginBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.util.CountDownTimerUtils;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.webview.WebActivity;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-04-18 9:39 PM
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    ActivityLoginBinding binding;
    boolean ivCheckbox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("登录注册");
        actionbarBack.setOnClickListener(this);
        binding.tvSend.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
        binding.ivCheckbox.setOnClickListener(this);
        setAgreementSpannable();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.tvSend:
                if (binding.edtPhone.getText().toString().length() == 11) {
                    captchaSend();
                } else {
                    ToastUtils.showToast("请输入正确手机号");
                }
                break;
            case R.id.tvLogin:
                loginCaptcha();
                break;
            case R.id.ivCheckbox:
                ivCheckbox = !ivCheckbox;
                if (ivCheckbox) {
                    binding.ivCheckbox.setImageResource(R.mipmap.ic_checkbox_select);
                } else {
                    binding.ivCheckbox.setImageResource(R.mipmap.ic_checkbox);
                }
                break;
        }

    }

    private void setAgreementSpannable() {
        String service = getString(R.string.login_service_agreement);
        String privacy = getString(R.string.login_privacy_agreement);
        SpannableString spannableInfo = new SpannableString(getString(R.string.login_read_and_agree, service, privacy));

        int start1 = spannableInfo.toString().indexOf(service);
        int end1 = start1 + service.length();
        int start2 = spannableInfo.toString().indexOf(privacy);
        int end2 = start2 + privacy.length();


        ClickableSpan clickAgreement = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent broker = new Intent(LoginActivity.this, WebActivity.class);
                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.SERVICE_AGREEMENT);
                startActivity(broker);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getColor(R.color.color_222222));
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan clickService = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent broker = new Intent(LoginActivity.this, WebActivity.class);
                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.PRIVACY_AGREEMENT);
                startActivity(broker);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getColor(R.color.color_222222));
                ds.setUnderlineText(false);
            }
        };

        spannableInfo.setSpan(clickAgreement, start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableInfo.setSpan(clickService, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvAgreement.setText(spannableInfo);


    }

    /**
     * 登陆
     */
    private void loginCaptcha() {
        if (binding.edtPhone.getText().toString().length() != 11) {
            ToastUtils.showToast("请输入正确手机号");
            return;
        }
        if (TextUtils.isEmpty(binding.edtVerifycode.getText().toString())) {
            ToastUtils.showToast("请输入验证码");
            return;
        }
        if (!ivCheckbox) {
            ToastUtils.showToast("请阅读并勾选服务协议");
            return;
        }
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("phone", binding.edtPhone.getText().toString());
        paramsMap.put("code", binding.edtVerifycode.getText().toString());
        OkHttpManager.getInstance().postRequest(Api.LOGIN_CAPTCHA, paramsMap, new BaseCallBack<LoginMessage>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, LoginMessage result) {
                cancelLoading();
                MMKVHelper.putLoginMessage(result);
                LiveEventBus.get("changeCartNum").post("");
                finish();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    /**
     * 发送验证码
     */
    private void captchaSend() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("phone", binding.edtPhone.getText().toString());
        paramsMap.put("sendType", "1");
        OkHttpManager.getInstance().postRequest(Api.CAPTCHA_SEND, paramsMap, new BaseCallBack<CaptchaSendBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();

            }

            @Override
            public void onSuccess(Call call, Response response, CaptchaSendBean captchaSendBean) {
                cancelLoading();
                //倒计时1分钟
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(binding.tvSend, 60000, 1000);
                mCountDownTimerUtils.start();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
                if (statusCode == 600) {
                    ToastUtils.showToast("短信发送频繁，请您稍后再试！");
                }
            }
        });
    }
}
