package com.example.movie_ticket_booking.Components;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common.GenericAdapter;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.UserRole;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieAdapter extends GenericAdapter<Movie> {

    private LifecycleOwner owner;
    public MovieAdapter(LifecycleOwner owner, List<Movie> list) {
        super(list, R.layout.grid_movie_item_home);
        this.owner = owner;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = super.getView(position, view, parent);
        Movie m = this.getItem(position);

        ImageView img = view.findViewById(R.id.imageItem);
        TextView title = view.findViewById(R.id.titleItem);
        TextView rating = view.findViewById(R.id.ratingItem);

        if (AuthUserController.getInstance().getUserlogin().getValue().getRole() == UserRole.ADMIN) {
            title.setTextColor(Color.BLACK);
            rating.setTextColor(Color.BLACK);
        }

        Glide.with(view).load(m.getPoster()).into(img);
        title.setText(m.getTitle());

        m.getAvgPoint(owner).observe(owner, aDouble -> {
            if (aDouble == null) return;
            double stars = m.getRatingPoint();
            rating.setText(stars != stars  ? "Không có đánh giá" : String.format("%.1f / 5.0", stars));
        });

        return view;
    }
}
