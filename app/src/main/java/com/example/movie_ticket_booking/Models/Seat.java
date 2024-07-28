package com.example.movie_ticket_booking.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat extends DetailTicket{
    private String seatNumber;
    private SeatType type;

    @Override
    public String toString() {
        return String.format("%s - %s", this.type.name(), this.seatNumber);
    }
}
