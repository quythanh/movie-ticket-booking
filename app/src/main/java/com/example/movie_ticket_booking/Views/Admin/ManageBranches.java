package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.R;

public class ManageBranches extends Fragment {
    private static ManageBranches _instance = null;

    private ManageBranches() {
        super(R.layout.frag_admin_manage_branches);
    }

    public static ManageBranches getInstance() {
        if (_instance == null)
            _instance = new ManageBranches();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
