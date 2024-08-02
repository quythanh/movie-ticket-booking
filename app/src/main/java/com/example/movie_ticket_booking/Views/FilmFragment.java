package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.movie_ticket_booking.Components.MovieAdapter;
import com.example.movie_ticket_booking.Components.ViewPagerAdapter;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.R;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //custom fields
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ViewPagerAdapter viewPagerAdapter;

    private final MovieController _controller = MovieController.getInstance();

    public FilmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilmFragment newInstance(String param1, String param2) {
        FilmFragment fragment = new FilmFragment();
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
        View view = inflater.inflate(R.layout.fragment_film, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        GridView grid = view.findViewById(R.id.gridMovie);

        this._controller.getAll().observe(getViewLifecycleOwner(), movies -> {
            viewPagerAdapter = new ViewPagerAdapter(view.getContext(), movies);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Movie m = movies.get(position);
                    TextView text = view.findViewById(R.id.adText);
                    text.setText(m.getTitle());
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            MovieAdapter adapt = new MovieAdapter(view.getContext(), movies);
            grid.setAdapter(adapt);
            ViewGroup.LayoutParams params = grid.getLayoutParams();
            params.height = movies.size() * 550;
            grid.setLayoutParams(params);
            grid.setOnItemClickListener((parent, _view, position, id) -> {
                System.out.println(adapt.getMovies().get(position));
            });
        });

        return view;
    }
}