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

    public static final class Time {
        public static final long SEC = 1000;
        public static final long MINUTE = 60 * SEC;
        public static final long HOUR = 60 * MINUTE;
        public static final long DAY = 24 * HOUR;
        public static final long WEEK = 7 * DAY;
        public static final long MONTH = 30 * DAY;
        public static final long QUARTER = 3 * MONTH;
    }

    static {
        currencyFormatter.setCurrency(Currency.getInstance("VND"));
    }
}
