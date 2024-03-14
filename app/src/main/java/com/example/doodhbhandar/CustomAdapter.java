package com.example.doodhbhandar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<MyViewHolder2> {

    Context context;
    List<User_history> user_histories;

    public CustomAdapter(Context context, List<User_history> user_histories) {
        this.context = context;
        this.user_histories = user_histories;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder2(LayoutInflater.from(context).inflate(R.layout.entry_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {

        holder.product.setText("Product: "+user_histories.get(position).getProduct_name());

        int price = user_histories.get(position).getSold_amount();
        if(price != 0)
                holder.soldAmount.setText("₹ "+price);
        else
            holder.soldAmount.setText("");

        price = user_histories.get(position).getTaken_amount();
        if(price != 0)
            holder.getAmount.setText("₹ "+price);
        else
            holder.getAmount.setText("");

        holder.date_time.setText(user_histories.get(position).getTransaction_date_time());

    }

    @Override
    public int getItemCount() {
        return user_histories.size();
    }
}
