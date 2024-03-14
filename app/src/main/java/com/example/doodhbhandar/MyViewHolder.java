package com.example.doodhbhandar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder  {

    TextView customer_name,amount,user_index;
    ImageView btn_remove;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        user_index = itemView.findViewById(R.id.user_index);
        customer_name = itemView.findViewById(R.id.name);
        amount = itemView.findViewById(R.id.amount_of_customer);
        btn_remove = itemView.findViewById(R.id.btn_remove);

    }
}
