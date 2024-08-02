package com.example.movie_ticket_booking.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieAdapter extends BaseAdapter {
    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context c, List<Movie> m ){
        this.context = c;
        this.movies = m;
    }
    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        String id = movies.get(position).getId();
        return Integer.parseInt(id.substring(1, id.length()-1));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_movie_item_home, parent, false);
        Movie m = movies.get(position);
        ImageView img = view.findViewById(R.id.imageItem);
        TextView title = view.findViewById(R.id.titleItem);
        TextView rating = view.findViewById(R.id.ratingItem);

        Glide.with(view).load(m.getPoster()).into(img);
        title.setText(m.getTitle());
        rating.setText(m.getId());

        img.setBackground(view.getResources().getDrawable(R.drawable.radius10dp, null));
        return view;
    }
}
