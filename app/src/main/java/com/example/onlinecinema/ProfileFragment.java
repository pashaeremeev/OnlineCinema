package com.example.onlinecinema;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.repos.MovieRepo;
import com.example.onlinecinema.repos.PreferencesRepo;
import com.example.onlinecinema.repos.UserRepo;
import com.example.onlinecinema.requests.DownloadFavMovies;
import com.example.onlinecinema.requests.RestClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.RequestBody;


public class ProfileFragment extends Fragment {

    private static final String AUTH_FRAG = "AUTH_FRAG";
    private static final String BLOCK_FRAG = "BLOCK_FRAG";
    private static final String ADM_FRAG = "ADM_FRAG";
    private UserRepo userRepo;
    private MovieRepo movieRepo;
    private LiveData<User> liveData;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        liveData = UserRepo.getCurrentUser();
        TextView accountName = view.findViewById(R.id.accountName);
        TextView extraInfoAboutAcc = view.findViewById(R.id.warningMsg);
        Button exitButton = view.findViewById(R.id.exitAccButton);
        Button authButton = view.findViewById(R.id.enterAccButton);
        Button unblockNblockButton = view.findViewById(R.id.unblockNblockButton);
        Button appointAdminButton = view.findViewById(R.id.appointAdminButton);
        userRepo = new UserRepo(getContext());
        movieRepo = new MovieRepo();
        liveData.observe(getViewLifecycleOwner(), user -> {
            if (!userRepo.isGuest()) {
                accountName.setText(user.getUsername());
                extraInfoAboutAcc.setText("id: " + user.getId());
                authButton.setVisibility(View.GONE);
                exitButton.setVisibility(View.VISIBLE);
                if (user.getIdRole() == 1) {
                    unblockNblockButton.setVisibility(View.VISIBLE);
                    appointAdminButton.setVisibility(View.VISIBLE);
                }
            } else {
                accountName.setText(getResources().getString(R.string.profile_item));
                extraInfoAboutAcc.setText(getResources().getString(R.string.warning_msg_acc));
                authButton.setVisibility(View.VISIBLE);
                exitButton.setVisibility(View.GONE);
                unblockNblockButton.setVisibility(View.GONE);
                appointAdminButton.setVisibility(View.GONE);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button authButton = view.findViewById(R.id.enterAccButton);
        Button exitButton = view.findViewById(R.id.exitAccButton);
        Button unblockNblockButton = view.findViewById(R.id.unblockNblockButton);
        Button appointAdminButton = view.findViewById(R.id.appointAdminButton);

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

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRepo.deleteUser();
                if (userRepo.isGuest()) {
                    movieRepo.setFavMovies(new ArrayList<>());
                }
            }
        });

        unblockNblockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainContainer, new BlockFragment(BLOCK_FRAG), BLOCK_FRAG)
                        .commit();
                BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
                bottomNavView.setEnabled(false);
                bottomNavView.setVisibility(View.GONE);
            }
        });

        appointAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainContainer, new AdminFragment(ADM_FRAG), ADM_FRAG)
                        .commit();
                BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
                bottomNavView.setEnabled(false);
                bottomNavView.setVisibility(View.GONE);
            }
        });
    }
}