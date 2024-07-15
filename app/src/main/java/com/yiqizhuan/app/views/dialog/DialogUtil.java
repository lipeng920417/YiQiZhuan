package com.yiqizhuan.app.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ShopCartBean;
import com.yiqizhuan.app.util.ToastUtils;

/**
 * @author LiPeng
 * @create 2024-04-23 6:33 PM
 */
public class DialogUtil {

    public static Dialog build1BtnDialog(Context context, String message, String textPositive, boolean cancelable, final DialogListener1Btn listener) {
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

    public static Dialog build2BtnDialog(Context context, String message, String textPositive, String textNegative, boolean cancelable, final DialogListener2Btn listener2Btn) {
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

    public static Dialog build2BtnTitle(Context context, String title, SpannableString message, String textPositive, String textNegative, boolean cancelable, final DialogListener2Btn listener2Btn) {
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

    public static Dialog build2BtnDialogNum(Context context, String message, String textPositive, String textNegative, boolean cancelable, ShopCartBean.DetailsDTO detailsDTO, final DialogListener2BtnNum listener2Btn) {
        int MIN_VALUE = 0;
        int MAX_VALUE = 1;
        if (detailsDTO.getGoodsVO().getStock() > 0) {
            MAX_VALUE = detailsDTO.getGoodsVO().getStock();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_2btn_num, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setView(layout);
        TextView msg = (TextView) layout.findViewById(R.id.text_dialog_message);
        ImageView ivJiaHao = (ImageView) layout.findViewById(R.id.ivJiaHao);
        ImageView ivJianHao = (ImageView) layout.findViewById(R.id.ivJianHao);
        EditText edtNum = (EditText) layout.findViewById(R.id.edtNum);
        msg.setText(message);
        edtNum.setText(detailsDTO.getProductNum() + "");
        TextView btnNegative = (TextView) layout.findViewById(R.id.btn_dialog_negative);
        TextView btnPositive = (TextView) layout.findViewById(R.id.btn_dialog_positive);
        btnPositive.setText(textPositive);
        btnNegative.setText(textNegative);
        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 1;
                if (!TextUtils.isEmpty(edtNum.getText().toString().trim())) {
                    if (Integer.parseInt(edtNum.getText().toString().trim()) > 0) {
                        num = Integer.parseInt(edtNum.getText().toString().trim());
                    }
                }
                listener2Btn.onPositiveClick(v, detailsDTO, num);
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
        ivJianHao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numJ = Integer.parseInt(edtNum.getText().toString()) - 1;
                if (numJ > 0) {
                    edtNum.setText(numJ + "");
                    edtNum.setSelection(edtNum.getText().length());
                } else {
                    ToastUtils.showToast("数量最少为1");
                }
            }
        });
        ivJiaHao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numJ = Integer.parseInt(edtNum.getText().toString()) + 1;
                if (numJ <= detailsDTO.getGoodsVO().getStock()) {
                    edtNum.setText(numJ + "");
                    edtNum.setSelection(edtNum.getText().length());
                } else {
                    ToastUtils.showToast("数量不能超过库存");
                }
            }
        });
        int finalMAX_VALUE = MAX_VALUE;
        edtNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                // 防止输入前导零
//                if (input.startsWith("0") && input.length() > 1) {
//                    edtNum.setText(input.substring(1));
//                    edtNum.setSelection(edtNum.getText().length()); // 设置光标位置
//                    return;
//                }

                // 防止输入为空或0
//                if (input.isEmpty() || input.equals("0")) {
//                    edtNum.setText(String.valueOf(MIN_VALUE));
//                    edtNum.setSelection(edtNum.getText().length()); // 设置光标位置
//                    return;
//                }
                if (!input.isEmpty()) {
                    try {
                        int value = Integer.parseInt(input);
//                        if (value < MIN_VALUE) {
//                            edtNum.setText(String.valueOf(MIN_VALUE));
//                        } else
                        if (value > finalMAX_VALUE) {
                            edtNum.setText(String.valueOf(finalMAX_VALUE));
                            edtNum.setSelection(edtNum.getText().length());
                        }
                    } catch (NumberFormatException e) {
                        // 忽略非法输入
                    }
                } else {
//                    edtNum.setText(String.valueOf(MIN_VALUE));
                }
                // 将光标移到文本的末尾
//                edtNum.setSelection(edtNum.getText().length());
                Log.d("afterTextChanged", edtNum.getText().toString());
            }
        });
        return dialog;
    }

    /**
     * 新版loading，透明背景无字
     *
     * @param context
     * @return
     */
    public static Dialog buildLoadingDialog(Context context) {
        return buildLoadingDialog(context, true);
    }

    /**
     * 新版loading，透明背景无字
     *
     * @param context
     * @param cancelable
     * @return
     */
    public static Dialog buildLoadingDialog(Context context, boolean cancelable) {
        return buildLoadingDialog(context, "", cancelable);
    }

    /**
     * 新版loading，透明背景有字
     *
     * @param context
     * @return
     */
    public static Dialog buildLoadingDialog(Context context, String msg, boolean cancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_loading_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.up_dialogstyle);
        builder.setCancelable(cancelable);
        builder.setView(layout);
        ImageView img = layout.findViewById(R.id.loading_img);
        TextView tvMsg = layout.findViewById(R.id.tvMsg);
        try {
            Glide.with(context).asGif().load(R.mipmap.loading).into(img);
        } catch (Exception e) {
        }
//        AnimUtil.startLoadingAnimation(img, context);
        if (!TextUtils.isEmpty(msg)) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(msg);
        } else {
            tvMsg.setVisibility(View.GONE);
        }
        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                AnimUtil.stopLoadingAnimation();
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

    public interface DialogListener2BtnNum {
        void onPositiveClick(View v, ShopCartBean.DetailsDTO detailsDTO, int num);

        void onNegativeClick(View v);
    }

}
