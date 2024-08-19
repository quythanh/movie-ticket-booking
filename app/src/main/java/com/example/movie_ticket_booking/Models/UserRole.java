package com.example.movie_ticket_booking.Models;

public enum UserRole {
    ALL(null), // Use for filter only
    ADMIN("Quản trị viên"),
    CLIENT("Khách hàng");

    public final String prefix;

    private UserRole(String prefix) {
        this.prefix = prefix;
    }
}
