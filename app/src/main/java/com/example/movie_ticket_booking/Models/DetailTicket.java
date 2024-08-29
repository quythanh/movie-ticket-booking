package com.example.movie_ticket_booking.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class DetailTicket {
    protected DetailType detailType;


    public static enum DetailType{
        SEAT,
        PRODUCT
    }
}
