package com.example.onlinecinema;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyFavMoviePreviewHolder extends RecyclerView.ViewHolder {

    private ImageView favPreviewMovie;

    private TextView nameRuText;

    private TextView nameOrigText;

    private TextView yearText;

    public MyFavMoviePreviewHolder(@NonNull View itemView) {
        super(itemView);
        this.favPreviewMovie = itemView.findViewById(R.id.previewMovie);
        this.nameOrigText = itemView.findViewById(R.id.nameOrigText);
        this.nameRuText = itemView.findViewById(R.id.nameRuText);
        this.yearText = itemView.findViewById(R.id.yearText);
    }

    public ImageView getFavPreviewMovie() {
        return favPreviewMovie;
    }

    public TextView getNameRuText() {
        return nameRuText;
    }

    public TextView getNameOrigText() {
        return nameOrigText;
    }

    public TextView getYearText() {
        return yearText;
    }
}
