package com.ikhiloya.imokhai.annotationbasedcaching;

import android.content.res.Resources;

import com.ikhiloya.imokhai.annotationbasedcaching.annotation.RequiresCaching;
import com.ikhiloya.imokhai.annotationbasedcaching.interceptor.NetworkInterceptor;
import com.ikhiloya.imokhai.annotationbasedcaching.interceptor.OfflineCacheInterceptor;
import com.ikhiloya.imokhai.annotationbasedcaching.service.PaymentService;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Invocation;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomAnnotationUnitTest {
    private static String BASE_URL = "https://dd9a42cd-8472-49a6-8e9d-722ab41ee384.mock.pstmn.io";
    private static String PAYMENT_TYPES = "/payment-types";

    @Test(expected = Resources.NotFoundException.class)
    public void testCoolStuffRequest() throws Exception {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        NetworkInterceptor networkInterceptor = new NetworkInterceptor();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(networkInterceptor)
                .addInterceptor(
                        new OfflineCacheInterceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                final Request request = chain.request();
                                //tes the request method
                                assertEquals("GET", request.method());
                                HttpUrl url = request.url();
                                // Test the url
                                assertEquals(BASE_URL + PAYMENT_TYPES, url.toString());

                                //get the RequiresCaching annotation from the request
                                Invocation invocation = request.tag(Invocation.class);
                                assertNotNull(invocation);
                                RequiresCaching annotation = invocation.method().getAnnotation(RequiresCaching.class);
                                TestCase.assertNotNull(annotation);

                                // The following just ends the test with an expected exception.
                                // You could construct a valid Response and return that instead
                                // Do not return chain.proceed(), because then your unit test may become
                                // subject to the whims of the network
                                throw new Resources.NotFoundException();
                            }
                        })
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        PaymentService paymentService = retrofit.create(PaymentService.class);
        paymentService.getPaymentTypes().execute();

    }


}
