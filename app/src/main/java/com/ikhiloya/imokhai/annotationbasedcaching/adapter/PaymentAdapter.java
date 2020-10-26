package com.ikhiloya.imokhai.annotationbasedcaching.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikhiloya.imokhai.annotationbasedcaching.R;
import com.ikhiloya.imokhai.annotationbasedcaching.model.PaymentType;

import java.util.List;


public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private List<PaymentType> paymentTypes;


    public PaymentAdapter(List<PaymentType> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.payment_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(paymentTypes.get(position));
    }


    @Override
    public int getItemCount() {
        return paymentTypes != null ? paymentTypes.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView paymentNameTxt;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentNameTxt = itemView.findViewById(R.id.payment_name_txt);
        }

        @SuppressLint("SetTextI18n")
        void bind(final PaymentType paymentType) {
            paymentNameTxt.setText(paymentType.getName());
        }
    }
}
