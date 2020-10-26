package com.ikhiloya.imokhai.annotationbasedcaching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ikhiloya.imokhai.annotationbasedcaching.adapter.PaymentAdapter;
import com.ikhiloya.imokhai.annotationbasedcaching.di.Injector;
import com.ikhiloya.imokhai.annotationbasedcaching.model.PaymentType;
import com.ikhiloya.imokhai.annotationbasedcaching.service.PaymentService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private PaymentAdapter paymentAdapter;
    private List<PaymentType> paymentTypes;
    private PaymentService paymentService;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paymentService = Injector.providePaymentService();
        paymentTypes = new ArrayList<>();

        RecyclerView paymentRcv = findViewById(R.id.payment_rcv);
        progressBar = findViewById(R.id.progress_bar);

        paymentAdapter = new PaymentAdapter(paymentTypes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(paymentRcv.getContext(),
                linearLayoutManager.getOrientation());

        paymentRcv.setLayoutManager(linearLayoutManager);
        paymentRcv.addItemDecoration(dividerItemDecoration);
        paymentRcv.setAdapter(paymentAdapter);


        if (PaymentApp.hasNetwork()) {
            getPaymentTypes();
        } else {
            Toast.makeText(this, getResources().getString(R.string.network_unavailable_msg), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.refresh) {
            getPaymentTypes();
        }
        return super.onOptionsItemSelected(item);
    }


    private void getPaymentTypes() {
        progressBar.setVisibility(View.VISIBLE);
        paymentService.getPaymentTypes().enqueue(new Callback<List<PaymentType>>() {
            @Override
            public void onResponse(@NotNull Call<List<PaymentType>> call, @NotNull Response<List<PaymentType>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {


                    if (response.raw().networkResponse() != null) {
                        Timber.i("onResponse: response is from NETWORK...");
                    } else if (response.raw().cacheResponse() != null
                            && response.raw().networkResponse() == null) {
                        Timber.i("onResponse: response is from CACHE...");
                    }

                    List<PaymentType> data = response.body();
                    Timber.i("Data from network%s", data.toString());
                    progressBar.setVisibility(View.GONE);
                    paymentTypes.clear();
                    paymentTypes.addAll(data);
                    paymentAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<PaymentType>> call, @NotNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Timber.e(t, getString(R.string.error_occurred));

            }
        });
    }
}
