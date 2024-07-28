package com.example.movie_ticket_booking.Models;

public enum SeatType {
    STANDARD(60),
    VIP(80),
    COUPLE(70);

    public final int price;
    private SeatType(int price){
        this.price = price;
    }
}
