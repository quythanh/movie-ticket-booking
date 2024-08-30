package com.example.movie_ticket_booking.Components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Models.Rating;
import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingAdapter extends BaseAdapter {

    private List<Rating> ratingList;

    @Override
    public int getCount() {
        return ratingList.size();
    }

    @Override
    public Object getItem(int position) {
        return ratingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rating_item, parent, false);
        Rating r = (Rating) getItem(position);

        TextView username, content, star, date;
        ImageView img;

        username = view.findViewById(R.id.ratingUsername);
        content = view.findViewById(R.id.ratingContent);
        star = view.findViewById(R.id.ratingStar);
        date = view.findViewById(R.id.ratingDate);
        img = view.findViewById(R.id.ratingAvatar);

        r.getUser().get().addOnSuccessListener(command -> {
            User u = command.toObject(User.class);
            username.setText(u.getUsername());
            Glide.with(parent.getContext()).load(u.getAvatarPath()).into(img);
        });

        content.setText(r.getComment());
        star.setText(String.format("%d.0/5.0", r.getStars()));
        date.setText(Constant.DATETIME_FORMATTER.format(r.getCreatedDate()));
        return view;
    }
}
