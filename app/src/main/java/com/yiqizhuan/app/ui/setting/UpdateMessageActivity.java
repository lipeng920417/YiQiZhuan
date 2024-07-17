package com.yiqizhuan.app.ui.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.databinding.ActivityUpdateMessageBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseActivity;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-04-16 8:35 PM
 */
public class UpdateMessageActivity extends BaseActivity implements View.OnClickListener {
    ActivityUpdateMessageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        TextView actionbarRight = binding.includeActionbar.actionbarRight;
        includeActionbar.setText("修改昵称");
        actionbarRight.setVisibility(View.VISIBLE);
        actionbarRight.setText("确定");
        actionbarBack.setOnClickListener(this);
        actionbarRight.setOnClickListener(this);
        binding.edtName.setText(MMKVHelper.getString("nickName", ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.actionbar_right:
                userSave();
                break;
        }
    }


    private void userSave() {
        if (TextUtils.isEmpty(binding.edtName.getText().toString())) {
            return;
        }
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", MMKVHelper.getString("userId", ""));
        paramsMap.put("nickName", binding.edtName.getText().toString());
        paramsMap.put("avatarUrl", MMKVHelper.getString("avatarUrl", ""));
        paramsMap.put("phone", MMKVHelper.getString("phone", ""));
        paramsMap.put("state", MMKVHelper.getString("state", ""));
        OkHttpManager.getInstance().postRequest(Api.USER_SAVE, paramsMap, new BaseCallBack<BaseResult<String>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<String> result) {
                cancelLoading();
                MMKVHelper.putString("nickName", binding.edtName.getText().toString());
                LiveEventBus.get("nickName").post("");
                finish();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

}
