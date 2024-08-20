package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movie_ticket_booking.R;

public class CinemasFragment extends Fragment {
    private static CinemasFragment _instance = null;

    private CinemasFragment() {
        // Required empty public constructor
    }

    public static CinemasFragment getInstance() {
        if (_instance == null)
            _instance = new CinemasFragment();
        return _instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_cinema_fragment, container, false);
    }
}