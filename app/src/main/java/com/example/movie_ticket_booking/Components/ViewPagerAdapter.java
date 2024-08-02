package com.example.movie_ticket_booking.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Movie> photos;

    public ViewPagerAdapter(Context c, List<Movie> pt){
        this.context = c;
        this.photos = pt;
    }

    @Override
    public int getCount() {
        return this.photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(android.view.ViewGroup container, int position, java.lang.Object object) {
        container.removeView((View) object);
    }

    @Override
    public java.lang.Object instantiateItem(android.view.ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_item_home, container, false);
        ImageView img = view.findViewById(R.id.slider);

        String url = photos.get(position).getLandscapeImage();
        if(url!=null && !url.isEmpty()){
            Glide.with(context).load(url).into(img);
        }

        container.addView(view);
        return view;
    }

}
