package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Cinema;

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
}
