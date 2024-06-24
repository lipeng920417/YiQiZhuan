package com.yiqizhuan.app.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.yiqizhuan.app.R;


public abstract class BaseDialog extends Dialog {
    protected View mContentview;


    public BaseDialog(Context context) {
        this(context, R.style.custom_dialog_theme);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        fillGravity();
        setContentView(getLayoutResId());
        mContentview = getWindow().getDecorView();
        initDialog();
    }

    protected abstract void fillGravity();

    protected abstract int getLayoutResId();

    protected abstract void initDialog();

}
