package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common;
import com.example.movie_ticket_booking.FragmentEnum;
import com.example.movie_ticket_booking.MainActivity;
import com.example.movie_ticket_booking.Models.BaseModel;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.R;

import lombok.Getter;
import lombok.Setter;

public class MovieInfoFragment extends Fragment {
    private static MovieInfoFragment _instance = null;

    @Getter
    private static MutableLiveData<Movie> selectedMovie = new MutableLiveData<>();

    private ImageView landscape, poster;
    private TextView title, rating, directors, actors, premiere, minute, intro;
    private WebView trailer;

    private MovieInfoFragment() {
        // Required empty public constructor
        super(R.layout.fragment_movie_info);
    }

    public static MovieInfoFragment getInstance() {
        if (_instance == null)
            _instance = new MovieInfoFragment();
        return _instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Button backButton = view.findViewById(R.id.backbtn);
        backButton.setOnClickListener(_v -> Common.changeFragment(getParentFragmentManager(), FilmFragment.getInstance()));

        if(selectedMovie == null)
            Common.changeFragment(getParentFragmentManager(), FilmFragment.getInstance());
        else {
            MovieInfoFragment.selectedMovie.observe(getViewLifecycleOwner(), movie -> {
                landscape = view.findViewById(R.id.InfoLandscape);
                poster = view.findViewById(R.id.InfoPoster);
                title = view.findViewById(R.id.movieInfoTitle);
                directors = view.findViewById(R.id.nameDirector);
                actors = view.findViewById(R.id.nameActor);
                minute = view.findViewById(R.id.nameTime);
                premiere = view.findViewById(R.id.namePremiere);
                intro = view.findViewById(R.id.intro);
                trailer = view.findViewById(R.id.trailer);

                Glide.with(view).load(movie.getLandscapeImage()).into(landscape);
                Glide.with(view).load(movie.getPoster()).into(poster);
                title.setText(movie.getTitle());
                directors.setText(movie.getDirectorsToString());
                actors.setText(movie.getActorsToString());
                minute.setText(movie.getMinute() + " phút");
                intro.setText(movie.getIntro());
                premiere.setText(Common.dateFormatter.format(movie.getPremiere()));


                trailer.loadData(movie.getTrailer(), "text/html", "utf-8");
                trailer.getSettings().setJavaScriptEnabled(true);
                trailer.getSettings().setLoadWithOverviewMode(true);
                trailer.getSettings().setUseWideViewPort(true);
                trailer.setWebChromeClient(new WebChromeClient());
            });
        }
        return view;
    }
}