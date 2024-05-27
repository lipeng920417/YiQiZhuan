package com.yiqizhuan.app.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.yiqizhuan.app.R;

/**
 * @author LiPeng
 * @create 2024-04-23 6:33 PM
 */
public class DialogUtil {

    public static Dialog build1BtnDialog(Context context
            , String message
            , String textPositive
            , boolean cancelable
            , final DialogListener1Btn listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_1btn, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setView(layout);
        TextView msg = (TextView) layout.findViewById(R.id.text_dialog_message);
        msg.setText(message);
        TextView btnPositive = (TextView) layout.findViewById(R.id.btn_dialog_positive);
        btnPositive.setText(textPositive);
        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPositiveClick(v);
                }
                dialog.dismiss();
            }
        });
        return dialog;
    }
    public static Dialog build2BtnDialog(Context context
            , String message
            , String textPositive
            , String textNegative
            , boolean cancelable
            , final DialogListener2Btn listener2Btn) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_2btn, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setView(layout);
        TextView msg = (TextView) layout.findViewById(R.id.text_dialog_message);
        msg.setText(message);
        TextView btnNegative = (TextView) layout.findViewById(R.id.btn_dialog_negative);
        TextView btnPositive = (TextView) layout.findViewById(R.id.btn_dialog_positive);
        btnPositive.setText(textPositive);
        btnNegative.setText(textNegative);
        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2Btn.onPositiveClick(v);
                dialog.dismiss();
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2Btn.onNegativeClick(v);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog build2BtnTitle(Context context
            , String title
            , SpannableString message
            , String textPositive
            , String textNegative
            , boolean cancelable
            , final DialogListener2Btn listener2Btn) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_2btn_left_title, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setView(layout);
        TextView msg = (TextView) layout.findViewById(R.id.text_dialog_message);
        msg.setMovementMethod(LinkMovementMethod.getInstance());
        msg.setText(message);
        TextView btnPositive = (TextView) layout.findViewById(R.id.btn_dialog_positive);
        TextView btnNegative = (TextView) layout.findViewById(R.id.btn_dialog_negative);
        TextView dialogTitle = (TextView) layout.findViewById(R.id.tv_dialog_title);
        dialogTitle.setText(title);
        btnPositive.setText(textPositive);
        btnNegative.setText(textNegative);
        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2Btn.onPositiveClick(v);
                dialog.dismiss();
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2Btn.onNegativeClick(v);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface DialogListener1Btn {
        void onPositiveClick(View v);
    }


    public interface DialogListener2Btn {
        void onPositiveClick(View v);

        void onNegativeClick(View v);
    }

}
