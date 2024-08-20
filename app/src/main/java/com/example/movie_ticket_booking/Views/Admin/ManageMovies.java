package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.R;

public class ManageMovies extends Fragment {
    private static ManageMovies _instance = null;

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
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
