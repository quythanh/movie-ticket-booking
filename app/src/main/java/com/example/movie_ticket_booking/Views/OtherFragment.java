package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movie_ticket_booking.R;

public class OtherFragment extends Fragment {
    private static OtherFragment _instance = null;

    private OtherFragment() {
        // Required empty public constructor
    }

    public static OtherFragment getInstance() {
        if (_instance == null)
            _instance = new OtherFragment();
        return _instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_other_fragment, container, false);
    }
}