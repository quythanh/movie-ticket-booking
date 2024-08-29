package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Components.TicketListAdapter;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.TicketController;
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

    private ListView list;
    private TicketListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if(AuthUserController.getInstance().getUserlogin().getValue().getId() == null)
            UIManager.changeFragment(getParentFragmentManager(), LoginFragment.getInstance());
        else {
            initial(view);
            getData();
        }
        return view;
    }

    private void initial(View v){
        list = v.findViewById(R.id.tickets);
    }

    private void getData(){
        TicketController.getInstance().getMyTickets().observe(getViewLifecycleOwner(), tickets -> {
            if(tickets == null) return;
            adapter = new TicketListAdapter(getViewLifecycleOwner(), tickets);
            list.setAdapter(adapter);
        });
    }
}