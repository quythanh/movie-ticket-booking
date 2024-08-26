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
    private String ward;
    private String district;
    private String province;
    private double latitude;
    private double longitude;

    @NonNull
    @Override
    public String toString() {
        return String.format("%s, Phường %s, %s, %s", this.street, this.ward, this.district, this.province);
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    // d = 2R × sin⁻¹(√[sin²((θ₂ - θ₁)/2) + cosθ₁ × cosθ₂ × sin²((φ₂ - φ₁)/2)])
    public double distance(double latitude, double longitude) {
        double R = 6371;
        double dLat = deg2rad(latitude - this.latitude);
        double dLon = deg2rad(longitude - this.longitude);
        double a = Math.pow(Math.sin(dLat/2), 2) +Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(latitude))
                *Math.pow(Math.sin(dLon/2),2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return  R * c;
    }
}
