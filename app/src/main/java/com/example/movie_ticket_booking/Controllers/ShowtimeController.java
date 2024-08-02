package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Showtime;

public class ShowtimeController extends GenericController<Showtime> {
    private static ShowtimeController _instance = null;

    private ShowtimeController() {
        super("showtimes", Showtime.class);
    }

    public static synchronized ShowtimeController getInstance() {
        if (_instance == null)
            _instance = new ShowtimeController();
        return _instance;
    }
}
