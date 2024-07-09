package com.yiqizhuan.app.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.util.AppUtil;
import com.yiqizhuan.app.util.ToastUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Author : lipeng
 * @Time : 2023/3/6
 */
public class OkHttpManager {
    int CALL_TIME = 50;

    private static OkHttpManager mOkHttpManager;

    private OkHttpClient mOkHttpClient;

    private Gson mGson;

    private Handler handler;

    private OkHttpManager() {
        mOkHttpClient = OkHttpCustomTrustManager.getUnsafeOkHttpClient();
        mOkHttpClient.newBuilder().connectTimeout(CALL_TIME, TimeUnit.SECONDS).readTimeout(CALL_TIME, TimeUnit.SECONDS).writeTimeout(CALL_TIME, TimeUnit.SECONDS);
        mGson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    //创建单例模式
    public static OkHttpManager getInstance() {
        if (null == mOkHttpManager) {
            synchronized (OkHttpManager.class) {
                if (null == mOkHttpManager) {
                    mOkHttpManager = new OkHttpManager();
                }
            }
        }
        return mOkHttpManager;
    }

    /***********************
     * 对外公布的可调方法
     ************************/

    public void getRequest(String url, Map<String, String> params, final BaseCallBack callBack) {
        url = getCommonUrl(url);
        logRequest(url, params);
        Request request = buildRequest(getUrl(url, params), null, HttpMethodType.GET);
        doRequest(request, callBack);
    }

    public void postRequest(String url, Map<String, String> params, final BaseCallBack callBack) {
        url = getCommonUrl(url);
        logRequest(url, params);
        Request request = buildRequest(url, params, HttpMethodType.POST);
        doRequest(request, callBack);
    }


    public void postRequestObject(String url, Object params, final BaseCallBack callBack) {
        url = getCommonUrl(url);
        Request request = buildRequestString(url, params);
        doRequest(request, callBack);
    }

    //去进行网络 异步 请求

    /**
     * @param request
     * @param callBack 200：正常
     *                 400：请求参数异常
     *                 401：密码错误/验证码错误/token失效等
     *                 500：服务器内部错误
     *                 600：业务异常
     */
    private void doRequest(Request request, final BaseCallBack callBack) {
//        callBack.OnRequestBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                callBack.onFailure(call, e);
                callBackFailure(callBack, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                callBack.onResponse(response);
                String result = response.body().string();
                Log.d("YQZ-Response", request + "-" + result);
                if (response.isSuccessful()) {
                    if (callBack.mType == String.class) {
//                        callBack.onSuccess(call, response, result);
                        callBackSuccess(callBack, call, response, result);
                    } else {
                        try {
                            Object object = mGson.fromJson(result, callBack.mType);//自动转化为 泛型对象
                            if (object != null) {
                                BaseResult baseResult = (BaseResult) object;
                                if (baseResult.getCode() == 200) {
                                    callBackSuccess(callBack, call, response, object);
                                } else if (baseResult.getCode() == 401) {
                                    LiveEventBus.get("expiredToken").post(baseResult.getMsg());
                                    callBackError(callBack, call, baseResult.getCode());
                                } else {
                                    callBackError(callBack, call, baseResult.getCode());
                                }
                            } else {
                                callBackError(callBack, call, response.code());
                            }
//                            callBack.onSuccess(call, response, object);
                        } catch (JsonParseException e) {
                            //json解析错误时调用
//                            callBack.onError(call, response.code(), e);
                            callBackError(callBack, call, response.code());
                        }

                    }
                } else {
//                    callBack.onError(call, response.code(), null);
                    callBackError(callBack, call, response.code());
                }
            }
        });
    }

    //创建 Request对象
    private Request buildRequest(String url, Map<String, String> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (methodType == HttpMethodType.GET) {
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            RequestBody requestBody = buildFormDataJson(params);
            builder.post(requestBody);
        }
        //公参
        builder.addHeader("X-Auth-Token", MMKVHelper.getString("token", ""));
        builder.addHeader("clientType", "1");
        builder.addHeader("clientVersion", BuildConfig.VERSION_NAME);
        return builder.build();
    }

    private Request buildRequestString(String url, Object params) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Gson gson = new Gson();
        String json = gson.toJson(params);
        Log.d("YQZ-Request-", url + "-" + json);
//        String json = JSON.toJSON(params);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        builder.post(requestBody);
        //公参
        builder.addHeader("X-Auth-Token", MMKVHelper.getString("token", ""));
        builder.addHeader("clientType", "1");
        builder.addHeader("clientVersion", BuildConfig.VERSION_NAME);
        return builder.build();
    }


    //get请求拼接参数
    private String getUrl(String url, Map<String, String> params) {
        if (params != null) {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : params.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                try {
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pos++;
            }
            url = url + "?" + tempParams;
        }
        return url;
    }

    //post请求拼接参数
    private RequestBody buildFormData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    //post请求拼接参数
    private RequestBody buildFormDataJson(Map<String, String> params) {
        String json = JSON.toJSONString(params);
        return FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    private RequestBody buildFormDataJson(Object object) {
        String json = JSON.toJSONString(object);
        return FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }


    private void logRequest(String url, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("clientType", "1");
        params.put("clientVersion", BuildConfig.VERSION_NAME);
        Log.d("YQZ-Request-", url + "-" + JSON.toJSONString(params));
    }

    //Activity页面所有的请求以Activity对象作为tag，可以在onDestroy()里面统一取消,this
    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    private void callBackSuccess(final BaseCallBack callBack, final Call call, final Response response, final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(call, response, object);
            }
        });

    }

    private void callBackError(final BaseCallBack callBack, final Call call, final int code) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(call, code, null);
            }
        });

    }

    private void callBackFailure(final BaseCallBack callBack, final Call call, IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast("手机网络开小差啦");
                callBack.onFailure(call, e);
            }
        });

    }

    enum HttpMethodType {
        GET, POST
    }

    /**
     * 请求全路径
     *
     * @return
     */
    public static String getCommonUrl(String url) {
        return BuildConfig.BASE_URL + url;
    }

}

