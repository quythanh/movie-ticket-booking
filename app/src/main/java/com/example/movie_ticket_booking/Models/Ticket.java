package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket extends BaseModel{
    private User user;
    private Showtime showtime;
    private Rating rating;
    private List<Integer> details;
    private Integer total;

    @NonNull
    @Override
    public String toString() {
        return String.format("Vé xem phim: %s", this.showtime);
    }
}
