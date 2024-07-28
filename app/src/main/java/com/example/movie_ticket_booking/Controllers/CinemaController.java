package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Cinema;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class CinemaController {
    @Getter
    private static List<Cinema> cinemas;

    static {
        cinemas = new ArrayList<>();
    }
    private CinemaController(){};
}
