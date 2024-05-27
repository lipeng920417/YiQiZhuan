package com.yiqizhuan.app.ui.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hjq.permissions.XXPermissions;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.CouponInfoBean;
import com.yiqizhuan.app.bean.UserCouponBean;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.bean.UserInfoBean;
import com.yiqizhuan.app.databinding.FragmentMineBinding;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.setting.SettingActivity;
import com.yiqizhuan.app.util.CheckPermission;
import com.yiqizhuan.app.util.PhoneUtil;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.views.dialog.DialogUtil;
import com.yiqizhuan.app.webview.WebActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private FragmentMineBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MineViewModel mineViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        binding = FragmentMineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        binding.tvName.setOnClickListener(this);
        binding.ivSetting.setOnClickListener(this);
        binding.llyBroker.setOnClickListener(this);
        binding.llyCompleted.setOnClickListener(this);
        binding.avatar.setOnClickListener(this);
        binding.ivHint.setOnClickListener(this);
        binding.ivXiaoxi.setOnClickListener(this);
        initView();
        return root;
    }

    private void initView() {
        LiveEventBus.get("nickName", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.tvName.setText(MMKVHelper.getString("nickName", ""));
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            binding.tvName.setText(MMKVHelper.getString("nickName", ""));
            userInfo();
            queryUserCoupon();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvName:
            case R.id.ivSetting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.llyBroker:
                Intent broker = new Intent(getActivity(), WebActivity.class);
                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ORDER + "?type=1");
                startActivity(broker);
                break;
            case R.id.llyCompleted:
                Intent completed = new Intent(getActivity(), WebActivity.class);
                completed.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ORDER + "?type=2");
                startActivity(completed);
                break;
            case R.id.avatar:
//                LiveEventBus.get("goToLogin").post("");
//                LiveEventBus.get("changeCartNum").post("0");
//                LiveEventBus.get("shopping").post("");
                break;
            case R.id.ivHint:
                couponInfo();
                break;
            case R.id.ivXiaoxi:
                PhoneUtil.getPhone(getActivity());
                break;
        }
    }

    /**
     * 获取用户信息
     */
    private void userInfo() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.USER_INFO, paramsMap, new BaseCallBack<UserInfoBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, UserInfoBean result) {
                MMKVHelper.putUserInfo(result);
                binding.tvName.setText(MMKVHelper.getString("nickName", ""));
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

    /**
     * 创购中心
     */
    private void queryUserCoupon() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.QUERY_USER_COUPON, paramsMap, new BaseCallBack<UserCouponBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, UserCouponBean result) {
                if (result != null && result.getData() != null) {
                    if (!TextUtils.isEmpty(result.getData().getTotalQuota())) {
                        binding.tvBroker.setText(result.getData().getTotalQuota());
                    }
                    if (!TextUtils.isEmpty(result.getData().getMonthQuota())) {
                        binding.tvLimit.setText(result.getData().getMonthQuota());
                    }
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

    /**
     * 额度展示
     */
    private void couponInfo() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.COUPON_INFO, paramsMap, new BaseCallBack<CouponInfoBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, CouponInfoBean result) {
                cancelLoading();
                if (result != null && result.getData() != null && !TextUtils.isEmpty(result.getData().getCouponTips())) {
                    DialogUtil.build1BtnDialog(getActivity(), result.getData().getCouponTips(), "我知道了", true, new DialogUtil.DialogListener1Btn() {
                        @Override
                        public void onPositiveClick(View v) {

                        }
                    }).show();
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

}