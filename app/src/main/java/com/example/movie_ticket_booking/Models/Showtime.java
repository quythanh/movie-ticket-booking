package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Showtime extends BaseModel {
    private Movie movie;
    private Room room;
    private Date date;
    private List<Ticket> tickets;

    @NonNull
    @Override
    public String toString() {
        return String.format("%s - %s", this.movie, this.date);
    }
}
