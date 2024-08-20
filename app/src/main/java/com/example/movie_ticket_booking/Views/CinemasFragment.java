package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.movie_ticket_booking.Components.CinemaAdapter;
import com.example.movie_ticket_booking.Components.CinemaControllerAdapter;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.RoomController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CinemasFragment extends Fragment {
    private static CinemasFragment _instance = null;

    private MutableLiveData<Map<String, List<Cinema>>> data;
    private CinemaController controller;
    private LiveData<List<Cinema>> cinemas;
    private RecyclerView closest, filter;

    private CinemasFragment() {
        super(R.layout.activity_cinema_fragment);
    }

    public static CinemasFragment getInstance() {
        if (_instance == null)
            _instance = new CinemasFragment();
        return _instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        bindingViews(view);
        setupViews(view);
        return view;
    }

    private void bindingViews(View view) {
        closest = view.findViewById(R.id.closest);
        filter = view.findViewById(R.id.cinemasController);
    }

    private void setupViews(View view) {
        closest.setHasFixedSize(true);
        closest.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        filter.setHasFixedSize(true);
        filter.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false));

        final boolean[] initial = {false};
        final int[] counter = {0};
        controller = CinemaController.getInstance();
        data = new MutableLiveData<>();
        cinemas = controller.getAll();

        CinemaAdapter closestCinemaAdapter = new CinemaAdapter(new ArrayList<>());
        CinemaControllerAdapter cinemaControllerAdapter = new CinemaControllerAdapter(new HashMap<String, List<Cinema>>());

        //OBSERVER
        cinemas.observe(getViewLifecycleOwner(), cinemas1 -> {
            if (cinemas1 == null) return;
            AuthUserController.getInstance().getLatitudeLocation().observe(getViewLifecycleOwner(),latitude -> {
                Toast.makeText(view.getContext(), "Lat: " + latitude, Toast.LENGTH_SHORT).show();
                counter[0] += 1;

                initial[0] = true;
                data.setValue(controller.reorderedMap(cinemas1));

            });
            AuthUserController.getInstance().getLongtitudeLocation().observe(getViewLifecycleOwner(),latitude -> {
                Toast.makeText(view.getContext(), "Long: " + latitude, Toast.LENGTH_SHORT).show();
                counter[0] += 1;
                if(!initial[0])
                {
                    initial[0] = true;
                    data.setValue(controller.reorderedMap(cinemas1));
                }
            });
        });
        
        data.observe(getViewLifecycleOwner(), map -> {
            if(map == null)
                return;
            Log.d("cinemas", map.get("tp HCM").get(0).toString());
            List<Cinema> closestList = map.remove("closest");
            closestCinemaAdapter.setCinemas(closestList);
            closest.setAdapter(closestCinemaAdapter);


            cinemaControllerAdapter.setCinemas(map);
            filter.setAdapter(cinemaControllerAdapter);
        });
    }
}