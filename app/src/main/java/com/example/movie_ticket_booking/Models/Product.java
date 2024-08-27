package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseModel {
    private String name;
    private int unitPrice;
    private String description;

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
