package com.ikhiloya.imokhai.annotationbasedcaching.di;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;

import com.ikhiloya.imokhai.annotationbasedcaching.PaymentApp;
import com.ikhiloya.imokhai.annotationbasedcaching.service.PaymentService;

/**
 * Created by Ikhiloya Imokhai on 2020-10-26.
 * <p>
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */

public class Injector {

    /**
     * Creates an instance of  [PaymentService]
     */
    public static PaymentService providePaymentService() {
        return PaymentApp.get().getPaymentService();
    }
}
