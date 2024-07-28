package com.example.movie_ticket_booking.Models;

import java.util.List;

public class Tag extends BaseModel{
    private String title;
    private List<Movie> movies;

    @Override
    public String toString() {
        return this.title;
    }
}
