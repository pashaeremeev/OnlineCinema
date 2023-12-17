package com.example.onlinecinema;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.entities.SettingsOfFilter;
import com.example.onlinecinema.repos.MovieRepo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@UnstableApi public class SearchFragment extends Fragment {

    private static final String FLT_FRAG = "FLT_FRAG";
    private static final String FLT_KEY = "FLT_KEY";
    private ArrayList<Movie> rezSearchMovies;
    private MyFavMoviesAdapter adapter;
    private SettingsOfFilter settings;

    public SearchFragment() {
        this.rezSearchMovies = new ArrayList();
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_search, container, false);

        ImageView searchButton = fragment.findViewById(R.id.searchButton);
        ImageView filterButton = fragment.findViewById(R.id.filterButton);
        EditText searchEditField = fragment.findViewById(R.id.searchEditField);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = fragment.findViewById(R.id.rezList);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyFavMoviesAdapter(getContext(), movie -> clickOnMovieView(movie));
        recyclerView.setAdapter(adapter);
        MovieRepo movieRepo = new MovieRepo();
        rezSearchMovies = movieRepo.getMovieMutableLiveData().getValue();
        adapter.setMovies(rezSearchMovies);
        adapter.notifyDataSetChanged();

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new FilterFragment(FLT_FRAG))
                        .commit();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rezSearchMovies = searchMovies(searchEditField.getText().toString().toLowerCase());
                adapter.setMovies(rezSearchMovies);
                adapter.notifyDataSetChanged();
            }
        });

        return fragment;
    }

    public ArrayList<Movie> searchMovies(String text) {
        if (!text.equals("") && text != null) {
            //rezSearchMovies.clear();
            ArrayList<Movie> result = new ArrayList<>();
            String json = getActivity().getIntent().getStringExtra(FLT_KEY);
            settings = new Gson().fromJson(json, SettingsOfFilter.class);
            MovieRepo movieRepo = new MovieRepo();
            ArrayList<Movie> movies = movieRepo.getMovieMutableLiveData().getValue();
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                if (settings != null) {
                    if (settings.getYear() != null) {
                        if (settings.getGenre().equals("любой") && settings.getCountry().equals("любая")) {
                            if (movie.getNameRu().toLowerCase().indexOf(text) >= 0
                                    && movie.getRating() >= settings.getMinRating()
                                    && movie.getRating() <= settings.getMaxRating()
                                    && movie.getYear() == settings.getYear()) {
                                result.add(movie);
                            }
                        } else if (settings.getGenre().equals("любой")) {
                            if (movie.getNameRu().toLowerCase().indexOf(text) >= 0
                                    && movie.getCountries().contains(settings.getCountry())
                                    && movie.getRating() >= settings.getMinRating()
                                    && movie.getRating() <= settings.getMaxRating()
                                    && movie.getYear() == settings.getYear()) {
                                result.add(movie);
                            }
                        } else if (settings.getCountry().equals("любая")) {
                            if (movie.getNameRu().toLowerCase().indexOf(text) >= 0 && movie.getGenres().contains(settings.getGenre())
                                    && movie.getRating() >= settings.getMinRating()
                                    && movie.getRating() <= settings.getMaxRating()
                                    && movie.getYear() == settings.getYear()) {
                                result.add(movie);
                            }
                        } else {
                            if (movie.getNameRu().toLowerCase().indexOf(text) >= 0 && movie.getGenres().contains(settings.getGenre())
                                    && movie.getCountries().contains(settings.getCountry())
                                    && movie.getRating() >= settings.getMinRating()
                                    && movie.getRating() <= settings.getMaxRating()
                                    && movie.getYear() == settings.getYear()) {
                                result.add(movie);
                            }
                        }
                    } else {
                        /*if (movie.getNameRu().indexOf(text) >= 0 && movie.getGenres().contains(settings.getGenre())
                                && movie.getCountries().contains(settings.getCountry())
                                && movie.getRating() >= settings.getMinRating()
                                && movie.getRating() <= settings.getMaxRating()) {
                            result.add(movie);
                        }*/
                        if (settings.getGenre().equals("любой") && settings.getCountry().equals("любая")) {
                            if (movie.getNameRu().toLowerCase().indexOf(text) >= 0
                                    && movie.getRating() >= settings.getMinRating()
                                    && movie.getRating() <= settings.getMaxRating()) {
                                result.add(movie);
                            }
                        } else if (settings.getGenre().equals("любой")) {
                            if (movie.getNameRu().toLowerCase().indexOf(text) >= 0
                                    && movie.getCountries().contains(settings.getCountry())
                                    && movie.getRating() >= settings.getMinRating()
                                    && movie.getRating() <= settings.getMaxRating()) {
                                result.add(movie);
                            }
                        } else if (settings.getCountry().equals("любая")) {
                            if (movie.getNameRu().toLowerCase().indexOf(text) >= 0 && movie.getGenres().contains(settings.getGenre())
                                    && movie.getRating() >= settings.getMinRating()
                                    && movie.getRating() <= settings.getMaxRating()) {
                                result.add(movie);
                            }
                        } else {
                            if (movie.getNameRu().toLowerCase().indexOf(text) >= 0 && movie.getGenres().contains(settings.getGenre())
                                    && movie.getCountries().contains(settings.getCountry())
                                    && movie.getRating() >= settings.getMinRating()
                                    && movie.getRating() <= settings.getMaxRating()) {
                                result.add(movie);
                            }
                        }
                    }
                    Collections.sort(result, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie movie1, Movie movie2) {
                            return movie1.compareTo(movie2);
                        }
                    });
                    if (settings.getTypeOfSort() == 1 && !result.isEmpty()) {
                        Collections.reverse(result);
                    }
                } else {
                    if (movie.getNameRu().toLowerCase().indexOf(text) >= 0) {
                        result.add(movie);
                    }
                }
            }
            return result;
        }
        return new ArrayList<>();
    }

    private Void clickOnMovieView(Movie movie) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.windowContainer, new MovieFragment(movie), movie.getId() + "");
        fragmentTransaction.commitAllowingStateLoss();
        return null;
    }
}