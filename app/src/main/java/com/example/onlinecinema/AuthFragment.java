package com.example.onlinecinema;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.repos.PreferencesRepo;
import com.example.onlinecinema.repos.UserRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AuthFragment extends Fragment {

    private static final String REG_FRAG = "REG_FRAG";
    private static final String IS_GUEST = "IS_GUEST";
    private static final String CURRENT_ID = "CURRENT_ID";
    private String tagFragment;

    public AuthFragment(String tagFragment) {
        this.tagFragment = tagFragment;
    }

    public static AuthFragment newInstance(String tagFragment) {
        AuthFragment fragment = new AuthFragment(tagFragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                removeFragment();
            }
        });
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button crossToRegButton = view.findViewById(R.id.crossToRegButton);
        ImageView arrowBackBtn = view.findViewById(R.id.arrowBackButtonAuth);
        Button authButton = view.findViewById(R.id.authButton);
        EditText userNameField = view.findViewById(R.id.editAuthLogin);
        EditText passwordField = view.findViewById(R.id.editAuthPassword);
        crossToRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainContainer, new RegFragment(REG_FRAG), REG_FRAG)
                        .commit();
                BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
                bottomNavView.setEnabled(false);
                bottomNavView.setVisibility(View.GONE);
            }
        });
        arrowBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRepo userRepo = new UserRepo(getContext());
                ArrayList<User> users = userRepo.getUsers();
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (userNameField.getText().toString().equals(user.getUsername())
                            && passwordField.getText().toString().equals(user.getPassword())) {
                        Toast.makeText(getContext(), "Вы авторизованы!", Toast.LENGTH_SHORT).show();
                        PreferencesRepo preferencesRepo = new PreferencesRepo(getContext());
                        preferencesRepo.save(false, IS_GUEST);
                        preferencesRepo.save(user.getId(), CURRENT_ID);
                        userRepo.setUser(user);
                        removeFragment();
                    }
                }
            }
        });
    }

    private void removeFragment() {
        BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
        bottomNavView.setEnabled(true);
        bottomNavView.setVisibility(View.VISIBLE);
        Fragment fragment = getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(tagFragment);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }
}