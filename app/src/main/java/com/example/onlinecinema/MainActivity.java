package com.example.onlinecinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.repos.MovieRepo;
import com.example.onlinecinema.repos.UserRepo;
import com.example.onlinecinema.requests.DownloadFavMovies;
import com.example.onlinecinema.requests.DownloadMoviesFromDB;
import com.example.onlinecinema.requests.DownloadMoviesFromService;
import com.example.onlinecinema.requests.DownloadUserFromDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavView;
    private UserRepo userRepo;
    private MovieRepo movieRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setOnItemSelectedListener(this::onNavigationItemSelected);
        bottomNavView.setSelectedItemId(R.id.main);

        userRepo = new UserRepo(getBaseContext());
        //PreferencesRepo p = new PreferencesRepo(getBaseContext());
        //p.save(true, "IS_GUEST");
        if (!userRepo.isGuest()) {
            User currentUser = userRepo.getLastUser();
            userRepo.setUser(currentUser);
            if (currentUser != null) {
                DownloadUserFromDB.downloadMovies(userRepo);
            }
        }

        movieRepo = new MovieRepo();
        DownloadMoviesFromService.downloadMovies(movieRepo);
        DownloadMoviesFromDB.downloadMovies(movieRepo);
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