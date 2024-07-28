package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cinema extends BaseModel{
    private String name;
    private Address address;
    private List<Movie> nowPresenting;

    @NonNull
    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.address.toString());
    }
}
