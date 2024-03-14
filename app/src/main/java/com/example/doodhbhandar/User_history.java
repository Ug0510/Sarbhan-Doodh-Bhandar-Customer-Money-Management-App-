package com.example.doodhbhandar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User_history {

    private String transaction_date_time;
    private int sold_amount;
    private int taken_amount;
    private String product_name;

    public User_history(int sold_amount, int taken_amount, String product_name) {
        this.sold_amount = sold_amount;
        this.taken_amount = taken_amount;
        this.product_name = product_name;

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        // Format the date and time as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy, hh:mm a");
        transaction_date_time = dateFormat.format(date);
    }

    public String getTransaction_date_time() {
        return transaction_date_time;
    }

    public int getSold_amount() {
        return sold_amount;
    }

    public int getTaken_amount() {
        return taken_amount;
    }

    public String getProduct_name() {
        return product_name;
    }


}
