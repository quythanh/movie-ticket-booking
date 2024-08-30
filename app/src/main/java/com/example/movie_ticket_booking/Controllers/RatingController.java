package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.Rating;

public class RatingController extends GenericController<Rating> {
    private static RatingController _instance = null;

    private RatingController(){super("ratings", Rating.class);}

    public static RatingController getInstance(Movie m){
        if(_instance == null)
            _instance = new RatingController();
        _instance.collectionPath = String.format("/movies/%s/ratings/", m.getId());
        return _instance;
    }
}
