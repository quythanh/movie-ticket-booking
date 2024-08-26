package com.example.movie_ticket_booking.Views.BookingViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Components.CinemaControllerAdapter;
import com.example.movie_ticket_booking.Components.ShowtimeGridAdapter;
import com.example.movie_ticket_booking.Components.WeekdateApdater;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.R;

import java.text.ParseException;
import java.util.List;

public class ShowtimeCinemaBooking extends AppCompatActivity {

    private RecyclerView rcv, cine;
    private final CinemaController cc = CinemaController.getInstance();
    private GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_cinema_booking);

        GenericFilter<Cinema> filters = new GenericFilter<>(Cinema.class);
        WeekdateApdater week = new WeekdateApdater(getApplicationContext());

        rcv = findViewById(R.id.weekdate);
        cine = findViewById(R.id.booking_cinema_choosing);
        grid = findViewById(R.id.grid_showtime);

        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rcv.setAdapter(week);

        cine.setHasFixedSize(true);
        cine.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        filters.set(FilterType.ARRAY_CONTAINS, "nowPresenting", getIntent().getStringExtra("movie_id"));

        cc.filter(filters.get()).observe(this, _cinema -> {
            CinemaControllerAdapter cine_controller = new CinemaControllerAdapter(cc.reorderedMap(_cinema));
            cine.setAdapter(cine_controller);
        });
        cc.get("WfIqznm1zxCnLoywARBU").observe(this, cinema -> {
            try {
                if (cinema == null) return;
                ShowtimeController.getInstance()
                        .getShowtime("82x3BStVeLfsIRIhLHyw", cinema, Constant.DATE_FORMATTER.parse("25/08/2024"))
                        .observe(this, showtimes -> {
                            ShowtimeGridAdapter gridAdapter = new ShowtimeGridAdapter(showtimes);
                            grid.setAdapter(gridAdapter);
                        });
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}