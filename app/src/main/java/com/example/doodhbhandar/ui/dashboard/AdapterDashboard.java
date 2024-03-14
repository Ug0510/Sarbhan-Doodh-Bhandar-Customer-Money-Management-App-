package com.example.doodhbhandar.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodhbhandar.R;

import java.util.List;

public class AdapterDashboard extends RecyclerView.Adapter<ViewHolderDashboard> {


    Context context;
    List<UserListDashboard> userListDashboard;

    public AdapterDashboard(Context context, List<UserListDashboard> userListDashboard) {
        this.context = context;
        this.userListDashboard = userListDashboard;
    }

    @NonNull
    @Override
    public ViewHolderDashboard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderDashboard(LayoutInflater.from(context).inflate(R.layout.dashboard_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDashboard holder, int position) {
        holder.customer_name.setText(userListDashboard.get(position).getName());
        holder.date.setText(userListDashboard.get(position).getDate());
        holder.soldAmount.setText("₹ "+userListDashboard.get(position).getSoldAmount());
        holder.takenAmount.setText("₹ "+userListDashboard.get(position).getTakenAmount());

    }

    @Override
    public int getItemCount() {
        return userListDashboard.size();
    }
}
