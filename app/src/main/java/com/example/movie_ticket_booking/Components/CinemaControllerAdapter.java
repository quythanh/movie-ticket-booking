package com.example.movie_ticket_booking.Components;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CinemaControllerAdapter extends RecyclerView.Adapter<CinemaControllerAdapter.CCViewHolder> {

    private Map<String, List<Cinema>> cinemas;

    @NonNull
    @Override
    public CCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_cinema_address_filter_item, parent, false);

        return new CCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CCViewHolder holder, int position) {
        Map.Entry<String, List<Cinema>> c = null;
        int i = 0;
        for (Map.Entry<String, List<Cinema>> m : cinemas.entrySet()) {
            if (i == position)
                c = m;
            i += 1;
        }
        if (c != null) {
            holder.address.setText(!c.getKey().equals("closest") ? c.getKey() : "Gần bạn nhất");
            holder.amount.setText(String.format("%d",c.getValue().size()));

            List<Cinema> l = c.getValue();
            holder.trigger.setOnClickListener(v -> {
                if(!holder.isCliked) {
                    holder.main.setBackgroundColor(Color.argb(255, 92, 12, 18));
                    holder.adapter.setCinemas(l);
                    holder.detail.setAdapter(holder.adapter);
                    holder.detail.setVisibility(View.VISIBLE);
                    holder.isCliked = true;
                }
                else {
                    holder.main.setBackgroundColor(Color.argb(255, 176, 0, 32));
                    holder.detail.setVisibility(View.GONE);
                    holder.isCliked = false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cinemas.entrySet().size();
    }

    public static class CCViewHolder extends RecyclerView.ViewHolder{

        private TextView address, amount, trigger;
        private LinearLayout main;
        private RecyclerView detail;
        private CinemaAdapter adapter;
        private boolean isCliked;
        public CCViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.cineAddress);
            amount = itemView.findViewById(R.id.cineAmount);
            trigger = itemView.findViewById(R.id.triggerOnfilterClick);
            main = itemView.findViewById(R.id.mainOnClick);
            detail = itemView.findViewById(R.id.movieHorizontal);
            detail.setHasFixedSize(true);
            detail.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapter = new CinemaAdapter(new ArrayList<>(), CinemaAdapter.EventType.ON_INTENT);
            isCliked = false;
        }
    }
}
