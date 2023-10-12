package com.example.onlinecinema;

import android.os.Bundle;

import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NoRezSearchFragment extends Fragment {

    public NoRezSearchFragment() {
        // Required empty public constructor
    }

    public static NoRezSearchFragment newInstance(String param1, String param2) {
        NoRezSearchFragment fragment = new NoRezSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_no_rez_search, container, false);
        Button noRezButton = root.findViewById(R.id.noRezButton);
        View activity = inflater.inflate(R.layout.activity_main, container, false);
        noRezButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomNavigationView bottomNavView = activity.findViewById(R.id.bottomNavView);
                //bottomNavView.setSelectedItemId(R.id.main);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentContainer, new HomeFragment());
//                fragmentTransaction.commit();
                bottomNavView.setSelectedItemId(R.id.main);
                noRezButton.setText((bottomNavView.getSelectedItemId() == R.id.main) + "");
            }
        });
        return root;
    }
}