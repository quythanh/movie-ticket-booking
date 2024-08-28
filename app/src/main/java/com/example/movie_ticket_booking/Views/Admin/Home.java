package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.R;

public class Home extends Fragment {
    private static Home _instance = null;

    private LinearLayout mBtnCinema, mBtnMovie, mBtnUser, mBtnProduct, mBtnShowtime, mBtnTicket;

    private Home() {
        super(R.layout.frag_admin_home);
    }

    public static Home getInstance() {
        if (_instance == null)
            _instance = new Home();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        bindingViews(view);
        setupViews(view);
        return view;
    }

    private void bindingViews(@Nullable View view) {
        mBtnCinema = view.findViewById(R.id.btn_cinema);
        mBtnMovie = view.findViewById(R.id.btn_movie);
        mBtnUser = view.findViewById(R.id.btn_user);
        mBtnProduct = view.findViewById(R.id.btn_product);
        mBtnShowtime = view.findViewById(R.id.btn_showtime);
        mBtnTicket = view.findViewById(R.id.btn_ticket);
    }

    private void setupViews(@Nullable View view) {
        mBtnCinema.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageCinemas.getInstance()));
        mBtnMovie.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageMovies.getInstance()));
        mBtnUser.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageUsers.getInstance()));
        mBtnProduct.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageProducts.getInstance()));
        mBtnShowtime.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageShowtimes.getInstance()));
        mBtnTicket.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageTickets.getInstance()));
    }
}
