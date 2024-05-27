package com.yiqizhuan.app.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.tu.loadingdialog.LoadingDailog;

/**
 * @author LiPeng
 * @create 2024-02-21 3:27 PM
 */
public abstract class BaseFragment extends Fragment {
    private LoadingDailog loadingDialog;

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
}


