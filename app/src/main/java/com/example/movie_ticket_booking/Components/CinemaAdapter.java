package com.example.movie_ticket_booking.Components;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {

    private List<Cinema> cinemas;
    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_cinema_item, parent,false);
        return new CinemaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        Cinema c = cinemas.get(position);
        holder.name.setText(c.getName());
        holder.address.setText(c.getAddress().toString());
    }

    @Override
    public int getItemCount() {
        return this.cinemas.size();
    }

    public static class  CinemaViewHolder extends RecyclerView.ViewHolder{

        private TextView name, address;
        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.CinemaItemName);
            address = itemView.findViewById(R.id.CinemaItemAdress);
        }
    }

}
