package com.ikhiloya.imokhai.annotationbasedcaching.interceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;
import timber.log.Timber;

import static com.ikhiloya.imokhai.annotationbasedcaching.util.Constant.HEADER_CACHE_CONTROL;
import static com.ikhiloya.imokhai.annotationbasedcaching.util.Constant.HEADER_PRAGMA;

/**
 * This interceptor will be called ONLY if the network is available
 *
 * @return
 */
public class NetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Timber.d("network interceptor: called.");

        Response response = chain.proceed(chain.request());

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(20, TimeUnit.SECONDS)
                .build();

        return response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build();
    }
}
