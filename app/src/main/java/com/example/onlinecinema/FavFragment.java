package com.example.onlinecinema;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.repos.MovieRepo;

import java.util.ArrayList;


public class FavFragment extends Fragment {

    private MovieRepo movieRepo;
    private MyFavMoviesAdapter adapter;
    private ArrayList<Movie> favMovies;
    private LiveData<ArrayList<Movie>> liveData;

    public FavFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        movieRepo = new MovieRepo();
        liveData = movieRepo.getMovieMutableLiveData();
        favMovies = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.movieList);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyFavMoviesAdapter(getContext(), movie -> clickOnMovieView(movie));
        liveData.observe(getViewLifecycleOwner(), movies -> {
            /*for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                if (movie.isFavorite()) {
                    favMovies.add(movie);
                }
            }*/
            adapter.setMovies(favMovies);
            adapter.notifyDataSetChanged();
        });
        if (favMovies.size() < 1) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new NoRezSearchFragment());
            fragmentTransaction.commitAllowingStateLoss();
        }
        //DownloadMovies.downloadChannels(movieRepo);
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
}