package com.example.movie_ticket_booking.Models;

public enum UserRole {
    CLIENT("khách hàng"),
    ADMIN("Quản trị viên");

    public final String prefix;

    private UserRole(String pf){
        this.prefix = pf;
    }
}
