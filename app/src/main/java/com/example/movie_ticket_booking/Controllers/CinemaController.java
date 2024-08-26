package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Models.Address;
import com.example.movie_ticket_booking.Models.Cinema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CinemaController extends GenericController<Cinema> {
    private static CinemaController _instance = null;

    private CinemaController() {
        super("cinemas", Cinema.class);
    }

    public static synchronized CinemaController getInstance() {
        if (_instance == null)
            _instance = new CinemaController();
        return _instance;
    }

    public Map<String, List<Cinema>> reorderedMap(List<Cinema> cinemas) {
        AuthUserController controller = AuthUserController.getInstance();
        Map<String, List<Cinema>> map = new HashMap<>();

        map.put("closest", new ArrayList<>());

        cinemas.forEach(x -> {
            if (x.getAddress().distance(controller.getLatitudeLocation().getValue(), controller.getLongtitudeLocation().getValue()) <= 20)
                map.get("closest").add(x);
            map.putIfAbsent(x.getAddress().getProvince(), new ArrayList<>());
            map.get(x.getAddress().getProvince()).add(x);
        });

        return map;
    }
}
