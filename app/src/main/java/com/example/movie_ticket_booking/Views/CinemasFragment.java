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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CinemasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CinemasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MutableLiveData<Map<String, List<Cinema>>> data;
    private CinemaController controller;
    private LiveData<List<Cinema>> cinemas;
    private RecyclerView closest, filter;
    public CinemasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CinemasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CinemasFragment newInstance(String param1, String param2) {
        CinemasFragment fragment = new CinemasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final boolean[] initial = {false};
        final int[] counter = {0};
        controller = CinemaController.getInstance();
        data = new MutableLiveData<>();
        cinemas = controller.getAll();

        View view = inflater.inflate(R.layout.activity_cinema_fragment, container, false);

        closest = view.findViewById(R.id.closest);
        closest.setHasFixedSize(true);
        closest.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        filter = view.findViewById(R.id.cinemasController);
        filter.setHasFixedSize(true);
        filter.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false));

        CinemaAdapter closestCinemaAdapter = new CinemaAdapter(new ArrayList<>());
        CinemaControllerAdapter cinemaControllerAdapter = new CinemaControllerAdapter(new HashMap<String, List<Cinema>>());

        //OBSERVER
        cinemas.observe(getViewLifecycleOwner(), cinemas1 -> {
            if(cinemas1 == null) return;
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

        return view;
    }
}