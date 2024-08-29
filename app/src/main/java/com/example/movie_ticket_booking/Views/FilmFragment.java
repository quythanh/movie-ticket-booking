package com.example.movie_ticket_booking.Views;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Components.MovieAdapter;
import com.example.movie_ticket_booking.Components.ViewPagerAdapter;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.MovieType;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.Map;

public class FilmFragment extends Fragment {
    private static FilmFragment _instance = null;

    //custom fields
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private GridView grid;
    private MovieAdapter adapt;
    private TextView text, hello, name;
    private Map<MovieType, TextView> filter;
    private MovieType currentType = MovieType.INACCESSIBLE;
    private Button loginBtn, btn;
    private final MovieController _controller = MovieController.getInstance();
    private final AuthUserController authUserController = AuthUserController.getInstance();

    private FilmFragment() {
        // Required empty public constructor
    }

    public static FilmFragment getInstance() {
        if (_instance == null)
            _instance = new FilmFragment();
        return _instance;
    }

    private void updateViewByType(MovieType type, boolean forceUpdate) {
        if (type == currentType && !forceUpdate) return;
        currentType = type;

        this._controller.getByType(type).observe(getViewLifecycleOwner(), movies -> {
            adapt.setList(movies);
            grid.setAdapter(adapt);
            ViewGroup.LayoutParams params = grid.getLayoutParams();
            params.height = (movies.size() / 2 + (movies.size() & 1)) * 1200;
            grid.setLayoutParams(params);
            grid.setOnItemClickListener((parent, _view, position, id) -> {
                Log.d("film",adapt.getList().get(position).getId());
                MovieInfoFragment.getSelectedMovie().setValue(adapt.getList().get(position));
                UIManager.changeFragment(getParentFragmentManager(), MovieInfoFragment.getInstance());
            });
        });

        filter.forEach((key, value) -> value.setTextColor(Color.parseColor("#8f9193")));
        filter.get(type).setTextColor(Color.parseColor("#b00020"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film, container, false);
        bindingViews(view);
        setupViews(view);
        return view;
    }

    private void bindingViews(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        grid = view.findViewById(R.id.gridMovie);
        text = view.findViewById(R.id.adText);
        loginBtn = view.findViewById(R.id.loginButton);
        hello = view.findViewById(R.id.helloTxt);
        name = view.findViewById(R.id.nameTxt);
        btn = view.findViewById(R.id.buttonDetail);
    }

    private void setupViews(View view) {
        viewPagerAdapter = new ViewPagerAdapter(view.getContext(), new ArrayList<>());
        adapt = new MovieAdapter(new ArrayList<>());

        filter = Map.of(
                MovieType.PRESENTING, view.findViewById(R.id.txt_presenting),
                MovieType.COMING_SOON, view.findViewById(R.id.txt_coming_soon),
                MovieType.EARLY_ACCESS, view.findViewById(R.id.txt_early_access)
        );
        filter.forEach((key, value) -> value.setOnClickListener(_view -> updateViewByType(key, false)));

        //
        loginBtn.setOnClickListener(_v -> UIManager.changeFragment(getParentFragmentManager(), LoginFragment.getInstance()));

        authUserController.getUserlogin().observe(getViewLifecycleOwner(), user -> {
            if (user.getId() == null) {
                loginBtn.setVisibility(View.VISIBLE);
                hello.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
            }
            else {
                loginBtn.setVisibility(View.GONE);
                hello.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                name.setText(String.format("%s %s",user.getLastName(),user.getFirstName()));
            }
        });
        //
        updateViewByType(MovieType.PRESENTING, true);

        // Landscape Images
        this._controller.getByType(MovieType.PRESENTING).observe(getViewLifecycleOwner(), movies -> {
            viewPagerAdapter.setPhotos(movies);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Movie m = movies.get(position);
                    text.setText(m.getTitle());
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        });
    }
}