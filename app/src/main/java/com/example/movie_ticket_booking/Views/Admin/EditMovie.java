package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.R;

public class EditMovie extends Fragment {
    private static EditMovie _instance = null;

    private ImageView btnBack;

    private EditMovie() {
        super(R.layout.frag_admin_edit_movie);
    }

    public static EditMovie getInstance() {
        if (_instance == null)
            _instance = new EditMovie();
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
        btnBack = view.findViewById(R.id.btn_back);
    }

    private void setupViews() {
        btnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());
    }
}
