package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.Models.Showtime;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ShowtimeController extends GenericController<Showtime> {
    private static ShowtimeController _instance = null;

    private ShowtimeController() {
        super("showtimes", Showtime.class);
    }

    public static synchronized ShowtimeController getInstance(Cinema c) {
        if (_instance == null)
            _instance = new ShowtimeController();
        _instance.collectionPath = String.format("/cinemas/%s/showtimes", c.getId());
        return _instance;
    }

    public LiveData<List<Showtime>> getShowtime(String movieId, Date date) throws ParseException {
        GenericFilter<Showtime> filters = new GenericFilter<>(Showtime.class);

        Date startDate = Constant.DATETIME_FORMATTER.parse(Constant.DATE_FORMATTER.format(date) + " 00:00");
        Date endDate = (Date) startDate.clone();
        Date now = new Date();

        endDate.setTime(startDate.getTime() + 1000*60*60*24);

        Timestamp start = new Timestamp(startDate.before(now) ? now : startDate);
        Timestamp end = new Timestamp(endDate);

        filters.set(FilterType.EQUAL, "movie", movieId);
        filters.set(FilterType.GREATER_OR_EQUAL, "date", start);
        filters.set(FilterType.LESS, "date", end);

        // .orderBy("date")

        return this.filter(filters.get());
    }
}
