package com.example.movie_ticket_booking.Components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common.GenericAdapter;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieAdapter extends GenericAdapter<Movie> {

    public MovieAdapter(List<Movie> list) {
        super(list, R.layout.grid_movie_item_home);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = super.getView(position, view, parent);
        Movie m = this.getItem(position);

        ImageView img = view.findViewById(R.id.imageItem);
        TextView title = view.findViewById(R.id.titleItem);
        TextView rating = view.findViewById(R.id.ratingItem);

        Glide.with(view).load(m.getPoster()).into(img);
        title.setText(m.getTitle());
        rating.setText(m.getId());

        return view;
    }
}
