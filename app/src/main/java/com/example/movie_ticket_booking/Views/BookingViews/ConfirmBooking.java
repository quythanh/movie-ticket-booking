package com.example.movie_ticket_booking.Views.BookingViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Components.ConfirmedProductAdapter;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Controllers.ProductController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.Models.SeatType;
import com.example.movie_ticket_booking.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmBooking extends AppCompatActivity {

    private TextView movie, showtime, cinema, room, vip, normal, couple, total;
    private Bundle bundle;
    private LinearLayout normalSession, vipSession, coupleSession;
    private long totalInt;
    private ListView prods;
    private ConfirmedProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        initial();
        getData();
    }

    private void initial(){
        movie = findViewById(R.id.finalMovieTitle);
        showtime = findViewById(R.id.finalShowtimeDate);
        cinema = findViewById(R.id.finalCinemaTitle);
        room = findViewById(R.id.finalRoomNumber);
        vip = findViewById(R.id.finalVipSeat);
        normal = findViewById(R.id.finalNormalSeat);
        couple = findViewById(R.id.finalCoupleSeat);
        total = findViewById(R.id.finalTotalPrice);
        normalSession = findViewById(R.id.standardSession);
        vipSession = findViewById(R.id.vipSession);
        coupleSession = findViewById(R.id.coupleSession);
        prods = findViewById(R.id.products);

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("ticket");

        totalInt = 0;
    }

    private void getData(){
        ShowtimeController.getInstance().getLiveData(bundle.getString("showtime")).observe(this, showtime1 -> {
            if(showtime1 == null ){
                return;
            }
            showtime.setText(Constant.DATETIME_FORMATTER.format(showtime1.getDate()));
            MovieController.getInstance().getLiveData(showtime1.getMovie()).observe(this, movie1 -> {
                if(movie1 == null){
                    return;
                }
                movie.setText(movie1.getTitle());
            });

            showtime1.getRoom().get().addOnSuccessListener(documentSnapshot -> {
                Room room1 = documentSnapshot.toObject(Room.class);
                room1.setId(documentSnapshot.getId());

                room.setText("P" + room1.getRoomNumber());

                showtime1.getRoom().getParent().getParent().get().addOnSuccessListener(documentSnapshot1 -> {
                    Cinema cinema1 = documentSnapshot1.toObject(Cinema.class);
                    cinema.setText("TP CINE " + cinema1.getName());
                });
            });

            String str = "";
            if(bundle.get(SeatType.STANDARD.toString()) != null){
                List<String> normalSeat = bundle.getStringArrayList(SeatType.STANDARD.toString());
                for(String x : normalSeat){
                    str += " " + x;
                }
                normal.setText(str);
                normalSession.setVisibility(View.VISIBLE);
                totalInt += SeatType.STANDARD.price * 1000 * normalSeat.size();
            }

            str = "";
            if(bundle.get(SeatType.VIP.toString()) != null){
                List<String> vipSeat = bundle.getStringArrayList(SeatType.VIP.toString());
                for(String x : vipSeat){
                    str += " " + x;
                }
                vip.setText(str);
                vipSession.setVisibility(View.VISIBLE);
                totalInt += SeatType.VIP.price * 1000 * vipSeat.size();
            }

            str = "";
            if(bundle.get(SeatType.COUPLE.toString()) != null){
                List<String> coupleSeat = bundle.getStringArrayList(SeatType.COUPLE.toString());
                for(String x : coupleSeat){
                    str += " " + x;
                }
                couple.setText(str);
                coupleSession.setVisibility(View.VISIBLE);
                totalInt += SeatType.COUPLE.price * 1000 * coupleSeat.size();
            }

            if(bundle.get("products") !=null){
                Bundle b = bundle.getBundle("products");
                ProductController.getInstance().getAll().observe(this, products -> {
                    if(products == null) {
                        return;
                    }
                    Map<Product, Integer> map = new HashMap<>();
                    products.forEach(x -> {
                        if(b.get(x.getId()) != null){
                            int a = b.getInt(x.getId());
                            total.setText(String.format("%d", x.getUnitPrice()*a));
                            map.put(x,a);
                            totalInt += x.getUnitPrice() * a;
                        }
                    });

                    adapter = new ConfirmedProductAdapter(map);
                    prods.setAdapter(adapter);

                    ViewGroup.LayoutParams params = prods.getLayoutParams();
                    params.height = map.size() * 450;
                    prods.setLayoutParams(params);

                    total.setText(Constant.currencyFormatter.format(totalInt));
                });
            }
            total.setText(Constant.currencyFormatter.format(totalInt));
        });
    }
}