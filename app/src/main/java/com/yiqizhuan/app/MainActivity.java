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
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.home.HomeFragment;
import com.yiqizhuan.app.ui.login.LoginActivity;
import com.yiqizhuan.app.ui.mine.MineFragment;
import com.yiqizhuan.app.ui.shopping.ShoppingFragment;
import com.yiqizhuan.app.util.SizeUtils;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.util.UnreadMsgUtil;
import com.yiqizhuan.app.webview.WebActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private HomeFragment homeFragment;
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
        shoppingFragment = new ShoppingFragment();
        mineFragment = new MineFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.nav_host_fragment_activity_main, homeFragment);
        transaction.show(homeFragment);
        transaction.add(R.id.nav_host_fragment_activity_main, shoppingFragment);
        transaction.hide(shoppingFragment);
        transaction.add(R.id.nav_host_fragment_activity_main, mineFragment);
        transaction.hide(mineFragment);
        transaction.commit();
        // 设置BottomNavigationView的监听器
        binding.navView.setOnItemSelectedListener(onItemSelectedListener);
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
        transaction.hide(shoppingFragment);
        transaction.hide(mineFragment);
        switch (itemId) {
            case R.id.navigation_home:
                transaction.show(homeFragment);
                break;
            case R.id.navigation_dashboard:
                transaction.show(shoppingFragment);
                break;
            case R.id.navigation_notifications:
                transaction.show(mineFragment);
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
                setBNV_Badge(1, 0);
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
                    setBNV_Badge(1, Integer.parseInt(s));
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
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    productId = jsonObject.getString("productId");
                    type = jsonObject.getString("cartType");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent broker = new Intent(MainActivity.this, WebActivity.class);
                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + productId + "&type=" + type);
                startActivity(broker);
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
                    setBNV_Badge(1, Integer.parseInt(result.getData()));
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
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