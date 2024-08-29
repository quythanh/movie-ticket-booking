package com.example.movie_ticket_booking.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seat extends DetailTicket{
    private String seatNumber;
    private SeatType type;

    public Seat(DetailTicket.DetailType T, String n, SeatType t){
        super(T);
        this.seatNumber = n;
        this.type = t;
    }
    @Override
    public String toString() {
        return String.format("%s - %s", this.type.name(), this.seatNumber);
    }
}
