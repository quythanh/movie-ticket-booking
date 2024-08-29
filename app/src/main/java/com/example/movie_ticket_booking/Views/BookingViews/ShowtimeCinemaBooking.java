package com.example.movie_ticket_booking.Views.BookingViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Components.CinemaAdapter;
import com.example.movie_ticket_booking.Components.ShowtimeGridAdapter;
import com.example.movie_ticket_booking.Components.WeekdateApdater;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ShowtimeCinemaBooking extends AppCompatActivity {

    private RecyclerView rcv, cine;
    private CinemaController cc = CinemaController.getInstance();
    private GridView grid;
    private MutableLiveData<Map<String, Object>> data;
    private TextView sd, sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_cinema_booking);

        data = new MutableLiveData<>(new HashMap<>());
        GenericFilter<Cinema> filters = new GenericFilter<>(Cinema.class);
        AtomicReference<Map<String, Object>> tempMap = new AtomicReference<>(new HashMap<>());

        rcv = findViewById(R.id.weekdate);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        WeekdateApdater week = new WeekdateApdater(getApplicationContext());
        rcv.setAdapter(week);

        cine = findViewById(R.id.booking_cinema_choosing);
        cine.setHasFixedSize(true);
        cine.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        grid = findViewById(R.id.grid_showtime);
        sd = findViewById(R.id.selectedDate);
        sc = findViewById(R.id.selectedCinema);

        Intent intent = getIntent();
        filters.set(FilterType.ARRAY_CONTAINS, "nowPresenting", intent.getStringExtra("movie_id"));

        CinemaAdapter cinemaAdapter = new CinemaAdapter(new ArrayList<>(), CinemaAdapter.EventType.ON_BOOKING);
        cc.filter(filters.get()).observe(this, cinemas1 -> {
            Log.d("qq", cinemas1.toString());
            cinemaAdapter.setCinemas(cinemas1);
            cine.setAdapter(cinemaAdapter);
        });

        week.getDate_data().observe(this, date -> {
            if(date == null) return;
            sd.setText(Constant.DATE_FORMATTER.format(date));
            tempMap.set(data.getValue());
            tempMap.get().put("date", date);
            data.setValue(tempMap.get());
            Log.d("qqz", "helllllllll" + data.getValue().toString());
        });
        cinemaAdapter.getCinema_data().observe(this, cinema -> {
            if(cinema == null) return;
            sc.setText(cinema.getName());
            tempMap.set(data.getValue());
            tempMap.get().put("cinema", cinema);
            data.setValue(tempMap.get());
            Log.d("qqz", "helllllllll" + data.getValue().toString());
        });

        data.observe(this, map -> {
            if(map.get("cinema") == null || map.get("date") == null) return;
            Cinema c = (Cinema) map.get("cinema");
            Date d = (Date) map.get("date");
            try {
                ShowtimeController.getInstance(c).getShowtime(intent.getStringExtra("movie_id"), d).observe(this, showtimes -> {
                    Log.d("qqz", "hello " + showtimes.toString());
                    if(showtimes.isEmpty() || showtimes==null)
                        grid.setAdapter(null);
                    else {
                        ShowtimeGridAdapter gridAdapter = new ShowtimeGridAdapter(showtimes);
                        grid.setAdapter(gridAdapter);
                        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent ToChoseSeat = new Intent(getApplicationContext(), SeatBooking.class);
                                Showtime s = (Showtime) gridAdapter.getItem(position);
                                ToChoseSeat.putExtra("showtime", s.getId());
                                ToChoseSeat.putExtra("cinema", c.getId());
                                startActivity(ToChoseSeat);
                            }
                        });
                    }
                });

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}