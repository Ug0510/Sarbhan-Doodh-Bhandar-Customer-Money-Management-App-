package com.example.doodhbhandar;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class User {

    String name;
    String phone_number;
    int get_amount;
    int give_amount;
    static int unique_list;

    public int getPosition() {
        return position;
    }

    int position;

    public User(String name, String phone_number,Context context) {
        this.name = name;
        this.phone_number = phone_number;

        SharedPreferences sharedPreferences = context.getSharedPreferences("User_pref64", Context.MODE_PRIVATE);
        unique_list = sharedPreferences.getInt("unique_list", 1000);
        position = unique_list;
        unique_list++;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("unique_list", unique_list);
        editor.apply();
    }

    public int getAmount()
    {
        return give_amount - get_amount;
    }
    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int getGet_amount(Context context) {


        // Get the list of user histories from SharedPreferences
        List<User_history> user_histories = new ArrayList<>();
        SharedPreferences sharedPreferences =  context.getSharedPreferences("User_pref"+phone_number, Context.MODE_PRIVATE);
        String userHistoriesJson = sharedPreferences.getString("user_histories", null);
        if (userHistoriesJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<User_history>>(){}.getType();
            user_histories = gson.fromJson(userHistoriesJson, type);
        }
        int sum = 0,sum2 = 0;
        for(User_history item :user_histories)
        {
           sum += item.getSold_amount();
           sum2 += item.getTaken_amount();
        }
        setGive_amount(sum);
        setGet_amount(sum2);
        return sum - sum2;
    }

    public int getGive_amount(Context context) {

        // Get the list of user histories from SharedPreferences
        List<User_history> user_histories = new ArrayList<>();
        SharedPreferences sharedPreferences =  context.getSharedPreferences("User_pref"+phone_number, Context.MODE_PRIVATE);
        String userHistoriesJson = sharedPreferences.getString("user_histories", null);
        if (userHistoriesJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<User_history>>(){}.getType();
            user_histories = gson.fromJson(userHistoriesJson, type);
        }
        int sum = 0;
        for(User_history item :user_histories)
        {
            sum += item.getSold_amount();
        }
        setGive_amount(sum);
        return give_amount;
    }

    public void setGet_amount(int get_amount) {
        this.get_amount = get_amount;
    }

    public void setGive_amount(int give_amount) {
        this.give_amount = give_amount;
    }


}
