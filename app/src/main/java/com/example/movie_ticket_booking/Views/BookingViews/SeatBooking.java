package com.example.movie_ticket_booking.Views.BookingViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Components.SeatRowAdapter;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.Models.SeatType;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SeatBooking extends AppCompatActivity {

    private RecyclerView seatMap;
    private TextView result, normal, vip, couple, mname, sname, loading;
    private String showtime_id;
    private Button back, next;
    private Bundle bundle;
    private boolean isBooked;
    private void initial(){
        Intent intent = getIntent();
        result = findViewById(R.id.result);
        showtime_id = intent.getStringExtra("showtime");
        normal = findViewById(R.id.normalPrice);
        vip = findViewById(R.id.vipPrice);
        couple = findViewById(R.id.couplePrice);
        mname = findViewById(R.id.movieName);
        sname = findViewById(R.id.showtimeName);
        loading = findViewById(R.id.loading);
        back = findViewById(R.id.backBtn);
        next = findViewById(R.id.nextBtn);
        bundle = new Bundle();
        bundle.putString("showtime", showtime_id);
        isBooked = false;
    }
    private void getData(){
        ShowtimeController.getInstance().getLiveData(showtime_id).observe(this, showtime -> {
            if(showtime == null) {
                loading.setText("Không thể tìm thấy suất chiếu");
                return;
            }
            sname.setText(Constant.DATETIME_FORMATTER.format(showtime.getDate()));
            MovieController.getInstance().getLiveData(showtime.getMovie()).observe(this, movie -> {
                mname.setText(movie.getTitle());
            });

            showtime.getRoom().get().addOnSuccessListener(documentSnapshot -> {
                Room room = documentSnapshot.toObject(Room.class);
                room.setId(documentSnapshot.getId());

                loading.setVisibility(View.GONE);

                seatMap = findViewById(R.id.seatMap);
                seatMap.setHasFixedSize(true);
                seatMap.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                SeatRowAdapter seatRowAdapter = new SeatRowAdapter(getApplicationContext(),room.getSeats());
                seatMap.setAdapter(seatRowAdapter);

                AuthUserController.getInstance().getBookingSeat().observe(this, seatMap -> {
                    AtomicReference<String> t = new AtomicReference<>("");
                    isBooked = false;
                    for(Map.Entry<Character, List<Integer>> e : seatMap.entrySet()){

                        if(!e.getValue().isEmpty()){
                            String type = UIManager.getSeatType(e.getKey(), room.getSeats().size()).toString();
                            ArrayList<String> str = bundle.getStringArrayList(type) == null ? new ArrayList<>() : bundle.getStringArrayList(type);
                            isBooked = true;
                            e.getValue().forEach(x -> {
                                String value = String.format("%c%d", e.getKey(), x+1);
                                t.set(String.format("%s  %s", t.get(), value));
                                if(!str.contains(value))
                                    str.add(value);
                            });
                            bundle.putStringArrayList(type,str);
                        }
                    }
                    result.setText(t.get());
                });
            });

//            RoomController.getInstance().getLiveData(showtime.getRoom()).observe(this, room -> {
//                if(room == null) return;
//                loading.setVisibility(View.GONE);
//
//                seatMap = findViewById(R.id.seatMap);
//                seatMap.setHasFixedSize(true);
//                seatMap.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//
//                SeatRowAdapter seatRowAdapter = new SeatRowAdapter(getApplicationContext(),room.getSeats());
//                seatMap.setAdapter(seatRowAdapter);
//
//                AuthUserController.getInstance().getBookingSeat().observe(this, seatMap -> {
//                    AtomicReference<String> t = new AtomicReference<>("");
//                    isBooked = false;
//                    for(Map.Entry<Character, List<Integer>> e : seatMap.entrySet()){
//
//                        if(!e.getValue().isEmpty()){
//                            String type =  Common.getSeatType(e.getKey(), room.getSeats().size()).toString();
//                            ArrayList<String> str = bundle.getStringArrayList(type) == null ? new ArrayList<>() : bundle.getStringArrayList(type);
//                            isBooked = true;
//                            e.getValue().forEach(x -> {
//                                String value = String.format("%c%d", e.getKey(), x+1);
//                                t.set(String.format("%s  %s", t.get(), value));
//                                if(!str.contains(value))
//                                    str.add(value);
//                            });
//                            bundle.putStringArrayList(type,str);
//                        }
//                    }
//                    result.setText(t.get());
//                });
//            });
        });
    }

    private void staticBinding(){
        loading.setVisibility(View.VISIBLE);
        normal.setText(String.format("%d.000 đ", SeatType.STANDARD.price));
        vip.setText(String.format("%d.000 đ", SeatType.VIP.price));
        couple.setText(String.format("%d.000 đ", SeatType.COUPLE.price * 2));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBooked)
                {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn ít nhất 1 ghế", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent it = new Intent(getApplicationContext(), ComboBooking.class);
                it.putExtra("ticket", bundle);
                startActivity(it);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_booking);

        initial();
        staticBinding();
        getData();

    }
}