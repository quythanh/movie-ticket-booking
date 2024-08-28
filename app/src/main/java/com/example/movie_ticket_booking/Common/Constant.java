package com.example.movie_ticket_booking.Common;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;

public class Constant {
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    public static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vn", "VN"));

    static {
        currencyFormatter.setCurrency(Currency.getInstance("VND"));
    }
}
