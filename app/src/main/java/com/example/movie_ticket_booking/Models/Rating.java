package com.example.movie_ticket_booking.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating extends BaseModel{
//    private Ticket ticket;
    private String user;
    private String movie;
    private int stars;
    private String comment;

    @Override
    public String toString() {
        return String.format("Bình luận của %s cho phim %s", user, movie);
    }
}
