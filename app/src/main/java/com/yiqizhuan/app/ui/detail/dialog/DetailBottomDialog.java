package com.yiqizhuan.app.ui.detail.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.GoodsDetailBean;
import com.yiqizhuan.app.bean.ShopCartBean;
import com.yiqizhuan.app.bean.UserCouponBean;
import com.yiqizhuan.app.ui.detail.PhotoViewActivity;
import com.yiqizhuan.app.ui.detail.item.AttributeFlexibleItem;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.views.dialog.BottomDialog;
import com.yiqizhuan.app.views.dialog.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * @author LiPeng
 * @create 2024-06-22 8:39 AM
 */
public class DetailBottomDialog extends BottomDialog implements View.OnClickListener {
    private ImageView ivClose;
    private ImageView ivGoods;
    private ImageView ivFangDa;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvFangAn;
    private TextView tvPriceJiFen;
    private RecyclerView recyclerView;
    private TextView tvJiaRuGoWuChe;
    private TextView tvCommitPrice;
    private TextView tvXianShiYuGu;
    private RelativeLayout rlyJifenquane;
    private LinearLayout llyCommit;
    private ImageView ivJiaHao;
    private ImageView ivJianHao;
    private TextView edtNum;
    private FlexibleAdapter<IFlexible> flexibleAdapter;
    private Context context;
    private GoodsDetailBean goodsDetailBean;
    private GoodsDetailBean.GoodsAndAttrMapping mapping;
    private String type;
    private DialogListenerSure dialogListenerSure;
    private boolean selectGoodsAndAttrMapping;
    private UserCouponBean userCouponBean;
    private boolean isClickShopping;
    private boolean isClickCommit;
    private int currentNum;
    private int MIN_VALUE = 1;
    private int MAX_VALUE = 1; // 设置你的最大值


    public DetailBottomDialog(Context context) {
        super(context);
        this.context = context;
    }

    public DetailBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_detail_bottomdialog;
    }

    @Override
    protected void initDialog() {
        ivClose = findViewById(R.id.ivClose);
        ivGoods = findViewById(R.id.ivGoods);
        ivFangDa = findViewById(R.id.ivFangDa);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvFangAn = findViewById(R.id.tvFangAn);
        tvPriceJiFen = findViewById(R.id.tvPriceJiFen);
        recyclerView = findViewById(R.id.recyclerView);
        tvJiaRuGoWuChe = findViewById(R.id.tvJiaRuGoWuChe);
        tvCommitPrice = findViewById(R.id.tvCommitPrice);
        tvXianShiYuGu = findViewById(R.id.tvXianShiYuGu);
        rlyJifenquane = findViewById(R.id.rlyJifenquane);
        llyCommit = findViewById(R.id.llyCommit);
        ivJiaHao = findViewById(R.id.ivJiaHao);
        ivJianHao = findViewById(R.id.ivJianHao);
        edtNum = findViewById(R.id.edtNum);
        flexibleAdapter = new FlexibleAdapter<>(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(flexibleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ivClose.setOnClickListener(this);
        tvJiaRuGoWuChe.setOnClickListener(this);
        llyCommit.setOnClickListener(this);
        ivJiaHao.setOnClickListener(this);
        ivJianHao.setOnClickListener(this);
        ivFangDa.setOnClickListener(this);
//        edtNum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String input = s.toString();
//
//                // 防止输入前导零
//                if (input.startsWith("0") && input.length() > 1) {
//                    edtNum.setText(input.substring(1));
//                    edtNum.setSelection(edtNum.getText().length()); // 设置光标位置
//                    return;
//                }
//
//                // 防止输入为空或0
//                if (input.isEmpty() || input.equals("0")) {
//                    edtNum.setText(String.valueOf(MIN_VALUE));
//                    edtNum.setSelection(edtNum.getText().length()); // 设置光标位置
//                    return;
//                }
//                if (!input.isEmpty()) {
//                    try {
//                        int value = Integer.parseInt(input);
//                        if (value < MIN_VALUE) {
//                            edtNum.setText(String.valueOf(MIN_VALUE));
//                        } else if (value > MAX_VALUE) {
//                            edtNum.setText(String.valueOf(MAX_VALUE));
//                        }
//                    } catch (NumberFormatException e) {
//                        // 忽略非法输入
//                    }
//                } else {
//                    edtNum.setText(String.valueOf(MIN_VALUE));
//                }
//                // 将光标移到文本的末尾
//                edtNum.setSelection(edtNum.getText().length());
//                Log.d("afterTextChanged", edtNum.getText().toString());
//                currentNum = Integer.parseInt(edtNum.getText().toString());
//                setPrice();
//            }
//        });
        edtNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopCartBean.DetailsDTO detailsDTO = new ShopCartBean.DetailsDTO();
                detailsDTO.setProductNum(currentNum);
                ShopCartBean.DetailsDTO.GoodsVODTO goodsVODTO = new ShopCartBean.DetailsDTO.GoodsVODTO();
                goodsVODTO.setStock(MAX_VALUE);
                detailsDTO.setGoodsVO(goodsVODTO);
                DialogUtil.build2BtnDialogNum(context, "修改购买数量", "确定", "取消", true, detailsDTO, new DialogUtil.DialogListener2BtnNum() {
                    @Override
                    public void onPositiveClick(View v, ShopCartBean.DetailsDTO detailsDTO, int num) {
                        currentNum = num;
                        edtNum.setText(num+"");
                    }

                    @Override
                    public void onNegativeClick(View v) {

                    }
                }).show();
            }
        });
    }

    public void initData(GoodsDetailBean goodsDetailBean, GoodsDetailBean.GoodsAndAttrMapping mapping, String type, UserCouponBean userCouponBean, boolean selectGoodsAndAttrMapping, int currentNum, DialogListenerSure dialogListenerSure) {
        this.goodsDetailBean = goodsDetailBean;
        this.mapping = mapping;
        this.type = type;
        this.userCouponBean = userCouponBean;
        this.selectGoodsAndAttrMapping = selectGoodsAndAttrMapping;
        this.currentNum = currentNum;
        this.dialogListenerSure = dialogListenerSure;
        if (Integer.parseInt(mapping.getInventory()) > 0) {
            MAX_VALUE = Integer.parseInt(mapping.getInventory());
        } else {
            MAX_VALUE = 1;
        }
        for (String string : mapping.getAttrs()) {
            setAttr(true, string, true);
        }
        edtNum.setText(currentNum + "");
        setPrice();
        for (int i = 0; i < goodsDetailBean.getAttributes().size(); i++) {
            goodsDetailBean.getAttributes().get(i).setPos(i);
            AttributeFlexibleItem attributeFlexibleItem = new AttributeFlexibleItem(context, goodsDetailBean.getAttributes().get(i));
            attributeFlexibleItem.setOnClickListener(new AttributeFlexibleItem.OnClickListener() {
                @Override
                public void onPositiveClick(boolean b, GoodsDetailBean.AttrDescription attrDescription, int pos) {
                    setAttr(b, attrDescription.getAttrId(), false);
                    flexibleAdapter.notifyDataSetChanged();
                }
            });
            flexibleAdapter.addItem(attributeFlexibleItem);
        }
    }

    //是否存在
    private boolean isAttr(GoodsDetailBean.Attributes attributes, String attrId) {
        for (GoodsDetailBean.AttrDescription attrDescription : attributes.getAttrDescription()) {
            if (TextUtils.equals(attrDescription.getAttrId(), attrId)) {
                return true;
            }

        }
        return false;
    }

    //选中、取消选中
    private void setAttr(boolean b, String attrId, boolean isFirst) {
        if (b) {
            List<String> allAttr = new ArrayList<>();
            //所有包含的Mapping 包含当前选中的
            for (GoodsDetailBean.GoodsAndAttrMapping goodsAndAttrMapping : goodsDetailBean.getGoodsAndAttrMapping()) {
                if (goodsAndAttrMapping.getAttrs() != null && goodsAndAttrMapping.getAttrs().contains(attrId)) {
                    allAttr.addAll(goodsAndAttrMapping.getAttrs());
                }
            }
            //取出所有选择的属性
            for (GoodsDetailBean.Attributes attributes : goodsDetailBean.getAttributes()) {
                if (isAttr(attributes, attrId)) {
                    //选中处理当前行
                    for (GoodsDetailBean.AttrDescription attrDescription1 : attributes.getAttrDescription()) {
                        if (TextUtils.equals(attrId, attrDescription1.getAttrId())) {
                            attrDescription1.setSelect(1);
                        } else {
                            if (attrDescription1.getSelect() == 1) {
                                attrDescription1.setSelect(0);
                            }
                        }
                    }
                } else {
                    //处理其他行
                    for (GoodsDetailBean.AttrDescription attrDescription1 : attributes.getAttrDescription()) {
                        if (allAttr.contains(attrDescription1.getAttrId())) {
                            if (attrDescription1.getSelect() == 2) {
                                attrDescription1.setSelect(0);
                            }
                        } else {
                            attrDescription1.setSelect(2);
                        }
                    }
                }
            }
            //取出选中的商品
            List<String> selectList = new ArrayList<>();
            for (GoodsDetailBean.Attributes attributes : goodsDetailBean.getAttributes()) {
                for (GoodsDetailBean.AttrDescription attrDescription1 : attributes.getAttrDescription()) {
                    if (attrDescription1.getSelect() == 1) {
                        selectList.add(attrDescription1.getAttrId());
                    }
                }
            }
            //取出最终商品,交集
            for (GoodsDetailBean.GoodsAndAttrMapping goodsAndAttrMapping : goodsDetailBean.getGoodsAndAttrMapping()) {
                if (goodsAndAttrMapping.getAttrs() != null && compareList(goodsAndAttrMapping.getAttrs(), selectList)) {
                    if (compareList(selectList, goodsAndAttrMapping.getAttrs())) {
                        selectGoodsAndAttrMapping = true;
                        mapping = goodsAndAttrMapping;
                        if (Integer.parseInt(mapping.getInventory()) > 0) {
                            MAX_VALUE = Integer.parseInt(mapping.getInventory());
                        } else {
                            MAX_VALUE = 1;
                        }
                        if (!isFirst) {
                            currentNum = 1;
                            edtNum.setText("1");
                        }
                        setPrice();
                    }
                }
            }
        } else {
            selectGoodsAndAttrMapping = false;
            //取消选中
            //取出所有选择的属性
            //取出选中的商品
            List<String> selectList = new ArrayList<>();
            for (GoodsDetailBean.Attributes attributes : goodsDetailBean.getAttributes()) {
                if (isAttr(attributes, attrId)) {
                    //选中处理当前行
                    for (GoodsDetailBean.AttrDescription attrDescription1 : attributes.getAttrDescription()) {
                        if (TextUtils.equals(attrId, attrDescription1.getAttrId())) {
                            attrDescription1.setSelect(0);
                        } else {
                            if (attrDescription1.getSelect() == 2) {
                                attrDescription1.setSelect(0);
                            }
                        }
                    }
                } else {
                    //处理其他行
                    for (GoodsDetailBean.AttrDescription attrDescription1 : attributes.getAttrDescription()) {
                        if (attrDescription1.getSelect() == 2) {
                            attrDescription1.setSelect(0);
                        }
                    }
                }
            }

            //取出选中的商品
            for (GoodsDetailBean.Attributes attributes : goodsDetailBean.getAttributes()) {
                for (GoodsDetailBean.AttrDescription attrDescription1 : attributes.getAttrDescription()) {
                    if (attrDescription1.getSelect() == 1) {
                        selectList.add(attrDescription1.getAttrId());
                    }
                }
            }
            //循环调用
            for (String select : selectList) {
                setAttr(true, select, false);
            }
        }
    }

    //前面包含后面
    private boolean compareList(List<String> list, List<String> list1) {
        if (list != null && list.size() > 0 && list1 != null && list1.size() > 0) {
            for (String s : list1) {
                if (!list.contains(s)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void setPrice() {
        isClickShopping = false;
        isClickCommit = false;
        GlideUtil.loadImage(mapping.getGoodsImageUrl(), ivGoods, 4);
        tvName.setText(goodsDetailBean.getProductName());
        tvPrice.setText(mapping.getPrice());
        tvCommitPrice.setText("￥" + mapping.getGoodsSellPrice());
        //顶部价格
        //尊享
        if (TextUtils.equals(type, "1")) {
            tvFangAn.setText("积分+现金兑换方案");
            tvPriceJiFen.setVisibility(View.VISIBLE);
            String sellPrice = mapping.getDiscount() + " 积分 + " + mapping.getGoodsSellPrice() + " 元";
            SpannableString spannableString1 = new SpannableString(sellPrice);
            RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(0.6f);
            RelativeSizeSpan relativeSizeSpan2 = new RelativeSizeSpan(0.6f);
            spannableString1.setSpan(relativeSizeSpan1, mapping.getDiscount().length(), mapping.getDiscount().length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString1.setSpan(relativeSizeSpan2, sellPrice.length() - 1, sellPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvPriceJiFen.setText(spannableString1);
            tvPrice.getPaint().setStrikeThruText(true);
        }
        //共享
        else if (TextUtils.equals(type, "2")) {
            tvFangAn.setText("共享合作 共创未来");
            tvPriceJiFen.setVisibility(View.GONE);
        }
        //悦享
        else if (TextUtils.equals(type, "3")) {
            tvFangAn.setText("积分全额抵扣");
            tvPriceJiFen.setVisibility(View.GONE);
            tvPrice.getPaint().setStrikeThruText(true);
        }
        //畅享
        else if (TextUtils.equals(type, "4")) {
            tvFangAn.setText("积分全额抵扣");
            tvPriceJiFen.setVisibility(View.GONE);
            tvPrice.getPaint().setStrikeThruText(true);
        }
        //底部按钮
        if (Integer.parseInt(goodsDetailBean.getDeleted()) == 1) {
            tvCommitPrice.setText("已下架");
            tvCommitPrice.setTextSize(14f);
            tvXianShiYuGu.setVisibility(View.GONE);
            llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
            tvJiaRuGoWuChe.setBackground(context.getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
            isClickShopping = false;
            isClickCommit = false;
        } else if (mapping.getStatus() == 0) {
            tvCommitPrice.setText("待开售");
            tvCommitPrice.setTextSize(14f);
            tvXianShiYuGu.setVisibility(View.GONE);
            llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
            tvJiaRuGoWuChe.setBackground(context.getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
            isClickShopping = false;
            isClickCommit = false;
        } else if (mapping.getStatus() == 1 && (Integer.parseInt(mapping.getInventory()) == 0)) {
            tvCommitPrice.setText("已售罄");
            tvCommitPrice.setTextSize(14f);
            tvXianShiYuGu.setVisibility(View.GONE);
            llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
            tvJiaRuGoWuChe.setBackground(context.getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
            isClickShopping = false;
            isClickCommit = false;
        } else {
            tvJiaRuGoWuChe.setBackground(context.getResources().getDrawable(R.drawable.background_conner_ffd418_ffbc1f_21dp));
            isClickShopping = true;
            isClickCommit = true;
            //尊享
            if (TextUtils.equals(type, "1")) {
                tvXianShiYuGu.setVisibility(View.VISIBLE);
                llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
                if (userCouponBean != null && userCouponBean.getData() != null && ((Double.parseDouble(mapping.getDiscount()) * currentNum) > Double.parseDouble(userCouponBean.getData().getTotalQuota()))) {
                    isClickCommit = false;
                    tvXianShiYuGu.setText("您的积分不足 ");
                    llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
                } else {
                    isClickCommit = true;
                    tvXianShiYuGu.setText("使用积分后单价 ");
                    llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
                }
            }
            //共享
            else if (TextUtils.equals(type, "2")) {
                tvXianShiYuGu.setVisibility(View.GONE);
                llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
            }
            //悦享 畅享
            else if (TextUtils.equals(type, "3") || TextUtils.equals(type, "4")) {
                tvXianShiYuGu.setVisibility(View.GONE);
                rlyJifenquane.setVisibility(View.VISIBLE);
                String jifen = "0元购";
                if (userCouponBean != null && userCouponBean.getData() != null && ((Double.parseDouble(mapping.getDiscount()) * currentNum) > Double.parseDouble(userCouponBean.getData().getTotalQuota()))) {
                    isClickCommit = false;
                    jifen = "您的积分不足";
                    llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
                } else {
                    isClickCommit = true;
                    llyCommit.setBackground(context.getResources().getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
                }
                tvCommitPrice.setText(jifen);
                tvCommitPrice.setTextSize(14f);
            }
        }
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        lp.height = (int) (displayMetrics.heightPixels * 0.7);
        window.setAttributes(lp);
        window.setBackgroundDrawableResource(R.drawable.background_conner_ffffff_top_20dp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dialogListenerSure != null) {
            dialogListenerSure.onPositiveClick(goodsDetailBean, mapping, currentNum, selectGoodsAndAttrMapping);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismiss();
                break;
            case R.id.ivJiaHao:
                int num = Integer.parseInt(edtNum.getText().toString()) + 1;
                edtNum.setText(num + "");
                break;
            case R.id.ivJianHao:
                int numJ = Integer.parseInt(edtNum.getText().toString()) - 1;
                if (numJ > 0) {
                    edtNum.setText(numJ + "");
                }
                break;
            case R.id.tvJiaRuGoWuChe:
                if (isClickShopping) {
                    if (selectGoodsAndAttrMapping) {
                        if (dialogListenerSure != null) {
                            dialogListenerSure.shopcartAction(goodsDetailBean, mapping, currentNum, selectGoodsAndAttrMapping);
                        }
                    } else {
                        ToastUtils.showToast("请选择商品规格");
                    }
                }
                break;
            case R.id.llyCommit:
                if (isClickCommit) {
                    if (selectGoodsAndAttrMapping) {
                        if (dialogListenerSure != null) {
                            dialogListenerSure.addShopcartPaymentConfirm(goodsDetailBean, mapping, currentNum, selectGoodsAndAttrMapping);
                        }
                    } else {
                        ToastUtils.showToast("请选择商品规格");
                    }
                }
                break;
            case R.id.ivFangDa:
                List<String> imageUrls = new ArrayList<>();
                imageUrls.add(mapping.getGoodsImageUrl());
                Intent photoView = new Intent(context, PhotoViewActivity.class);
                photoView.putStringArrayListExtra("imageUrls", (ArrayList<String>) imageUrls);
                context.startActivity(photoView);
                break;
        }
    }


    public interface DialogListenerSure {
        void onPositiveClick(GoodsDetailBean goodsDetailBean, GoodsDetailBean.GoodsAndAttrMapping mapping, int num, boolean selectGoodsAndAttrMapping);

        void shopcartAction(GoodsDetailBean goodsDetailBean, GoodsDetailBean.GoodsAndAttrMapping mapping, int num, boolean selectGoodsAndAttrMapping);

        void addShopcartPaymentConfirm(GoodsDetailBean goodsDetailBean, GoodsDetailBean.GoodsAndAttrMapping mapping, int num, boolean selectGoodsAndAttrMapping);
    }
}
