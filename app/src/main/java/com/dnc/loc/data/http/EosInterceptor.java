package com.dnc.loc.data.http;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;

class EosInterceptor {

    public static Interceptor requestBuilderInterceptor() {
        return chain -> {
            Request request = chain.request();
            if (request.method().equals("POST")) {
                if (request.body() instanceof FormBody) {
                    JSONObject jsonObject = new JSONObject();
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    FormBody formBody = (FormBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                        try {
                            jsonObject.put(formBody.encodedName(i), formBody.encodedValue(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    request = request.newBuilder().post(body).build();
                }
            }

            Request.Builder builder = request.newBuilder().url(request.url());
            builder.addHeader("Content-Type", "application/json");
//            builder.addHeader("Accept", "*/*");
            return chain.proceed(builder.build());
        };
    }

//    /**
//     * 处理重复多次请求
//     */
//    public static Interceptor provideCacheInterceptor() {
//        return chain -> {
//            Response response = chain.proceed(chain.request());
//            // 正常访问同一请求接口（多次访问同一接口），给2秒缓存，超过时间重新发送请求，否则取缓存数据
//
//            CacheControl cacheControl = new CacheControl.Builder().maxAge(2, TimeUnit.SECONDS).build();
//            return response.newBuilder().header("Cache-Control", cacheControl.toString()).build();
//        };
//    }
//
//    /**
//     * 处理离线请求
//     */
//    public static Interceptor provideOfflineCacheInterceptor(Context context) {
//        return chain -> {
//            Request request = chain.request();
//
//            /**
//             * 未联网获取缓存数据
//             */
//            if (!NetworkUtils.isConnected()) {
//                CacheControl cacheControl = new CacheControl.Builder()
//                        .maxStale(7, TimeUnit.DAYS)
//                        .build();
//
//                request = request.newBuilder()
//                        .cacheControl(cacheControl)
//                        .build();
//            }
//
//            return chain.proceed(request);
//        };
//    }
//
//    public static Cache provideCache(Context c) {
//        Cache cache = null;
//        try {
//            cache = new Cache(new File(c.getCacheDir(), "http-cache"),
//                    20 * 1024 * 1024); // 20 MB
//        } catch (Exception e) {
//        }
//        return cache;
//    }
}
