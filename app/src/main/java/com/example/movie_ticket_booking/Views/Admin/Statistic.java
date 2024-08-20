package com.example.movie_ticket_booking.Views.Admin;

import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.R;

public class Statistic extends Fragment {
    private static Statistic _instance = null;

    private Statistic() {
        super(R.layout.frag_admin_statistic);
    }

    public static Statistic getInstance() {
        if (_instance == null)
            _instance = new Statistic();
        return _instance;
    }
}
