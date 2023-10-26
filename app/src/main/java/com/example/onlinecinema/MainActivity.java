package com.example.onlinecinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.repos.PreferencesRepo;
import com.example.onlinecinema.repos.UserRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavView;
    private static final String IS_GUEST = "IS_GUEST";
    private static final String CURRENT_ID = "CURRENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setOnItemSelectedListener(this::onNavigationItemSelected);
        bottomNavView.setSelectedItemId(R.id.main);

        PreferencesRepo preferencesRepo = new PreferencesRepo(getBaseContext());
        boolean isGuest = preferencesRepo.getBoolean(IS_GUEST);
        if (!isGuest) {
            UserRepo userRepo = new UserRepo(getBaseContext());
            int currentIdUser = preferencesRepo.getInt(CURRENT_ID);
            User currentUser = userRepo.getById(currentIdUser);
            userRepo.setUser(currentUser);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main) {
            replaceFragment(new HomeFragment());
            return true;
        } else if (id == R.id.search) {
            replaceFragment(new SearchFragment());
            return true;
        } else if (id == R.id.fav) {
            replaceFragment(new FavFragment());
            return true;
        } else if (id == R.id.profile) {
            replaceFragment(new ProfileFragment());
            return true;
        }
        return false;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}