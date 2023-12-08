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

public class MyFavMoviesAdapter extends RecyclerView.Adapter<MyFavMoviePreviewHolder> {

    private Context context;
    private ArrayList<Movie> movies;
    private ClickListener clickListener;

    public MyFavMoviesAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyFavMoviePreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFavMoviePreviewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fav_movies_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyFavMoviePreviewHolder holder, int position) {
        Movie movie = movies.get(position);
        Uri posterUrl = Uri.parse(movie.getPoster());
        Glide.with(context)
                .load(posterUrl)
                .error(R.drawable.baseline_no_internet_24)
                .into(holder.getFavPreviewMovie());
        holder.getNameRuText().setText(movie.getFirstName());
        holder.getNameOrigText().setText(movie.getOrigName());
        String preText = holder.getYearText().getText().toString();
        holder.getYearText().setText(preText + movie.getYear());
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
