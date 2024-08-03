package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Movie;

public class MovieController extends GenericController<Movie> {
    private static MovieController _instance = null;

    private MovieController() {
        super("movies", Movie.class);
    }

    public static MovieController getInstance() {
        if (_instance == null)
            _instance = new MovieController();
        return _instance;
    }
}
