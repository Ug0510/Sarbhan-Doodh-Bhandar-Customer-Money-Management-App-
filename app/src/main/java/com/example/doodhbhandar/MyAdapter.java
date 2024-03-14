package com.example.doodhbhandar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodhbhandar.ui.home.HomeFragment;
import com.google.gson.Gson;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<User> users;

    public MyAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.user_view,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder.customer_name != null) {
            int maxChars = 15;
            String name = users.get(position).getName();
            if(name.length() > maxChars)
                holder.customer_name.setText(name.substring(0,maxChars) + "...");
            else
                holder.customer_name.setText(name);
            int amount = users.get(position).getGet_amount(context);
            if(amount > 0)
                holder.amount.setText("₹ "+amount);
            else
            {
                holder.amount.setText("₹ "+Math.abs(amount));
                holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green));
            }
        }
        holder.user_index.setText(""+users.get(position).getPosition());
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPrefsManager.getInstance(v.getContext()).removeUser(position);
                                        notifyDataSetChanged();
                                    }
                                })
                         .setNegativeButton("Cancel",null)
                          .show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,User_info.class);
                intent.putExtra("selectedUser",users.get(position).getName());
                intent.putExtra("selectedNumber",users.get(position).getPhone_number());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
