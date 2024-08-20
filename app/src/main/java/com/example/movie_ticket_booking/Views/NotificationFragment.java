package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movie_ticket_booking.R;

public class NotificationFragment extends Fragment {
    private static NotificationFragment _instance = null;

    private NotificationFragment() {
        super(R.layout.activity_notification_fragment);
    }

    public static NotificationFragment getInstance() {
        if (_instance == null)
            _instance = new NotificationFragment();
        return _instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}