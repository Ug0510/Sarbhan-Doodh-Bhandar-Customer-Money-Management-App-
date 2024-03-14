package com.example.doodhbhandar.ui.home;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodhbhandar.MyAdapter;
import com.example.doodhbhandar.R;
import com.example.doodhbhandar.SharedPrefsManager;
import com.example.doodhbhandar.User;
import com.example.doodhbhandar.databinding.FragmentHomeBinding;
import com.example.doodhbhandar.ui.dashboard.UserListDashboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    EditText searchBox;
    int give_total = 0;
    int balance_total = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //download report
        LinearLayout download_report = binding.downloadReport;
        download_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadReport();
            }
        });


        //recycler view setup
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


        //floating button
        FloatingActionButton fab = binding.fab;
        fab.setImageResource(R.drawable.add_user);



        // In your Fragment or Activity
        SharedPrefsManager sharedPrefsManager = SharedPrefsManager.getInstance(getContext());
        List<User> users = sharedPrefsManager.getUsers();



        //Setting up Adapter to show user list
        MyAdapter adapter = new MyAdapter(getContext(), users);
        recyclerView.setAdapter(adapter);


        //OnClick function for floating button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), User_info.class);
//                startActivity(intent);
//
                showInputDialog();

            }
        });

        searchBox = binding.editTextSearch;

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBox.setText("");
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Search the list for items that match the user's input
                List<User> searchResults = new ArrayList<>();
                for (User item : users) {
                    if (item.getName().toLowerCase().contains(charSequence.toString().toLowerCase()) ||String.valueOf(item.getPosition()).contains(charSequence.toString())) {
                        searchResults.add(item);
                    }
                }

                // Update the data source for the RecyclerView
                MyAdapter adapter = new MyAdapter(getContext(), searchResults);
                recyclerView.setAdapter(adapter);

            }

            // Other required methods, but not needed for this implementation
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        updateAmounts();

        LinearLayout btn_filter =  binding.btnFilter;
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterthelist();
            }
        });



        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setTitle("Sarbhan Doodh Bhandar");

        return root;
    }

    private void filterthelist() {
        CharSequence[] options = {"Name(A-Z)", "Name(Z-A)", "User Id(New-Old)", "User Id(Old-New)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sort By");
        builder.setItems(options, new DialogInterface.OnClickListener()
        { @Override public void onClick(DialogInterface dialog, int which)
        {
            PureFilter(which);
        }
        });
        builder.show();
    }

    private void PureFilter(int choice) {
        List<User> userList = SharedPrefsManager.getInstance(getContext()).getUsers();

        switch (choice)
        {
            case 0:
                userList.sort(Comparator.comparing(User::getName));
                break;
            case 1:
                userList.sort(Comparator.comparing(User::getName));
                Collections.reverse(userList);
                break;
            case 2:
                userList.sort(Comparator.comparing(User::getPosition));
                break;
            case 3:
                userList.sort(Comparator.comparing(User::getPosition));
                Collections.reverse(userList);
                break;
        }
        recyclerView.setAdapter(new MyAdapter(getContext(),userList));
    }

    private void downloadReport() {
        List<User> list = SharedPrefsManager.getInstance(getContext()).getUsers();
        // Create a new PDF document
        Document document = new Document();
        try {
            // Get the path to the Downloads directory
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

            // Create a new file in the Downloads directory with the current timestamp as the name
            File file = new File(path, "customer_list_" + System.currentTimeMillis() + ".pdf");

            // Create a new PdfWriter to write the document to the file
            PdfWriter.getInstance(document, new FileOutputStream(file));

            // Open the document
            document.open();

            Font bigFont = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD);
            Paragraph bigTitle = new Paragraph("Sarbhan Doodh Bhandar\n", bigFont);
            bigTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(bigTitle);

            // Add a title to the document
            bigFont = new Font(Font.FontFamily.HELVETICA,14,Font.BOLD);
            Paragraph title = new Paragraph("Customers List\n\n" ,bigFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Create a table to hold the customer list
            PdfPTable table = new PdfPTable(4);

            // Add column headings to the table
            table.addCell("Customer Name");
            table.addCell("Phone Number");
            table.addCell("Sold Product Amount");
            table.addCell("Balance Amount");

            // Add each customer in the list to the table
            for (int i = 0; i < list.size(); i++) {
                User customer = list.get(i);
                table.addCell(customer.getName());
                table.addCell(customer.getPhone_number());
                table.addCell("₹ "+String.valueOf(customer.getGive_amount(getContext())));
                table.addCell("₹ "+String.valueOf(customer.getGet_amount(getContext())));
            }

            // Add the table to the document
            document.add(table);

            //Adding Extra free line
            table = new PdfPTable(1);
            table.addCell(" ");
            document.add(table);

            // Showing Balance amount
            table = new PdfPTable(2);

            table.addCell(" ");
            table.addCell("Sold Product Amount Rs. "+give_total);
            table.addCell(" ");
            table.addCell("Pending Amount Rs. "+balance_total);

            document.add(table);

            Toast.makeText(getContext(), "Download Complete", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the document
            document.close();
        }
    }



    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.input_dialog, null);
        builder.setView(dialogView);
        final EditText nameEditText = dialogView.findViewById(R.id.et_name);
        final EditText phoneEditText = dialogView.findViewById(R.id.et_phone);

        Button verifyButton = dialogView.findViewById(R.id.btn_verify);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneEditText.getText().toString();
                phone.trim();
                if(phone.equals(""))
                {
                    Toast.makeText(getContext(), "Enter the Number first", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isValid = validateNumber(phone);
                if (isValid) {
                    verifyButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
                    verifyButton.setText("Number Verified");
                } else {
                    verifyButton.setText("Not Valid Number");
                    verifyButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
                    Toast.makeText(getContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                name.trim();
                phone.trim();

                if(name.equals("") || phone.equals(""))
                {
                    Toast.makeText(getContext(), "Fill Both Section", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(verifyButton.getText() != "Number Verified")
                {
                    Toast.makeText(getContext(), "Verify the number first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "Customer Added Successfully!", Toast.LENGTH_SHORT).show();
                SharedPrefsManager sharedPrefsManager = SharedPrefsManager.getInstance(getContext());
                List<User> users = sharedPrefsManager.getUsers();

                //To check if customer is already present in the list
                for (User item : users) {
                    if (item.getPhone_number().contains(phone)) {
                        Toast.makeText(getContext(), "Customer Already Present", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                name = CapatilizeName(name);
                sharedPrefsManager.addUser(0,new User(name,phone,getContext()));
                users = sharedPrefsManager.getUsers();
                MyAdapter adapter = new MyAdapter(getContext(),users);
                recyclerView.setAdapter(adapter);

            }

            private String CapatilizeName(String str) {
                if (str == null || str.isEmpty()) {
                    return str;
                } else {
                    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private boolean validateNumber(String number)  {
        Pattern p = Pattern.compile("[6-9][0-9]{9}");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    public void updateAmounts(){
        SharedPrefsManager sharedPrefsManager = SharedPrefsManager.getInstance(getContext());
        List<User> users = sharedPrefsManager.getUsers();
        give_total = 0;
        balance_total = 0;

        for(User item : users)
        {
            give_total += item.getGive_amount(getContext());
            balance_total += item.getGet_amount(getContext());
        }
        TextView give_amount = binding.givenAmount;
        TextView balance_amount = binding.balanceAmount;
        give_amount.setText("₹ "+give_total);
        balance_amount.setText("₹ "+balance_total);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void removeEntry(int position){
        Toast.makeText(getContext(),"hi", Toast.LENGTH_SHORT).show();
    }


}

