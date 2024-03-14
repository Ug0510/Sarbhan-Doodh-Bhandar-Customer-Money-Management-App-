package com.example.doodhbhandar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder2 extends RecyclerView.ViewHolder {

    TextView date_time,product,soldAmount,getAmount;

    public MyViewHolder2(@NonNull View itemView) {
        super(itemView);

        date_time = itemView.findViewById(R.id.date_time);
        product = itemView.findViewById(R.id.product_name);
        soldAmount = itemView.findViewById(R.id.sold_amount);
        getAmount = itemView.findViewById(R.id.taken_amount);
    }
}
