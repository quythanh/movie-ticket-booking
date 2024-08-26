package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cinema extends BaseModel {
    private String name;
    private Address address;
    private List<String> nowPresenting;
    private List<String> showtimes;

    public Cinema(String name, Address address) {
        this.name = name;
        this.address = address;
        nowPresenting = new ArrayList<>();
        showtimes = new ArrayList<>();
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.address.toString());
    }
}
