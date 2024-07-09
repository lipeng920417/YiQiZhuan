package com.yiqizhuan.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.navigation.NavigationBarView;
import com.gyf.immersionbar.ImmersionBar;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.databinding.ActivityMainBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.category.CategoryFragment;
import com.yiqizhuan.app.ui.home.HomeFragment;
import com.yiqizhuan.app.ui.login.LoginActivity;
import com.yiqizhuan.app.ui.mine.MineFragment;
import com.yiqizhuan.app.ui.pay.PayActivity;
import com.yiqizhuan.app.ui.remit.RemitFragment;
import com.yiqizhuan.app.ui.shopping.ShoppingFragment;
import com.yiqizhuan.app.util.SizeUtils;
import com.yiqizhuan.app.util.SkipActivityUtil;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.util.UnreadMsgUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private RemitFragment remitFragment;
    private ShoppingFragment shoppingFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupWithNavController(binding.navView, navController);
        // 初始化Fragments
        initFragment();
        initLiveEventBus();
        pop();
        if (!TextUtils.isEmpty(MMKVHelper.getString("token", ""))) {
            shopCartCount();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null && "switchHome".equals(intent.getExtras().getString("switchHome"))) {
            binding.navView.setSelectedItemId(R.id.navigation_home);
            switchTab(R.id.navigation_home);
        }
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        categoryFragment = new CategoryFragment();
        remitFragment = new RemitFragment();
        shoppingFragment = new ShoppingFragment();
        mineFragment = new MineFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.nav_host_fragment_activity_main, homeFragment);
        transaction.show(homeFragment);
        transaction.add(R.id.nav_host_fragment_activity_main, categoryFragment);
        transaction.show(categoryFragment);
        transaction.add(R.id.nav_host_fragment_activity_main, remitFragment);
        transaction.show(remitFragment);
        transaction.add(R.id.nav_host_fragment_activity_main, shoppingFragment);
        transaction.hide(shoppingFragment);
        transaction.add(R.id.nav_host_fragment_activity_main, mineFragment);
        transaction.hide(mineFragment);
        transaction.commit();
        // 设置BottomNavigationView的监听器
        binding.navView.setItemIconTintList(null);
        binding.navView.setOnItemSelectedListener(onItemSelectedListener);
        switchTab(R.id.navigation_home);
    }

    private NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        if (binding.navView.getSelectedItemId() != item.getItemId()) {
            if (item.getItemId() == R.id.navigation_dashboard || item.getItemId() == R.id.navigation_notifications) {
                if (TextUtils.isEmpty(MMKVHelper.getString("token", ""))) {
                    LiveEventBus.get("goToLogin").post("");
                    return false;
                } else {
                    switchTab(item.getItemId());
                }
            } else {
                switchTab(item.getItemId());
            }
        }
        return true;
    };

    private void switchTab(int itemId) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(homeFragment);
        transaction.hide(categoryFragment);
        transaction.hide(remitFragment);
        transaction.hide(shoppingFragment);
        transaction.hide(mineFragment);
        switch (itemId) {
            case R.id.navigation_home:
                transaction.show(homeFragment);
                binding.navView.getMenu().getItem(0).setIcon(R.mipmap.icon_home_select);
                binding.navView.getMenu().getItem(1).setIcon(R.mipmap.ic_category);
                binding.navView.getMenu().getItem(2).setIcon(R.mipmap.ic_remit);
                binding.navView.getMenu().getItem(3).setIcon(R.mipmap.icon_cart);
                binding.navView.getMenu().getItem(4).setIcon(R.mipmap.icon_my);
                break;
            case R.id.navigation_category:
                transaction.show(categoryFragment);
                binding.navView.getMenu().getItem(0).setIcon(R.mipmap.icon_home);
                binding.navView.getMenu().getItem(1).setIcon(R.mipmap.ic_category_select);
                binding.navView.getMenu().getItem(2).setIcon(R.mipmap.ic_remit);
                binding.navView.getMenu().getItem(3).setIcon(R.mipmap.icon_cart);
                binding.navView.getMenu().getItem(4).setIcon(R.mipmap.icon_my);
                break;
            case R.id.navigation_remit:
                transaction.show(remitFragment);
                binding.navView.getMenu().getItem(0).setIcon(R.mipmap.icon_home);
                binding.navView.getMenu().getItem(1).setIcon(R.mipmap.ic_category);
                binding.navView.getMenu().getItem(2).setIcon(R.mipmap.ic_remit_select);
                binding.navView.getMenu().getItem(3).setIcon(R.mipmap.icon_cart);
                binding.navView.getMenu().getItem(4).setIcon(R.mipmap.icon_my);
                break;
            case R.id.navigation_dashboard:
                transaction.show(shoppingFragment);
                binding.navView.getMenu().getItem(0).setIcon(R.mipmap.icon_home);
                binding.navView.getMenu().getItem(1).setIcon(R.mipmap.ic_category);
                binding.navView.getMenu().getItem(2).setIcon(R.mipmap.ic_remit);
                binding.navView.getMenu().getItem(3).setIcon(R.mipmap.icon_cart_select);
                binding.navView.getMenu().getItem(4).setIcon(R.mipmap.icon_my);
                break;
            case R.id.navigation_notifications:
                transaction.show(mineFragment);
                binding.navView.getMenu().getItem(0).setIcon(R.mipmap.icon_home);
                binding.navView.getMenu().getItem(1).setIcon(R.mipmap.ic_category);
                binding.navView.getMenu().getItem(2).setIcon(R.mipmap.ic_remit);
                binding.navView.getMenu().getItem(3).setIcon(R.mipmap.icon_cart);
                binding.navView.getMenu().getItem(4).setIcon(R.mipmap.icon_my_select);
                break;
        }
        transaction.commit();
    }

    private void initLiveEventBus() {
        //去登录
        LiveEventBus.get("goToLogin", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //token过期
        LiveEventBus.get("expiredToken", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //清除登录信息
                MMKVHelper.removeLoginMessage();
                setBNV_Badge(2, 0);
                ToastUtils.showToast("您的登录状态已过期，请重新登录");
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("switchHome", "switchHome");
                startActivity(intent);

            }
        });
        //去购物车
        LiveEventBus.get("shopping", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.navView.setSelectedItemId(R.id.navigation_dashboard);
                Log.d("LiveEventBus", s);
            }
        });
        //购物车红点增加
        LiveEventBus.get("changeCartNum", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (!TextUtils.isEmpty(s)) {
                    setBNV_Badge(2, Integer.parseInt(s));
                } else {
                    shopCartCount();
                }
            }
        });
        //购物车跳转商品详情
        LiveEventBus.get("jumpToDetail", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                String productId = "";
                String type = "";
                String goodsId = "";
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    productId = jsonObject.getString("productId");
                    type = jsonObject.getString("cartType");
                    goodsId = jsonObject.getString("goodsId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Intent broker = new Intent(MainActivity.this, WebActivity.class);
//                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + productId + "&type=" + type);
//                startActivity(broker);
                SkipActivityUtil.goGoodsDetail(MainActivity.this, productId, type, goodsId);
            }
        });

        //去首页
        LiveEventBus.get("goHome", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("switchHome", "switchHome");
                startActivity(intent);
            }
        });

        //尊享汇
        LiveEventBus.get("goZunXiangHui", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.navView.setSelectedItemId(R.id.navigation_remit);
            }
        });

        //h5跳转app支付
        LiveEventBus.get("jumpAppPay", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                String id = "";
                String orderNumber = "";
                boolean needCash = false;
                String totalPrice = "";
                String totalUseCoupon = "";
                String source = "";
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    id = jsonObject.getString("id");
                    orderNumber = jsonObject.getString("orderNumber");
                    JSONObject jsonObject1 = jsonObject.getJSONObject("confirmVO");
                    needCash = jsonObject1.getBoolean("needCash");
                    totalPrice = jsonObject1.getString("totalPrice");
                    totalUseCoupon = jsonObject1.getString("totalUseCoupon");
                    source = jsonObject1.getString("source");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.this, PayActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("orderNumber", orderNumber);
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("totalUseCoupon", totalUseCoupon);
                intent.putExtra("source", source);
                intent.putExtra("needCash", needCash);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void setBNV_Badge(int position, int num) {
//        if (num <= 0) {
//            return;
//        }
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.navView.getChildAt(0);
        if (null == menuView) {
            return;
        }
        if (position > menuView.getChildCount() - 1) {
            return;
        }
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(position);
        if (itemView.getChildCount() > 2) {
            itemView.removeViewAt(2);
        }
        if (num > 0) {
            View badge = LayoutInflater.from(this).inflate(R.layout.layout_badge, itemView, false);
            TextView numView = badge.findViewById(R.id.tv_badge_num);
            UnreadMsgUtil.show(numView, num);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.leftMargin = menuView.getItemIconSize();
            lp.bottomMargin = SizeUtils.dp2px(28);//BottomNavigationView的高度为50dp
            itemView.addView(badge, lp);
        }
    }

    private void shopCartCount() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.SHOP_CART_COUNT, paramsMap, new BaseCallBack<BaseResult<String>>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<String> result) {
                if (result != null && !TextUtils.isEmpty(result.getData())) {
                    setBNV_Badge(2, Integer.parseInt(result.getData()));
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

    private void pop() {
        binding.ivPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llyPop.setVisibility(View.GONE);
            }
        });
        binding.llyPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String current = new StringBuilder().append(year).append(month).append(day).toString();
        String homePop = MMKVHelper.getString("homePop", "");
        JSONObject object;
        int homePopNum = 0;
        if (!TextUtils.isEmpty(homePop)) {
            try {
                object = new JSONObject(homePop);
                homePopNum = Integer.parseInt(object.getString(current));
            } catch (Exception e) {
            }
        }
        if (homePopNum < 2) {
            homePopNum++;
            binding.llyPop.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(current, homePopNum);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            MMKVHelper.putString("homePop", jsonObject.toString());
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}