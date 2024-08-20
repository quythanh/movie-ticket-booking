package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.R;

public class ManageProducts extends Fragment {
    private static ManageProducts _instance = null;

    private ManageProducts() {
        super(R.layout.frag_admin_manage_products);
    }

    public static ManageProducts getInstance() {
        if (_instance == null)
            _instance = new ManageProducts();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(_view -> getParentFragmentManager().popBackStack());
        return view;
    }
}
