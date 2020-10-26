package com.ikhiloya.imokhai.annotationbasedcaching.service;

import com.ikhiloya.imokhai.annotationbasedcaching.annotation.RequiresCaching;
import com.ikhiloya.imokhai.annotationbasedcaching.model.PaymentType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PaymentService {

    @RequiresCaching
    @GET("/payment-types")
    Call<List<PaymentType>> getPaymentTypes();

}
