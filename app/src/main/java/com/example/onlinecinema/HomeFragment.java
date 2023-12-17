package com.example.onlinecinema;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.repos.MovieRepo;
import com.example.onlinecinema.repos.UserRepo;
import com.example.onlinecinema.requests.DownloadFavMovies;
import com.example.onlinecinema.requests.RestClient;
import com.example.onlinecinema.requests.json.MovieJson;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@UnstableApi public class HomeFragment extends Fragment {

    private MovieRepo movieRepo;
    private MyAllMoviesAdapter adapter;
    private LiveData<ArrayList<Movie>> liveData;
    private LiveData<ArrayList<Movie>> downloadedLiveData;
    private ArrayList<Movie> prevMovies;
    private static final int PREV_MOVIES_SIZE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        movieRepo = new MovieRepo();
        liveData = movieRepo.getMovieMutableLiveData();
        downloadedLiveData = movieRepo.getDownloadedMovieMutableLiveData();
        prevMovies = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.movieList);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyAllMoviesAdapter(getContext(), movie -> {
            clickOnMovieView(movie);
        });
        liveData.observe(getViewLifecycleOwner(), movies -> {
            adapter.setMovies(movies);
            adapter.notifyDataSetChanged();
        });
        downloadedLiveData.observe(getViewLifecycleOwner(), movies -> {
            ArrayList<MovieJson> jsons = new ArrayList<>();
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                jsons.add(movie.toJson());
            }
            RestClient client = RestClient.getInstance();
            String jsonData = new Gson().toJson(jsons);
            RequestBody body = client.createRequestBody(jsonData);
            Request request = client.createPostRequest("/movies/", body);
            client.getClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("Send download movies", "server is disconnect.");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("Send download movies", response.code() + ".");
                }
            });
        });
        UserRepo.getCurrentUser().observe(getViewLifecycleOwner(), (Observer<User>) user -> {
            DownloadFavMovies.downloadMovies(movieRepo);
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
}