package com.example.onlinecinema;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.repos.MovieRepo;
import com.example.onlinecinema.repos.UserRepo;
import com.example.onlinecinema.requests.DownloadFavMovies;
import com.example.onlinecinema.requests.DownloadMoviesFromDB;

import java.util.ArrayList;


@UnstableApi public class FavFragment extends Fragment {

    private MovieRepo movieRepo;
    private MyFavMoviesAdapter adapter;
    private ArrayList<Movie> favMovies;
    private LiveData<ArrayList<Movie>> liveData;
    private LiveData<ArrayList<Integer>> favLiveData;

    public FavFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        movieRepo = new MovieRepo();
        favLiveData = movieRepo.getFavMovieMutableLiveData();
        liveData = movieRepo.getMovieMutableLiveData();
        favMovies = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.movieList);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyFavMoviesAdapter(getContext(), movie -> clickOnMovieView(movie));
        ArrayList<Movie> movies = liveData.getValue();
        ArrayList<Movie> favMovies = new ArrayList<>();
        UserRepo.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            DownloadFavMovies.downloadMovies(movieRepo);
        });
        favLiveData.observe(getViewLifecycleOwner(), favMoviesIds -> {
            favMovies.clear();
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                if (favMoviesIds.contains(movie.getId())) {
                    favMovies.add(movie);
                }
            }
            if (favMoviesIds.isEmpty()) {
                showNoRezFragment();
            } else {
                adapter.setMovies(favMovies);
            }
            adapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    private Void clickOnMovieView(Movie movie) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.windowContainer, new MovieFragment(movie), movie.getId() + "");
        fragmentTransaction.commitAllowingStateLoss();
        return null;
    }

    private Void showNoRezFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new NoRezSearchFragment());
        fragmentTransaction.commitAllowingStateLoss();
        return null;
    }
}