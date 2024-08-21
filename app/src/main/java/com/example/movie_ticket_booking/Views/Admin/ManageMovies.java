package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.movie_ticket_booking.Common;
import com.example.movie_ticket_booking.R;

public class ManageMovies extends Fragment {
    private static ManageMovies _instance = null;

    private ImageView btnAdd, btnBack;

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
        bindingViews(view);
        setupViews();
        return view;
    }

    private void bindingViews(View view) {
        btnAdd = view.findViewById(R.id.btn_add);
        btnBack = view.findViewById(R.id.btn_back);
    }

    private void setupViews() {
        FragmentManager fragmentManager = getParentFragmentManager();

        btnAdd.setOnClickListener(_v -> Common.addFragment(fragmentManager, AddMovie.getInstance()));
        btnBack.setOnClickListener(_v -> fragmentManager.popBackStack());
    }
}
