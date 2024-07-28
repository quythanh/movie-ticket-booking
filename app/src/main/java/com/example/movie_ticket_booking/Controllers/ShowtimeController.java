package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Showtime;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ShowtimeController {
    private static List<Showtime> showtimes;
    static {
        showtimes = new ArrayList<>();
    }

    private  ShowtimeController() {}
}
