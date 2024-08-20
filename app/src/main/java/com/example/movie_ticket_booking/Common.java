package com.example.movie_ticket_booking;

import java.text.SimpleDateFormat;

public class Common {

    public static SimpleDateFormat dateFormatter;
    public static SimpleDateFormat dateTimeFormatter;
    public static SimpleDateFormat timeFormatter;

    static {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        timeFormatter = new SimpleDateFormat("hh:mm");

    }
}
