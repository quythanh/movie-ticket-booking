package com.example.movie_ticket_booking.Components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Common.ItemClickListener;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {
    public static enum EventType{
        ON_BOOKING,
        ON_INTENT
    }
    private List<Cinema> cinemas;
    private MutableLiveData<Cinema> cinema_data;
    private EventType setOnClick;

    public CinemaAdapter(List<Cinema> c, EventType setOnClick) {
        this.cinemas = c;
        this.setOnClick = setOnClick;
        cinema_data = new MutableLiveData<>();
    }

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

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                switch (setOnClick){
                    case ON_BOOKING:
                        cinema_data.setValue(cinemas.get(position));
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.cinemas.size();
    }

    public static class  CinemaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView name, address;
        private LinearLayout layout;
        @Setter
        private ItemClickListener itemClickListener;
        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.CinemaItemName);
            address = itemView.findViewById(R.id.CinemaItemAdress);
            layout = itemView.findViewById(R.id.background_item);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.OnClick(v, getBindingAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.OnClick(v, getBindingAdapterPosition(), true);
            return true;
        }
    }

}
