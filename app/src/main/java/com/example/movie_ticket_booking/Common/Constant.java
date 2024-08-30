package com.example.movie_ticket_booking.Common;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;

public class Constant {
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    public static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vn", "VN"));

    public enum Time {
        DAY(24 * 60 * 60 * 1000, "Ngày"),
        DAY_3(3 * 24 * 60 * 60 * 1000, "3 Ngày"),
        WEEK(7 * 24 * 60 * 60 * 1000, "Tuần"),
        WEEK_2(14 * 24 * 60 * 60 * 1000, "2 Tuần"),
        MONTH(30 * 24 * 60 * 60 * 1000, "Tháng"),
        QUARTER(90 * 24 * 60 * 60 * 1000, "Quý"),
        YEAR(365 * 24 * 60 * 60 * 1000, "Năm");

        public final long value;
        private final String label;

        private Time(long length, String label) {
            this.value = length;
            this.label = label;
        }

        @NonNull
        @Override
        public String toString() {
            return this.label;
        }
    }

    static {
        currencyFormatter.setCurrency(Currency.getInstance("VND"));
    }
}
