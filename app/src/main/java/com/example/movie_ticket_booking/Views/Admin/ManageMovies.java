package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.EditContext;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Components.MovieAdapter;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.MovieType;
import com.example.movie_ticket_booking.R;

import lombok.Getter;

public class ManageMovies extends Fragment {
    // === REGION: FIELDS ===
    private static ManageMovies _instance = null;

    private EditText inpName;
    private GridView gridMovies;
    private ImageView btnAdd, btnBack;
    private Spinner spnMovieType;

    private ArrayAdapter<MovieType> movieTypeAdapter;
    private FragmentManager fragmentManager;
    private GenericFilter<Movie> filters;
    private MovieController _controller;
    // === END REGION ===


    // === REGION: METHODS ===
    private ManageMovies() {
        super(R.layout.frag_admin_manage_movies);
    }

    public static ManageMovies getInstance() {
        if (_instance == null)
            _instance = new ManageMovies();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        bindingViews(view);
        setupViews();
        return view;
    }

    private void initData() {
        _controller = MovieController.getInstance();
        filters = new GenericFilter<>(Movie.class);
        fragmentManager = getParentFragmentManager();
        movieTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, MovieType.values());
    }

    private void bindingViews(View view) {
        btnAdd = view.findViewById(R.id.btn_add);
        btnBack = view.findViewById(R.id.btn_back);

        gridMovies = view.findViewById(R.id.grid_movies);

        inpName = view.findViewById(R.id.inp_name);

        spnMovieType = view.findViewById(R.id.spn_movie_type);
    }

    private void setupViews() {
        btnAdd.setOnClickListener(_v -> UIManager.addFragment(fragmentManager, AddMovie.getInstance()));
        btnBack.setOnClickListener(_v -> fragmentManager.popBackStack());

        inpName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = inpName.getText().toString().trim().toLowerCase();
                filters.set(FilterType.STRING_CONTAINS, "title", searchText.length() >= 3 ? searchText.trim() : null);
                loadViews();
            }
        });

        spnMovieType.setAdapter(movieTypeAdapter);
        spnMovieType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filters.set(FilterType.EQUAL, "type", adapterView.getItemAtPosition(i));
                loadViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadViews() {
        _controller
                .filter(filters.get())
                .observe(getViewLifecycleOwner(), _movies -> {
                    ListAdapter adapter = new MovieAdapter(_movies);
                    gridMovies.setAdapter(adapter);
                    gridMovies.setOnItemClickListener((_adapterView, _v, i, l) -> {
                        EditContext.movie.setValue(_movies.get(i));
                        UIManager.addFragment(fragmentManager, EditMovie.getInstance());
                    });
                });
    }
    // === END REGION ===
}
