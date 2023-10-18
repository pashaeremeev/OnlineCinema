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
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AuthFragment extends Fragment {

    private static final String REG_FRAG = "REG_FRAG";
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
                BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
                bottomNavView.setEnabled(true);
                bottomNavView.setVisibility(View.VISIBLE);
                Fragment greetingFragment = getActivity().
                        getSupportFragmentManager()
                        .findFragmentByTag(tagFragment);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .remove(greetingFragment)
                        .commit();
            }
        });
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button authButton = view.findViewById(R.id.crossToRegButton);
        authButton.setOnClickListener(new View.OnClickListener() {
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
        ImageView arrowBackBtn = view.findViewById(R.id.arrowBackButtonAuth);
        arrowBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}