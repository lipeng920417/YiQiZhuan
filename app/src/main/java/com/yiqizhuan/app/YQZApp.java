package com.yiqizhuan.app;

import android.app.Application;
import android.content.Context;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tencent.mmkv.MMKV;

/**
 * @author LiPeng
 * @create 2024-04-13 2:05 PM
 */
public class YQZApp extends Application {
    public static Context applicationContext;

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        MMKV.initialize(applicationContext);
        initLiveEventBus();
    }

    private void initLiveEventBus() {
        LiveEventBus.config().autoClear(true).lifecycleObserverAlwaysActive(true);
    }

}
