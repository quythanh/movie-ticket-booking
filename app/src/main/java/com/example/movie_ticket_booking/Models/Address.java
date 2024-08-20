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
public class Address {
    private String street;
    private String district;
    private String province;
    private double latitude;
    private double longitude;

    @NonNull
    @Override
    public String toString() {
        return String.format("%s, %s, %s", this.street, this.district, this.province);
    }
}
