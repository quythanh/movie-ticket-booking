package com.example.movie_ticket_booking.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Components.RatingAdapter;
import com.example.movie_ticket_booking.Components.StarAdapter;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Controllers.RatingController;
import com.example.movie_ticket_booking.Controllers.UserController;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.Rating;
import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.R;
import com.example.movie_ticket_booking.Views.BookingViews.ShowtimeCinemaBooking;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class MovieInfoFragment extends Fragment {
    private static MovieInfoFragment _instance = null;

    @Getter
    private static MutableLiveData<Movie> selectedMovie = new MutableLiveData<>();

    private ImageView landscape, poster;
    private TextView title, rating, directors, actors, premiere, minute, intro, trigger;
    private WebView trailer;
    private Button backButton, bookingBtn, reviewBtn, ratingBtn;
    private LinearLayout ratings, infos, panel;
    private RecyclerView stars;
    private Map<String, Object> starResult;
    private Boolean isOpeningRating, isWatchingRating;
    private TextInputEditText content;
    private ListView ratingList;
    private RatingAdapter ra;

    private MovieInfoFragment() {
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
        bindingViews(view);
        setupViews(view);
        return view;
    }

    private void bindingViews(View view) {
        backButton = view.findViewById(R.id.backbtn);
        landscape = view.findViewById(R.id.InfoLandscape);
        poster = view.findViewById(R.id.InfoPoster);
        title = view.findViewById(R.id.movieInfoTitle);
        directors = view.findViewById(R.id.nameDirector);
        actors = view.findViewById(R.id.nameActor);
        minute = view.findViewById(R.id.nameTime);
        premiere = view.findViewById(R.id.namePremiere);
        intro = view.findViewById(R.id.intro);
        trailer = view.findViewById(R.id.trailer);
        bookingBtn = view.findViewById(R.id.bookingBtn);
        reviewBtn = view.findViewById(R.id.reviewBtn);
        rating = view.findViewById(R.id.movieInfoRatings);
        trigger = view.findViewById(R.id.triggerOpenRating);

        ratings = view.findViewById(R.id.ratingSession);
        infos = view.findViewById(R.id.infoSession);
        panel = view.findViewById(R.id.ratingPanel);

        stars = view.findViewById(R.id.stars);
        starResult = new HashMap<>();

        isOpeningRating = false;
        isWatchingRating = false;

        ratingBtn = view.findViewById(R.id.ratingBtn);
        content = view.findViewById(R.id.ratingContentEdit);
        ratingList = view.findViewById(R.id.ratings);
    }

    private void setupViews(View view) {
        backButton.setOnClickListener(_v -> UIManager.changeFragment(getParentFragmentManager(), FilmFragment.getInstance()));

        if (selectedMovie == null)
            UIManager.changeFragment(getParentFragmentManager(), FilmFragment.getInstance());
        else
            MovieInfoFragment.selectedMovie.observe(getViewLifecycleOwner(), movie -> {
                Glide.with(view).load(movie.getLandscapeImage()).into(landscape);
                Glide.with(view).load(movie.getPoster()).into(poster);
                title.setText(movie.getTitle());
                directors.setText(movie.getDirectorsToString());
                actors.setText(movie.getActorsToString());
                minute.setText(movie.getMinute() + " phút");
                intro.setText(movie.getIntro());
                premiere.setText(Constant.DATE_FORMATTER.format(movie.getPremiere()));
                rating.setText(movie.getRatingPoint() == 0 ? "Không có đánh giá" : String.format("%.1f / 5.0", movie.getRatingPoint()));

                trailer.loadData(UIManager.getVideoFrame(movie.getTrailer()), "text/html", "utf-8");
                trailer.getSettings().setJavaScriptEnabled(true);
                trailer.getSettings().setLoadWithOverviewMode(true);
                trailer.getSettings().setUseWideViewPort(true);
                trailer.setWebChromeClient(new WebChromeClient());

                stars.setHasFixedSize(true);
                stars.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

                StarAdapter sa = new StarAdapter(starResult);
                stars.setAdapter(sa);

                RatingController.getInstance(movie).getAll().observe(getViewLifecycleOwner(), ratings1 -> {
                    ra = new RatingAdapter(ratings1);
                    ratingList.setAdapter(ra);
                });

                bookingBtn.setOnClickListener(_v -> {
                    if (AuthUserController.getInstance().getUserlogin().getValue().getId() == null){
                        UIManager.changeFragment(getParentFragmentManager(), LoginFragment.getInstance());
                        return;
                    }
                    Intent intent = new Intent(view.getContext(), ShowtimeCinemaBooking.class);
                    intent.putExtra("movie_id", movie.getId());
                    startActivity(intent);
                });

                reviewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isOpeningRating){
                            ratings.setVisibility(View.VISIBLE);
                            infos.setVisibility(View.GONE);
                            reviewBtn.setText("TRỞ VỀ");
                            reviewBtn.setBackgroundColor(Color.parseColor("#404040"));
                        }
                        else {
                            ratings.setVisibility(View.GONE);
                            infos.setVisibility(View.VISIBLE);
                            reviewBtn.setText("ĐÁNH GIÁ");
                            reviewBtn.setBackgroundColor(Color.parseColor("#F6AA30"));
                        }
                        isOpeningRating = !isOpeningRating;
                    }
                });
                trigger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isWatchingRating){
                            panel.setVisibility(View.GONE);
                            ratingList.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            panel.setVisibility(View.VISIBLE);
                            ratingList.setVisibility(View.GONE);
                        }
                        isWatchingRating = !isWatchingRating;
                    }
                });
                ratingBtn.setOnClickListener(v -> {
                    User u = AuthUserController.getInstance().getUserlogin().getValue();
                    if(u.getId() == null){
                        UIManager.changeFragment(getParentFragmentManager(), LoginFragment.getInstance());
                        return;
                    }
                    if(starResult.get("stars") == null || (int) starResult.get("stars") == 0){
                        Toast.makeText(view.getContext(), "Vui lòng đánh giá sao", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(content.getText().toString().isBlank()){
                        Toast.makeText(view.getContext(), "Vui lòng đánh giá", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Rating r = new Rating();
                    r.setUser(UserController.getInstance().getRef(u.getId()));
                    r.setStars((int) starResult.get("stars"));
                    r.setComment(content.getText().toString());
                    r.setMovie(MovieController.getInstance().getRef(movie.getId()));

                    content.setText("");
                    starResult.put("stars", 0);
                    ra.getRatingList().add(r);
                    ratingList.invalidateViews();
                    RatingController.getInstance(movie).add(r);

                });
            });
    }
}