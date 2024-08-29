package com.example.movie_ticket_booking.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Showtime extends BaseModel {
    private String movie;
    private DocumentReference room;
    private Date date;

//    public Showtime(Movie movie, Room room, Date date) throws IllegalAccessException, InstantiationException {
//        this.movie = movie.getId();
//        this.room = room.getId();
//        this.date = date;
//        this.tickets = new ArrayList<>();
//    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s - %s", this.movie, this.date);
    }

}
