package com.example.movie_ticket_booking.Components;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.R;

import java.util.Map;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> {

    private int max;
    private Map<String, Object> bindResult;

    public StarAdapter(Map<String, Object> r){
        super();
        max = 5;
        this.bindResult = r;
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_star_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        bindResult.putIfAbsent("stars", 0);
        int cs = (int) bindResult.get("stars");
        if(position <= cs - 1)
            holder.img.setImageResource(R.drawable.ic_star);
        else
            holder.img.setImageResource(R.drawable.ic_white_star);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if(position != cs - 1)
                {
                    bindResult.put("stars", position + 1);
                    notifyDataSetChanged();
                }
                else
                {
                    bindResult.put("stars", 0);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class StarViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.star);

        }
    }
}
