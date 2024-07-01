package com.yiqizhuan.app.ui.shopping.item;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ShopCartBean;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.SkipActivityUtil;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.views.dialog.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


public class ShoppingFlexibleItem extends AbstractFlexibleItem<ShoppingFlexibleItem.ItemViewHolder> {
    private Context context;
    private ShopCartBean.DetailsDTO detailsDTO;
    private ShopCartListenerSure shopCartListenerSure;


    public void setShopCartListenerSure(ShopCartListenerSure shopCartListenerSure) {
        this.shopCartListenerSure = shopCartListenerSure;
    }


    public ShopCartBean.DetailsDTO getDetailsDTO() {
        return detailsDTO;
    }

    public ShoppingFlexibleItem(Context context, ShopCartBean.DetailsDTO detailsDTO) {
        this.context = context;
        this.detailsDTO = detailsDTO;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_shopping;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        if (detailsDTO != null) {
            GlideUtil.loadImage(detailsDTO.getGoodsVO().getImageUrl(), holder.iv, 6);
            holder.tvName.setText(detailsDTO.getGoodsVO().getName());
            StringBuilder stringBuilder = new StringBuilder();
            if (detailsDTO.getGoodsVO().getAttributes() != null && detailsDTO.getGoodsVO().getAttributes().size() > 0) {
                for (ShopCartBean.DetailsDTO.GoodsVODTO.AttributesDTO attributes : detailsDTO.getGoodsVO().getAttributes()) {
                    if (attributes.getAttrDescription() != null && attributes.getAttrDescription().size() > 0) {
                        for (ShopCartBean.DetailsDTO.GoodsVODTO.AttributesDTO.AttrDescriptionDTO attrDescription : attributes.getAttrDescription()) {
                            stringBuilder.append(attrDescription.getAttrValue() + "，");
                        }
                    }
                }
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                holder.tvShuXing.setText(stringBuilder.toString());
            }
            if (TextUtils.equals(detailsDTO.getGoodsVO().getQuota(), "1")) {
                holder.tvMeiRiXianGou.setVisibility(View.VISIBLE);
            } else {
                holder.tvMeiRiXianGou.setVisibility(View.GONE);
            }
            if (detailsDTO.getCartType() == 1) {
                holder.ivTitle.setImageResource(R.mipmap.ic_zunxianghui_title);
                holder.tvMoney.setText(getMoney(detailsDTO.getGoodsVO()));
                holder.tvOriginalPriceL.setVisibility(View.VISIBLE);
                holder.tvOriginalPrice.setVisibility(View.VISIBLE);
            }
            //共享
            else if (detailsDTO.getCartType() == 2) {
                holder.ivTitle.setImageResource(R.mipmap.ic_gongxianghui_title);
                holder.tvMoney.setText(detailsDTO.getGoodsVO().getPrice());
                holder.tvOriginalPriceL.setVisibility(View.GONE);
                holder.tvOriginalPrice.setVisibility(View.GONE);
            }
            //悦享
            else if (detailsDTO.getCartType() == 3) {
                holder.ivTitle.setImageResource(R.mipmap.ic_yuxianghui_title);
                holder.tvMoney.setText(getMoney(detailsDTO.getGoodsVO()));
                holder.tvOriginalPriceL.setVisibility(View.VISIBLE);
                holder.tvOriginalPrice.setVisibility(View.VISIBLE);
            }
            //畅享
            else if (detailsDTO.getCartType() == 4) {
                holder.ivTitle.setImageResource(R.mipmap.ic_changxianghui_title);
                holder.tvMoney.setText(getMoney(detailsDTO.getGoodsVO()));
                holder.tvOriginalPriceL.setVisibility(View.VISIBLE);
                holder.tvOriginalPrice.setVisibility(View.VISIBLE);
            }
            holder.tvOriginalPrice.setText(detailsDTO.getGoodsVO().getPrice());
            holder.tvOriginalPrice.getPaint().setStrikeThruText(true);
            holder.tvNum.setText(detailsDTO.getProductNum() + "");
            if (detailsDTO.isSelect()) {
                holder.ivSelect.setImageResource(R.mipmap.ic_checkbox_select);
            } else {
                holder.ivSelect.setImageResource(R.mipmap.ic_checkbox);
            }
            holder.rlyYiXiaJia.setVisibility(View.GONE);
            if (detailsDTO.getState() == 0) {
                holder.llyCart.setVisibility(View.VISIBLE);
                if (detailsDTO.getGoodsVO().getDeleted() == 1 || detailsDTO.getProductVO().getDeleted() == 1) {
                    holder.ivSelect.setImageResource(R.mipmap.ic_checkbox_no_select);
                    holder.rlyYiXiaJia.setVisibility(View.VISIBLE);
                    holder.tvYiXiaJia.setText("已下架");
                    holder.ivSelect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    holder.llyCart.setVisibility(View.GONE);
                } else if (detailsDTO.getGoodsVO().getStatus() == 0) {
                    holder.ivSelect.setImageResource(R.mipmap.ic_checkbox_no_select);
                    holder.rlyYiXiaJia.setVisibility(View.VISIBLE);
                    holder.tvYiXiaJia.setText("待开售");
                    holder.ivSelect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    holder.llyCart.setVisibility(View.GONE);
                } else if (detailsDTO.getProductNum() == 0) {
                    holder.ivSelect.setImageResource(R.mipmap.ic_checkbox_no_select);
                    holder.rlyYiXiaJia.setVisibility(View.VISIBLE);
                    holder.tvYiXiaJia.setText("已售罄");
                    holder.ivSelect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    holder.llyCart.setVisibility(View.GONE);
                } else {
                    holder.ivSelect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            detailsDTO.setSelect(!detailsDTO.isSelect());
                            if (shopCartListenerSure != null) {
                                shopCartListenerSure.select();
                            }
                        }
                    });
                    holder.llyCart.setVisibility(View.VISIBLE);
                }
            } else {
                holder.llyCart.setVisibility(View.GONE);
                holder.ivSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detailsDTO.setSelect(!detailsDTO.isSelect());
                        if (shopCartListenerSure != null) {
                            shopCartListenerSure.select();
                        }
                    }
                });
            }
            holder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (detailsDTO.getGoodsVO().getDeleted() == 1 || detailsDTO.getProductVO().getDeleted() == 1) {
                        ToastUtils.showToast("该商品已下架，暂不支持查看");
                        return;
                    }
                    SkipActivityUtil.goGoodsDetail(context, detailsDTO.getProductId() + "", detailsDTO.getCartType() + "", detailsDTO.getGoodsVO().getGoodsId() + "");
                }
            });

            holder.ivJianHao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int numJ = Integer.parseInt(holder.tvNum.getText().toString()) - 1;
                    if (numJ > 0) {
                        holder.tvNum.setText(numJ + "");
                        detailsDTO.setProductNum(numJ);
                        if (shopCartListenerSure != null) {
                            shopCartListenerSure.subtraction(detailsDTO, numJ);
                        }
                    } else {
                        ToastUtils.showToast("数量最少为1");
                    }
                }
            });
            holder.ivJiaHao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int numJ = Integer.parseInt(holder.tvNum.getText().toString()) + 1;
                    if (numJ <= detailsDTO.getGoodsVO().getStock()) {
                        if (shopCartListenerSure != null) {
                            shopCartListenerSure.add(detailsDTO, numJ);
                        }
                    } else {
                        ToastUtils.showToast("数量不能超过库存");
                    }
                }
            });

            holder.tvNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUtil.build2BtnDialogNum(context, "修改购买数量", "确定", "取消", true, detailsDTO, new DialogUtil.DialogListener2BtnNum() {
                        @Override
                        public void onPositiveClick(View v, ShopCartBean.DetailsDTO detailsDTO, int num) {
                            if (shopCartListenerSure != null) {
                                shopCartListenerSure.add(detailsDTO, num);
                            }
                        }

                        @Override
                        public void onNegativeClick(View v) {

                        }
                    }).show();
                }
            });
        }
    }

    private String getMoney(ShopCartBean.DetailsDTO.GoodsVODTO goodsVODTO) {
        String sellPrice = String.valueOf(Double.parseDouble(goodsVODTO.getPrice()) - Double.parseDouble(goodsVODTO.getDiscount()));
        String money = sellPrice + "元+" + goodsVODTO.getDiscount() + "积分";
        SpannableString spannableString1 = new SpannableString(money);
        RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(0.6f);
        RelativeSizeSpan relativeSizeSpan2 = new RelativeSizeSpan(0.6f);
        spannableString1.setSpan(relativeSizeSpan1, sellPrice.length(), sellPrice.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(relativeSizeSpan2, money.length() - 2, money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString1.toString();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lly;
        ImageView ivSelect;
        ImageView iv;
        RelativeLayout rlyYiXiaJia;
        TextView tvYiXiaJia;
        TextView tvName;
        TextView tvShuXing;
        TextView tvMeiRiXianGou;
        ImageView ivTitle;
        TextView tvOriginalPriceL;
        TextView tvOriginalPrice;
        ImageView ivJiaHao;
        ImageView ivJianHao;
        TextView tvNum;
        TextView tvMoney;
        LinearLayout llyCart;


        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lly = itemView.findViewById(R.id.lly);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            iv = itemView.findViewById(R.id.iv);
            rlyYiXiaJia = itemView.findViewById(R.id.rlyYiXiaJia);
            tvYiXiaJia = itemView.findViewById(R.id.tvYiXiaJia);
            tvName = itemView.findViewById(R.id.tvName);
            tvShuXing = itemView.findViewById(R.id.tvShuXing);
            tvMeiRiXianGou = itemView.findViewById(R.id.tvMeiRiXianGou);
            ivTitle = itemView.findViewById(R.id.ivTitle);
            tvOriginalPriceL = itemView.findViewById(R.id.tvOriginalPriceL);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            ivJiaHao = itemView.findViewById(R.id.ivJiaHao);
            ivJianHao = itemView.findViewById(R.id.ivJianHao);
            tvNum = itemView.findViewById(R.id.tvNum);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            llyCart = itemView.findViewById(R.id.llyCart);
        }
    }

    public interface ShopCartListenerSure {
        void add(ShopCartBean.DetailsDTO detailsDTO, int num);

        void subtraction(ShopCartBean.DetailsDTO detailsDTO, int num);

        void select();
    }

}
