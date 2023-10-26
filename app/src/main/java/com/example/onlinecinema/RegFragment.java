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
import com.example.onlinecinema.repos.UserRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class RegFragment extends Fragment {

    private static final String AUTH_FRAG = "AUTH_FRAG";
    private String tagFragment;

    public RegFragment(String tagFragment) {
        this.tagFragment = tagFragment;
    }

    public static RegFragment newInstance(String tagFragment) {
        RegFragment fragment = new RegFragment(tagFragment);
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
        return inflater.inflate(R.layout.fragment_reg, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button crossToAuthButton = view.findViewById(R.id.crossToAuthButton);
        ImageView arrowBackBtn = view.findViewById(R.id.arrowBackButtonReg);
        Button regButton = view.findViewById(R.id.regButton);
        EditText userNameField = view.findViewById(R.id.editRegLogin);
        EditText passwordField = view.findViewById(R.id.editRegPassword);
        crossToAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainContainer, new AuthFragment(AUTH_FRAG), AUTH_FRAG)
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
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameField.getText().toString();
                String password = passwordField.getText().toString();
                UserRepo userRepo = new UserRepo(getContext());
                ArrayList<User> users = userRepo.getUsers();
                users.add(new User(userName, password));
                userRepo.saveUsers(users);
                Toast.makeText(getContext(), "Вы зарегистрировались!", Toast.LENGTH_SHORT).show();
                removeFragment();
            }
        });
    }

    public void removeFragment() {
        BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
        bottomNavView.setEnabled(true);
        bottomNavView.setVisibility(View.VISIBLE);
        Fragment fragment = getActivity().
                getSupportFragmentManager()
                .findFragmentByTag(tagFragment);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }
}