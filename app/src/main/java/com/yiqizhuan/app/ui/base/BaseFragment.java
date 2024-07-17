package com.yiqizhuan.app.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.tu.loadingdialog.LoadingDailog;
import com.yiqizhuan.app.views.dialog.DialogUtil;

/**
 * @author LiPeng
 * @create 2024-02-21 3:27 PM
 */
public abstract class BaseFragment extends Fragment {
    private LoadingDailog loadingDialog;

    private Dialog loadingDialog1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(getActivity()).setMessage("加载中...").setCancelable(true).setCancelOutside(false);
        loadingDialog = loadBuilder.create();
        loadingDialog.show();
    }

    public void cancelLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }

    public void showLoadingDialog() {
        dismissLoadingDialog();
        loadingDialog1 = DialogUtil.buildLoadingDialog(getContext());
        loadingDialog1.show();
    }

    public void showLoadingDialog(boolean cancelable) {
        dismissLoadingDialog();
        loadingDialog1 = DialogUtil.buildLoadingDialog(getContext(), cancelable);
        loadingDialog1.show();
    }

    public void dismissLoadingDialog() {
        if (null != loadingDialog1) {
            loadingDialog1.dismiss();
        }
    }

}


