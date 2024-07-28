package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String street;
    private String district;
    private String province;

    @NonNull
    @Override
    public String toString() {
        return String.format("%s, %s, %s", this.street, this.district, this.province);
    }
}
