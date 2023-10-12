package com.example.onlinecinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setOnItemSelectedListener(this::onNavigationItemSelected);
        //navController = Navigation.findNavController(this, R.id.fragmentContainer);
        //NavigationUI.setupWithNavController(bottomNavView, navController);
        replaceFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main) {
            replaceFragment(new HomeFragment());
            //navController.navigate(id);
            return true;
        } else if (id == R.id.search) {
            replaceFragment(new SearchFragment());
            //navController.navigate(id);
            return true;
        } else if (id == R.id.fav) {
            replaceFragment(new FavFragment());
            //navController.navigate(id);
            return true;
        } else if (id == R.id.profile) {
            replaceFragment(new ProfileFragment());
            //navController.navigate(id);
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

    private void updateNavigationBarState(int actionId) {
        Menu menu = bottomNavView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == actionId);
        }
    }

}