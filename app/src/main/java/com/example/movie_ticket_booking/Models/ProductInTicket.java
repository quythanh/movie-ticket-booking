package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

public class ProductInTicket extends DetailTicket{
    private Product product;
    private int amount;

    @NonNull
    @Override
    public String toString() {
        return this.product.toString();
    }
}
