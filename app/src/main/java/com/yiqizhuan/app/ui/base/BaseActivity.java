package com.yiqizhuan.app.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.tu.loadingdialog.LoadingDailog;
import com.gyf.immersionbar.ImmersionBar;


/**
 * @author LiPeng
 * @create 2024-02-21 3:14 PM
 */
public abstract class BaseActivity extends AppCompatActivity {
    private LoadingDailog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity管理
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity管理
        ActivityCollector.removeActivity(this);
    }

    public void showLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this).setMessage("加载中...").setCancelable(true).setCancelOutside(false);
        loadingDialog = loadBuilder.create();
        loadingDialog.show();
    }

    public void cancelLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }

}
