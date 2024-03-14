package com.example.doodhbhandar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class User_info extends AppCompatActivity {

    TextView user_name;
    ImageView back_btn,sms_btn;
    private String SHARED_PREFS_NAME = "User_pref";
    private String KEY_USER_HISTORIES = "user_histories";
    RecyclerView recyclerView;
    String Name,Number;
    int Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_user_info);

        user_name = findViewById(R.id.customer_name);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String data = extras.getString("selectedUser");
            Name = data;
            user_name.setText(data);
            data = extras.getString("selectedNumber");
            Number = data;
            SHARED_PREFS_NAME +=data;
        }

        //Setting up back button
        back_btn = findViewById(R.id.layout_1);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(User_info.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        //setting up sms button
        sms_btn = findViewById(R.id.message_alert);

        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = extras.getString("selectedUser");
                String number = extras.getString("selectedNumber");
                SendSMS.sentIt(name,"917817990855");
            }
        });


        recyclerView = findViewById(R.id.recycler_view2);
        List<User_history> user_histories = new ArrayList<User_history>();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String userHistoriesJson = sharedPreferences.getString(KEY_USER_HISTORIES, null);
        if (userHistoriesJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<User_history>>(){}.getType();
            user_histories = gson.fromJson(userHistoriesJson, type);
        }
        updateTotal(user_histories);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new CustomAdapter(getApplicationContext(),user_histories));

        Button btn_milk,btn_curd,btn_other;
        btn_milk = findViewById(R.id.btn_milk);
        btn_curd = findViewById(R.id.btn_curd);
        btn_other = findViewById(R.id.btn_others);
        btn_milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewEntry("Milk");
            }
        });
        btn_curd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewEntry("Curd");
            }
        });
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewEntry("Other");
            }
        });

        ImageView pdf_download = findViewById(R.id.pdf_download);
        pdf_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadList();
            }
        });

        ImageView messageAlert = findViewById(R.id.message_alert);

        messageAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });

        ImageView btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetConfirmation();
            }
        });

    }
    public void resetConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(User_info.this);
        builder.setMessage("Are you sure you want to reset all the transactions?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset the transaction here
                resetTransactions();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void resetTransactions() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this, "Transactions Deleted Succesfully", Toast.LENGTH_SHORT).show();
    }

    private void sendSMS() {
        ActivityCompat.requestPermissions(User_info.this,new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        String message="";
        if(Total==0)
        {
            Toast.makeText(this, "Amount is Rs.0 , No need to send Sms", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if(Total > 0)
                message = "Dear Sir/Madam,\n Amount of Rs."+Total+" is pending to pay at Sarbhan Doodh Bhandar.\n Thank You";
            else
                message = "Dear Sir/Madam,\n You have Submitted Extra Amount of Rs. "+Math.abs(Total)+" at Sarbhan Doodh Bhandar.\n Thank You";

            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(Number,null,message,null,null);

            Toast.makeText(User_info.this, "Message Succesfully Sent", Toast.LENGTH_SHORT).show();
        }
 }

    private void confirmationSms(int takenAmount) {
        ActivityCompat.requestPermissions(User_info.this,new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        String message;

        if(Total > 0)
            message = "You have Paid Rs."+takenAmount+" And You have a Pending amount of Rs."+Total+" at Sarbhan Doodh Bhandar.\nThank you.";
        else if(Total == 0)
            message = "You have Paid Rs."+takenAmount+" And You have No Pending amount at Sarbhan Doodh Bhandar.\nThank you.";
        else
            message = "You have Paid Rs."+takenAmount+" And You have extra amount of Rs."+Math.abs(Total)+" at Sarbhan Doodh Bhandar.\nThank you.";
        SmsManager mySmsManager = SmsManager.getDefault();
        mySmsManager.sendTextMessage(Number,null,message,null,null);

    }

    private void downloadList() {
        // Get the list of user histories from SharedPreferences
        List<User_history> user_histories = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String userHistoriesJson = sharedPreferences.getString(KEY_USER_HISTORIES, null);
        if (userHistoriesJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<User_history>>(){}.getType();
            user_histories = gson.fromJson(userHistoriesJson, type);
        }
        printList(user_histories);
    }

    private void printList(List<User_history> user_history) {
        // Create a new PDF document
        Document document = new Document();

        try {
            // Get the path to the Downloads directory
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

            // Create a new file in the Downloads directory with the current timestamp as the name
            File file = new File(path, Name + System.currentTimeMillis() + ".pdf");

            // Create a new PdfWriter to write the document to the file
            PdfWriter.getInstance(document, new FileOutputStream(file));

            // Open the document
            document.open();

            // Create a big heading
            Font bigFont = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD);
            Paragraph bigTitle = new Paragraph("Sarbhan Doodh Bhandar\n", bigFont);
            bigTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(bigTitle);


            // Add a title to the document
            bigFont = new Font(Font.FontFamily.HELVETICA,14,Font.BOLD);
            Paragraph title = new Paragraph("Customer Name: " +Name+"     "+"Phone Number: "+Number+"\n\n" ,bigFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Create a table to hold the user history list
            PdfPTable table = new PdfPTable(5);

            // Add column headings to the table
            table.addCell("Product Name");
            table.addCell("Date");
            table.addCell("Time");
            table.addCell("Sold Product Amount");
            table.addCell("Taken Amount");

            // Add each user history in the list to the table
            for (int i = 0; i < user_history.size(); i++) {
                User_history userHistory = user_history.get(i);
                table.addCell(userHistory.getProduct_name());
                String date_time = userHistory.getTransaction_date_time();
                table.addCell(date_time.substring(0,9));
                table.addCell(date_time.substring(10));
                table.addCell(String.valueOf(userHistory.getSold_amount()));
                table.addCell(String.valueOf(userHistory.getTaken_amount()));
            }

            // Add the table to the document
            document.add(table);

            //Adding Extra free line
            table = new PdfPTable(1);
            table.addCell(" ");
            document.add(table);

            // Showing Balance amount
            table = new PdfPTable(2);

            if(Total>=0)
            {
                table.addCell(" ");
                table.addCell("Balance Amount Rs. "+Total);
            }
            else
            {
                table.addCell(" ");
                table.addCell("Extra Amount Rs. "+Math.abs(Total));
            }
            document.add(table);

            Toast.makeText(this, "Download Succesfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the document
            document.close();
        }

    }

    private void updateTotal(List<User_history> user_histories) {

        int sum = 0;
        int sum2 = 0;

        for(User_history item: user_histories)
        {
            sum += item.getSold_amount();
            sum2 += item.getTaken_amount();
        }
        Total = sum-sum2;
        TextView amtStatus = findViewById(R.id.amountStatus);
        TextView amtValue = findViewById(R.id.amount_value);
        if((Total)>=0)
        {
            amtStatus.setText("Balance Amount");
            amtValue.setText("₹ "+Total);
            amtStatus.setTextColor(getColor(R.color.red));
            amtValue.setTextColor(getColor(R.color.red));
        }
        else
        {
            amtStatus.setText("Extra Amount");
            amtValue.setText("₹ "+Math.abs(Total));
            amtStatus.setTextColor(getColor(R.color.green));
            amtValue.setTextColor(getColor(R.color.green));
        }


    }

    private void makeNewEntry(String product) {
        // Create a new dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.product_dialog_form);

        // Find EditTexts in the dialog
        EditText etProductName = dialog.findViewById(R.id.et_product_name);
        etProductName.setText(product);
        EditText etSoldAmount = dialog.findViewById(R.id.et_sold_amount);
        EditText etTakenAmount = dialog.findViewById(R.id.et_taken_amount);

        // Find buttons in the dialog
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);

        // Set click listener for cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Set click listener for submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditTexts
                String productName = etProductName.getText().toString();
                int soldAmount, takenAmount;
                String soldText = etSoldAmount.getText().toString();
                String takenText = etTakenAmount.getText().toString();
                boolean bool = false;
                if (soldText.isEmpty()) {
                    soldAmount = 0;
                } else {
                    soldAmount = Integer.parseInt(soldText);
                    bool = true;
                }

                if (takenText.isEmpty()) {
                    takenAmount = 0;
                } else {
                    takenAmount = Integer.parseInt(takenText);
                    bool = true;
                }

                if(bool == false)
                {
                    Toast.makeText(User_info.this, "Atleast one amount required", Toast.LENGTH_SHORT).show();
                    return;
                }



                // Get the list of user histories from SharedPreferences
                List<User_history> user_histories = new ArrayList<>();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
                String userHistoriesJson = sharedPreferences.getString(KEY_USER_HISTORIES, null);
                if (userHistoriesJson != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<User_history>>(){}.getType();
                    user_histories = gson.fromJson(userHistoriesJson, type);
                }

                // Add a new user history to the list
                user_histories.add(0,new User_history(soldAmount, takenAmount, productName));

                // Save the list of user histories to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                userHistoriesJson = gson.toJson(user_histories);
                editor.putString(KEY_USER_HISTORIES, userHistoriesJson);
                editor.apply();

                recyclerView.setAdapter(new CustomAdapter(getApplicationContext(),user_histories));
                updateTotal(user_histories);
                if(takenAmount !=0)
                    confirmationSms(takenAmount);
                else if(soldAmount != 0)
                    BalanceSms(soldAmount);
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        // Show the dialog
        dialog.show();
    }

    private void BalanceSms(int soldAmount) {
        String message = "Dear Sir/Madam,\nYou have made a purchase of Rs."+soldAmount+" at Sarbhan Doodh Bhandar";
        if(Total > 0)
            message += "\n Amount of Rs."+Total+" is pending to pay \n Thank You";
        else
            message += "\n You have Extra Amount of Rs. "+Math.abs(Total)+".\n Thank You";
        SmsManager mySmsManager = SmsManager.getDefault();
        mySmsManager.sendTextMessage(Number,null,message,null,null);

    }


}