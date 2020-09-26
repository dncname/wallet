package com.dnc.loc.data.http;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.dnc.loc.data.http.model.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class EosHttp {

    public static EosApi app;
    private static OkHttpClient okHttpClient;

    public static void init(Context context) {
        app = new Retrofit.Builder()
                .baseUrl(EosConst.BASE_URL)
                .client(getOkHttp())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EosApi.class);
    }


    private static OkHttpClient getOkHttp() {
        if (okHttpClient == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(EosInterceptor.requestBuilderInterceptor())
                    .proxy(Proxy.NO_PROXY)
                    .readTimeout(15, TimeUnit.SECONDS);
            clientBuilder.sslSocketFactory(getSLLContext().getSocketFactory());
            okHttpClient = clientBuilder.build();
        }
        return okHttpClient;
    }


    public static <T> T getData(Response<T> response) {
        LogUtils.e("EosHttp", " getData: " + response.toString() );
        if (response.code != 100) {
            throw new HttpException(response.code, response.msg);
        }
        return response.data;
    }

    /**
     * 信任所有证书
     *
     * @return
     */
    public static SSLContext getSLLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    public static SSLContext getSLLContext(Context context) {
        SSLContext sslContext = null;
        try {
            String crt = "-----BEGIN CERTIFICATE-----\n" +
                    "-----END CERTIFICATE-----\n";
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
//            InputStream cerInputStream = context.getApplicationContext().getAssets().open("12306.cer");

            InputStream cerInputStream = new ByteArrayInputStream(crt.getBytes());
            Certificate ca = cf.generateCertificate(cerInputStream);

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            TrustManager[] trustManagers = tmf.getTrustManagers();

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

//    public static Picasso getPicasso(Context c) {
//        return HttpsPicasso.PicassoHolder.getInstance(getOkHttp(c), c);
//    }

}
