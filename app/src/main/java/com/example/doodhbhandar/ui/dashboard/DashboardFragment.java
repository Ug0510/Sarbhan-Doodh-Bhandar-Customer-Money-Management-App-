package com.example.doodhbhandar.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodhbhandar.SharedPrefsManager;
import com.example.doodhbhandar.User;
import com.example.doodhbhandar.User_history;
import com.example.doodhbhandar.databinding.FragmentDashboardBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //Merging all userlists to get the final list
        List<UserListDashboard> userList = mergeLists();

        RecyclerView recyclerView = binding.recyclerViewDashboard;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdapterDashboard(getContext(),userList));


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setTitle("Sarbhan Doodh Bhandar");
        return root;
    }

    private List<UserListDashboard> mergeLists() {
        List<UserListDashboard> list = new ArrayList<UserListDashboard>();
        List<User> userList= SharedPrefsManager.getInstance(getContext()).getUsers();
        List<User_history> historyList = new ArrayList<User_history>();
        for(User item : userList)
        {
            String name = item.getName();
            String number = item.getPhone_number();

            historyList = getSharedList(number);
            for(User_history item2 : historyList)
            {
                list.add(new UserListDashboard(name,item2.getTransaction_date_time(),""+item2.getSold_amount(),""+item2.getTaken_amount()));
            }
        }
        list = sortList(list);
        return list;
    }

    public static List<UserListDashboard> sortList(List<UserListDashboard> list) {
        // Define a comparator that compares UserListDashboard objects based on their date fields
        Comparator<UserListDashboard> comparator = Comparator.comparing(dashboards -> {
            // Parse the date string into a LocalDateTime object using a formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy, hh:mm a");
            LocalDateTime date = LocalDateTime.parse(dashboards.getDate(), formatter);
            return date;
        });

        // Sort the list using the comparator
        Collections.sort(list, comparator);
        Collections.reverse(list);
        return list;
    }

    private List<User_history> getSharedList(String number) {
        List<User_history> user_histories = new ArrayList<User_history>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User_pref"+number, Context.MODE_PRIVATE);
        String userHistoriesJson = sharedPreferences.getString("user_histories", null);
        if (userHistoriesJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<User_history>>(){}.getType();
            user_histories = gson.fromJson(userHistoriesJson, type);
        }
        return user_histories;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}