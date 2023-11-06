package com.example.onlinecinema;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyMoviePreviewHolder extends RecyclerView.ViewHolder {

    private ImageView previewMovie;

    public MyMoviePreviewHolder(@NonNull View itemView) {
        super(itemView);
        this.previewMovie = itemView.findViewById(R.id.previewMovie);
    }

    public ImageView getPreviewMovie() {
        return previewMovie;
    }
}
