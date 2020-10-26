package com.ikhiloya.imokhai.annotationbasedcaching.interceptor;

import com.ikhiloya.imokhai.annotationbasedcaching.PaymentApp;
import com.ikhiloya.imokhai.annotationbasedcaching.annotation.RequiresCaching;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Invocation;
import timber.log.Timber;

import static com.ikhiloya.imokhai.annotationbasedcaching.util.Constant.HEADER_CACHE_CONTROL;
import static com.ikhiloya.imokhai.annotationbasedcaching.util.Constant.HEADER_PRAGMA;

/**
 * This interceptor will be called both if the network is available and if the network is not available
 *
 * @return
 */
public class OfflineCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Invocation invocation = request.tag(Invocation.class);

        if (invocation != null) {
            RequiresCaching annotation = invocation.method().getAnnotation(RequiresCaching.class);
            if (annotation != null && !PaymentApp.hasNetwork()) {
                Timber.d("CACHE ANNOTATION: called.::%s", annotation.getClass().getSimpleName());

                // prevent caching when network is on. For that we use the "networkInterceptor"
                Timber.d("cache interceptor: called.");
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();

            } else {
                Timber.d("cache interceptor: not called.");
            }
        }
        return chain.proceed(request);
    }
}
