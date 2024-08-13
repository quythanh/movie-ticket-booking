package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.movie_ticket_booking.FragmentEnum;
import com.example.movie_ticket_booking.MainActivity;
import com.example.movie_ticket_booking.Models.BaseModel;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.R;

import lombok.Getter;
import lombok.Setter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Getter
    private static MutableLiveData<Movie> selectedMovie = new MutableLiveData<>();

    private ImageView landscape, poster;
    private TextView title, rating, directors, actors, premiere, minute, intro;
    private WebView trailer;
    public MovieInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieInfoFragment newInstance(String param1, String param2) {
        MovieInfoFragment fragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_info, container, false);
        Button backButton = view.findViewById(R.id.backbtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().getFragmentChanger().setValue(FragmentEnum.FILM);
            }
        });

        if(selectedMovie == null)
            MainActivity.getInstance().getFragmentChanger().setValue(FragmentEnum.FILM);
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
                premiere.setText(BaseModel.dateFormatter.format(movie.getPremiere()));


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