package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.R;

import java.util.Map;

public class Home extends Fragment {
    private static Home _instance = null;

    private LinearLayout layoutManagementCategories;

    Map<String, Fragment> managementCategories = Map.of(
        "Người dùng", ManageUsers.getInstance(),
        "Sản phẩm", ManageProducts.getInstance(),
        "Phim", ManageMovies.getInstance(),
        "Chi nhánh", ManageCinemas.getInstance()
    );

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
        layoutManagementCategories = view.findViewById(R.id.layout_management_categories);
    }

    private void setupViews(@Nullable View view) {
        managementCategories.forEach((k, v) -> {
            Button btn = new Button(view.getContext());
            btn.setText(String.format("Quản lý %s", k));
            btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), v));
            layoutManagementCategories.addView(btn);
        });
    }
}
