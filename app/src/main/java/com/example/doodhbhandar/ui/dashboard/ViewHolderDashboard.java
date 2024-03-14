package com.example.doodhbhandar.ui.dashboard;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodhbhandar.R;

public class ViewHolderDashboard extends RecyclerView.ViewHolder {

    TextView customer_name, date,soldAmount,takenAmount;

    public ViewHolderDashboard(@NonNull View itemView) {
        super(itemView);
        customer_name = itemView.findViewById(R.id.name_dashboard);
        date = itemView.findViewById(R.id.date_time_dashboard);
        soldAmount = itemView.findViewById(R.id.sold_amount_dashboard);
        takenAmount = itemView.findViewById(R.id.taken_amount_dashboard);
    }
}
