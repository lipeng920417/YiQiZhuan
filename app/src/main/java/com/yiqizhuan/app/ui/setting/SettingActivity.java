package com.yiqizhuan.app.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.MainActivity;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.databinding.ActivitySettingBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.views.dialog.DialogUtil;
import com.yiqizhuan.app.webview.WebActivity;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-04-16 7:52 PM
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("账户设置");
        actionbarBack.setOnClickListener(this);
        binding.llyName.setOnClickListener(this);
        binding.llyAddress.setOnClickListener(this);
        binding.llyFuWuXieYi.setOnClickListener(this);
        binding.llyYinShiXieYi.setOnClickListener(this);
        binding.tvExitLogin.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvName.setText(MMKVHelper.getString("nickName", ""));
        LiveEventBus.get("nickName", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.tvName.setText(MMKVHelper.getString("nickName", ""));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.llyName:
                Intent intent = new Intent(this, UpdateMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.llyAddress:
                Intent address = new Intent(this, WebActivity.class);
                address.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ADDRESS);
                startActivity(address);
                break;
            case R.id.llyFuWuXieYi:
                Intent service = new Intent(this, WebActivity.class);
                service.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.SERVICE_AGREEMENT);
                startActivity(service);
                break;
            case R.id.llyYinShiXieYi:
                Intent privacy = new Intent(this, WebActivity.class);
                privacy.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.PRIVACY_AGREEMENT);
                startActivity(privacy);
                break;
            case R.id.tvExitLogin:
                DialogUtil.build2BtnDialog(this, "您确定要退出登录吗?", "确定", "取消", true, new DialogUtil.DialogListener2Btn() {
                    @Override
                    public void onPositiveClick(View v) {
                        authLogout();
                    }

                    @Override
                    public void onNegativeClick(View v) {

                    }
                }).show();
                break;
            case R.id.tvCancel:
                DialogUtil.build2BtnDialog(this, "账户注销是不可逆行为，会导致您账户下的积分、订单等数据注销后无法恢复。\n" +
                        "您确认要注销当前账户吗？", "确定注销", "取消", true, new DialogUtil.DialogListener2Btn() {
                    @Override
                    public void onPositiveClick(View v) {
                        cancelLogout();
                    }

                    @Override
                    public void onNegativeClick(View v) {

                    }
                }).show();
                break;
        }

    }

    /**
     * 退出登录
     */
    private void authLogout() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().postRequest(Api.AUTH_LOGOUT, paramsMap, new BaseCallBack<BaseResult<String>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<String> result) {
                cancelLoading();
                MMKVHelper.removeLoginMessage();
                LiveEventBus.get("changeCartNum").post("0");
                ToastUtils.showToast("退出登录");
                Intent exitLogin = new Intent(SettingActivity.this, MainActivity.class);
                exitLogin.putExtra("switchHome", "switchHome");
                startActivity(exitLogin);
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    /**
     * 注销账号
     */
    private void cancelLogout() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().postRequest(Api.TERMINATE_ACCOUNT, paramsMap, new BaseCallBack<BaseResult<String>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<String> result) {
                cancelLoading();
                MMKVHelper.removeLoginMessage();
                LiveEventBus.get("changeCartNum").post("0");
                ToastUtils.showToast("退出登录");
                Intent exitLogin = new Intent(SettingActivity.this, MainActivity.class);
                exitLogin.putExtra("switchHome", "switchHome");
                startActivity(exitLogin);
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

}
