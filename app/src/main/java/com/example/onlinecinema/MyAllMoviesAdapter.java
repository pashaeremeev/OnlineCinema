package com.example.onlinecinema;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecinema.entities.Movie;

import java.util.ArrayList;

public class MyAllMoviesAdapter extends RecyclerView.Adapter<MyMoviePreviewHolder> {

    private Context context;
    private ArrayList<Movie> movies;
    private ClickListenerMovie clickListener;

    public MyAllMoviesAdapter(Context context, ClickListenerMovie clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyMoviePreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyMoviePreviewHolder(LayoutInflater.from(context)
                .inflate(R.layout.movie_preview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyMoviePreviewHolder holder, int position) {
        Movie movie = movies.get(position);
        Uri posterUrl = Uri.parse(movie.getPoster());
        Glide.with(context)
                .load(posterUrl)
                .error(R.drawable.baseline_no_internet_24)
                .into(holder.getPreviewMovie());
        holder.itemView.setOnClickListener(view -> clickListener.invoke(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
