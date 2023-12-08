package com.example.onlinecinema;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.repos.MovieRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MovieFragment extends Fragment {

    private Movie movie;
    private String tagFragment;
    private static final int SHORT_TEXT_LENGTH = 200;
    private boolean isExpandedText = false;

    public MovieFragment(int movieID, MovieRepo movieRepo) {
        this.movie = movieRepo.getById(movieID);
        this.tagFragment = movieID + "";
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
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
        bottomNavView.setEnabled(false);
        bottomNavView.setVisibility(View.GONE);

        ImageView poster = view.findViewById(R.id.posterView);
        TextView movieName = view.findViewById(R.id.movieName);
        TextView origName = view.findViewById(R.id.origNameView);
        TextView genresView = view.findViewById(R.id.genresView);
        TextView countriesView = view.findViewById(R.id.countryView);
        TextView yearView = view.findViewById(R.id.yearView);
        TextView descText = view.findViewById(R.id.descText);
        ImageView arrowBackBtn = view.findViewById(R.id.arrowBackButtonMovie);
        ImageView hideNshowBtn = view.findViewById(R.id.hideNshowButton);

        String allGenres = genresView.getText().toString() + readGenres();
        String allCountries = countriesView.getText().toString() + readCountries();
        Glide.with(getContext())
                .load(movie.getPoster())
                .error(R.drawable.baseline_no_internet_24)
                .into(poster);
        movieName.setText(movie.getFirstName());
        if (movie.getOrigName() != null) {
            origName.setText(movie.getOrigName());
        }
        genresView.setText(allGenres);
        countriesView.setText(allCountries);
        String year = yearView.getText().toString();
        yearView.setText(year + " " + movie.getYear());
        descText.setText(movie.getDescription().substring(0, SHORT_TEXT_LENGTH));

        arrowBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        hideNshowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExpandedText) {
                    descText.setText(movie.getDescription());
                    isExpandedText = true;
                    hideNshowBtn.setImageResource(R.drawable.baseline_hide_text_24);
                } else {
                    descText.setText(movie.getDescription().substring(0, SHORT_TEXT_LENGTH));
                    isExpandedText = false;
                    hideNshowBtn.setImageResource(R.drawable.baseline_open_full_text_24);
                }
            }
        });
    }

    private String readGenres() {
        String allGenres = " ";
        for (int i = 0; i < movie.getGenres().size(); i++) {
            String genre = movie.getGenres().get(i);
            if (i != movie.getGenres().size() - 1) {
                allGenres += (genre + ", ");
            } else {
                allGenres += (genre + ".");
            }
        }
        return allGenres;
    }

    private String readCountries() {
        String allCountries = " ";
        for (int i = 0; i < movie.getCountries().size(); i++) {
            String genre = movie.getCountries().get(i);
            if (i != movie.getCountries().size() - 1) {
                allCountries += (genre + ", ");
            } else {
                allCountries += (genre + ".");
            }
        }
        return allCountries;
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