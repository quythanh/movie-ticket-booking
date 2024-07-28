package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Movie;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class MovieController {
    private static List<Movie> movies;

    static {
        movies = new ArrayList<>();
    }

    private MovieController(){};

}
