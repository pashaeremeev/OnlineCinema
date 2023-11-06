package com.example.onlinecinema;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.repos.MovieRepo;
import com.example.onlinecinema.requests.DownloadMovies;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private MovieRepo movieRepo;
    private MyAllMoviesAdapter adapter;
    private LiveData<ArrayList<Movie>> liveData;
    private ArrayList<Movie> prevMovies;
    private static final int PREV_MOVIES_SIZE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        movieRepo = new MovieRepo();
        liveData = movieRepo.getMovieMutableLiveData();
        prevMovies = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.movieList);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyAllMoviesAdapter(getContext(), movie -> clickOnMovieView(movie));
        liveData.observe(getViewLifecycleOwner(), movies -> {
            if (movies.size() > PREV_MOVIES_SIZE) {
                for (int i = 0; i < PREV_MOVIES_SIZE; i++ ){
                    Movie movie = movies.get(i);
                    prevMovies.add(movie);
                }
                adapter.setMovies(prevMovies);
            } else {
                adapter.setMovies(movies);
            }
            adapter.notifyDataSetChanged();
        });
        DownloadMovies.downloadChannels(movieRepo);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private Void clickOnMovieView(Movie movie) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.windowContainer, new MovieFragment(movie.getId(), movieRepo), movie.getId() + "");
        fragmentTransaction.commitAllowingStateLoss();
        return null;
    }
}