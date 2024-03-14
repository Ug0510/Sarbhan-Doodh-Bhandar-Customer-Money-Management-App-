package com.example.doodhbhandar;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefsManager {

    private static SharedPrefsManager instance;
    private static final String SHARED_PREFS_NAME = "SHARED_PREFS";
    private static final String KEY_USERS = "KEY_USERS";

    private SharedPreferences sharedPreferences;
    private List<User> users;

    private SharedPrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        loadUsers();
    }
    public static synchronized SharedPrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsManager(context);
        }
        return instance;
    }
    private void loadUsers() {
        users = new ArrayList<>();
        String usersJson = sharedPreferences.getString(KEY_USERS, null);
        if (usersJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<User>>(){}.getType();
            users = gson.fromJson(usersJson, type);
        }
    }
    public void addUser(int i, User user) {
        users.add(0,user);
        saveUsers();
    }
    private void saveUsers() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String usersJson = gson.toJson(users);
        editor.putString(KEY_USERS, usersJson);
        editor.apply();
    }
    public void removeUser(int position)
    {
        users.remove(position);
        saveUsers();
    }
    public List<User> getUsers() {
        return users;
    }
}

