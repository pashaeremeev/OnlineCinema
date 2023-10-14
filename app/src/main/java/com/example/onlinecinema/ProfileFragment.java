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

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ProfileFragment extends Fragment {

    private static final String GREETING_FRAG = "GREETING_FRAG";
    private boolean isAuthButtonPressed = false;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
                if (isAuthButtonPressed) {
                    BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
                    bottomNavView.setEnabled(true);
                    bottomNavView.setVisibility(View.VISIBLE);
                    Fragment greetingFragment = getActivity().
                            getSupportFragmentManager().
                            findFragmentByTag(GREETING_FRAG);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .remove(greetingFragment)
                            .commit();
                    isAuthButtonPressed = false;
                }
            }
        });
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button authButton = view.findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainContainer, new AuthorizationFragment(), GREETING_FRAG)
                        .commit();
                BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
                bottomNavView.setEnabled(false);
                bottomNavView.setVisibility(View.GONE);
                isAuthButtonPressed = true;
            }
        });
    }


}