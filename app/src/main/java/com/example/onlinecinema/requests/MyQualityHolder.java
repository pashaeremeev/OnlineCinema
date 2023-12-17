package com.example.onlinecinema.requests;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecinema.R;

public class MyQualityHolder extends RecyclerView.ViewHolder {

    private TextView qualityText;

    public MyQualityHolder(@NonNull View itemView) {
        super(itemView);
        qualityText = itemView.findViewById(R.id.qualityItem);
    }

    public TextView getQualityText() {
        return qualityText;
    }
}
