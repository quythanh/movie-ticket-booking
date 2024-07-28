package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

public class Product extends BaseModel{
    private String name;
    private int unitPrice;

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
