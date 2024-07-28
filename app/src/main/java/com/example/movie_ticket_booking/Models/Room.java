package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room extends BaseModel{
    private int roomNumber;
    private Cinema cinema;
    private List<Integer> seats;

    @NonNull
    @Override
    public String toString() {
        return String.format("Phòng %d - rạp %s", this.roomNumber, this.cinema.toString());
    }
}
