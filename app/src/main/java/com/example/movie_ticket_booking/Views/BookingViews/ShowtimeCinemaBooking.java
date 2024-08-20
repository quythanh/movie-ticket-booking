package com.example.movie_ticket_booking.Views.BookingViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Common;
import com.example.movie_ticket_booking.Components.CinemaControllerAdapter;
import com.example.movie_ticket_booking.Components.ShowtimeGridAdapter;
import com.example.movie_ticket_booking.Components.WeekdateApdater;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ShowtimeCinemaBooking extends AppCompatActivity {

    private RecyclerView rcv, cine;
    private LiveData<List<Cinema>> cinemas;
    private CinemaController cc = CinemaController.getInstance();
    private GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_cinema_booking);

        rcv = findViewById(R.id.weekdate);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        WeekdateApdater week = new WeekdateApdater(getApplicationContext());
        rcv.setAdapter(week);

        cine = findViewById(R.id.booking_cinema_choosing);
        cine.setHasFixedSize(true);
        cine.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        grid = findViewById(R.id.grid_showtime);

        Intent intent = getIntent();
        cinemas = new MutableLiveData<>();
        cinemas = cc.getCinemaPresenting(intent.getStringExtra("movie_id"));
        cinemas.observe(this, cinemas1 -> {
            Log.d("qq", cinemas1.toString());
            CinemaControllerAdapter cine_controller = new CinemaControllerAdapter(CinemaController.getInstance().reorderedMap(cinemas1));
            cine.setAdapter(cine_controller);
        });
        CinemaController.getInstance().getLiveData("WfIqznm1zxCnLoywARBU").observe(this, cinema -> {
            try {
                if(cinema == null) return;
                ShowtimeController.getInstance().getShowtime("82x3BStVeLfsIRIhLHyw", cinema, Common.dateFormatter.parse("25/08/2024")).observe(this, showtimes -> {
                    Log.d("qqz", showtimes.toString());
                    ShowtimeGridAdapter gridAdapter = new ShowtimeGridAdapter(getApplicationContext(),showtimes);
                    grid.setAdapter(gridAdapter);
                });
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });


    }
}