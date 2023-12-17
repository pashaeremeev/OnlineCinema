package com.example.onlinecinema;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecinema.entities.Quality;
import com.example.onlinecinema.requests.MyQualityHolder;

import java.util.List;

public class MyItemQualityAdapter extends RecyclerView.Adapter<MyQualityHolder> {

    private ClickListenerQuality clickListener;
    private Context context;
    private List<Quality> qualities;

    public MyItemQualityAdapter(Context context, List<Quality> qualities, ClickListenerQuality clickListener) {
        this.clickListener = clickListener;
        this.context = context;
        this.qualities = qualities;
    }

    @Override
    public MyQualityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyQualityHolder(LayoutInflater.from(context)
                .inflate(R.layout.quality_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final MyQualityHolder holder, int position) {
        Quality item = qualities.get(position);
        if (item.getHeight() == -1) {
            holder.getQualityText().setText("AUTO");
        } else {
            holder.getQualityText().setText(item.getHeight() + "p");
        }
        if (item.isCurrent() != 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#0077FF"));
            holder.getQualityText().setTextColor(Color.WHITE);
        }
        holder.itemView.setOnClickListener(view -> clickListener.invoke(item));
    }

    @Override
    public int getItemCount() {
        return qualities.size();
    }
}

