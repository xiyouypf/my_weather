package com.ypf.myweather.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    private static final String TAG = "HttpUtil";
    public static void sendOkHttpRequest(String address, Callback callback) {
        Log.d(TAG, "sendOkHttpRequest: " + address);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Connection", "close")
                .build();
        client.newCall(request).enqueue(callback);
    }
}
