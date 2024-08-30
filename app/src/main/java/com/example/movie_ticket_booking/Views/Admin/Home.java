package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.R;
import com.example.movie_ticket_booking.Views.FilmFragment;

public class Home extends Fragment {
    private static Home _instance = null;

    private Button mBtnLogout;
    private TextView mTxtName;
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
        setupViews();
        return view;
    }

    private void bindingViews(@Nullable View view) {
        mBtnCinema = view.findViewById(R.id.btn_cinema);
        mBtnLogout = view.findViewById(R.id.btn_logout);
        mBtnMovie = view.findViewById(R.id.btn_movie);
        mBtnUser = view.findViewById(R.id.btn_user);
        mBtnProduct = view.findViewById(R.id.btn_product);
        mBtnShowtime = view.findViewById(R.id.btn_showtime);
        mBtnTicket = view.findViewById(R.id.btn_ticket);
        mTxtName = view.findViewById(R.id.txt_name);
    }

    private void setupViews() {
        mBtnCinema.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageCinemas.getInstance()));
        mBtnLogout.setOnClickListener(_v -> AuthUserController.getInstance().Logout(getParentFragmentManager()));
        mBtnMovie.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageMovies.getInstance()));
        mBtnUser.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageUsers.getInstance()));
        mBtnProduct.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageProducts.getInstance()));
        mBtnShowtime.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageShowtimes.getInstance()));
        mBtnTicket.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), ManageTickets.getInstance()));

        mTxtName.setText(AuthUserController.getInstance().getUserlogin().getValue().getFullName());
    }
}
