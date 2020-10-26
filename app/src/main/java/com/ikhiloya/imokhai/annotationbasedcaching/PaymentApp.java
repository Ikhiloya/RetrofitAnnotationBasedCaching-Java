package com.ikhiloya.imokhai.annotationbasedcaching;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikhiloya.imokhai.annotationbasedcaching.interceptor.NetworkInterceptor;
import com.ikhiloya.imokhai.annotationbasedcaching.interceptor.OfflineCacheInterceptor;
import com.ikhiloya.imokhai.annotationbasedcaching.service.PaymentService;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class PaymentApp extends Application {
    private static PaymentApp INSTANCE;
    private static String BASE_URL = "https://dd9a42cd-8472-49a6-8e9d-722ab41ee384.mock.pstmn.io";
    private PaymentService paymentService;

    public static PaymentApp get() {
        return INSTANCE;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        //Gson Builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();
        Timber.plant(new Timber.DebugTree());


        // HttpLoggingInterceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        NetworkInterceptor networkInterceptor = new NetworkInterceptor();
        OfflineCacheInterceptor offlineCacheInterceptor = new OfflineCacheInterceptor();


        // OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .cache(cache())
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(networkInterceptor) // only used when network is on
                .addInterceptor(offlineCacheInterceptor)
                .build();


        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        paymentService = retrofit.create(PaymentService.class);

    }

    public PaymentService getPaymentService() {
        return paymentService;
    }


    private static Cache cache() {
        long cacheSize = 5 * 1024 * 1024;
        return new Cache(INSTANCE.getApplicationContext().getCacheDir(), cacheSize);
    }


    public static boolean hasNetwork() {
        return INSTANCE.isNetworkConnected();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm;
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
