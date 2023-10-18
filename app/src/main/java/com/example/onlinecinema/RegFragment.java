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
        return inflater.inflate(R.layout.fragment_reg, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button authButton = view.findViewById(R.id.crossToAuthButton);
        authButton.setOnClickListener(new View.OnClickListener() {
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
        ImageView arrowBackBtn = view.findViewById(R.id.arrowBackButtonReg);
        arrowBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}