package com.example.onlinecinema.repos;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.onlinecinema.entities.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserRepo {

    private static final String KEY_USER = "KEY_USER";
    private static final String IS_GUEST = "IS_GUEST";
    private static final String CURRENT_ID = "CURRENT_ID";
    private static final String LAST_USER = "LAST_USER";

    private PreferencesRepo preferencesRepo;
    private static MutableLiveData<User> currentUser = new MutableLiveData<>(null);

    public UserRepo(Context context) {
        this.preferencesRepo = new PreferencesRepo(context);
        getUsers();
    }

    public void saveUsers(ArrayList<User> userList) {
        String userListJson = new Gson().toJson(userList);
        preferencesRepo.save(userListJson, KEY_USER);
    }

    public ArrayList<User> getUsers() {
        String userListJson = preferencesRepo.get(KEY_USER);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        ArrayList<User> userList = new Gson().fromJson(userListJson, type);
        return (userList == null) ? new ArrayList<>() : userList;
    }

    public User getById(Integer id) {
        ArrayList<User> userList = getUsers();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public static LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void setUser(User user) {
        currentUser.postValue(user);
        String userJson = new Gson().toJson(user);
        preferencesRepo.save(userJson, LAST_USER);
        //preferencesRepo.save(user.getId(), CURRENT_ID);
        preferencesRepo.save(false, IS_GUEST);
    }

    public User getLastUser() {
        String userJson = preferencesRepo.get(LAST_USER);
        return new Gson().fromJson(userJson, User.class);
    }

    public void deleteUser() {
        currentUser.postValue(null);
        //preferencesRepo.save(0, CURRENT_ID);
        preferencesRepo.save("null", LAST_USER);
        preferencesRepo.save(true, IS_GUEST);
    }

    public boolean isGuest() {
        return preferencesRepo.getBoolean(IS_GUEST);
    }

    public int getCurrentIdUser() {
        return preferencesRepo.getInt(CURRENT_ID);
    }
}
