package com.example.movie_ticket_booking.Views.BookingViews;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Components.ConfirmedProductAdapter;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Controllers.ProductController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Controllers.TicketController;
import com.example.movie_ticket_booking.Controllers.UserController;
import com.example.movie_ticket_booking.MainActivity;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.DetailTicket;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.Models.ProductInTicket;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.Models.Seat;
import com.example.movie_ticket_booking.Models.SeatType;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.R;
import com.example.movie_ticket_booking.Views.FilmFragment;

import java.util.ArrayList;
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
    private Button confirm, pay, back, home;
    private Ticket ticket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        initial();
        getData();
        staticBinding();
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
        pay = findViewById(R.id.payBtn);
        confirm = findViewById(R.id.confirmBtn);
        back = findViewById(R.id.backBtn2);
        home =findViewById(R.id.homeBtn);

        ticket = new Ticket();

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("ticket");

        totalInt = 0;
    }

    private void getData(){
        CinemaController.getInstance().get(bundle.getString("cinema")).observe(this, cinema1 -> {
            if(cinema1 == null) return;
            cinema.setText("TP CINE " + cinema1.getName());

            ticket.setShowtime(ShowtimeController.getInstance(cinema1).TryGet(bundle.getString("showtime")));
            ticket.setUser(UserController.getInstance().TryGet(AuthUserController.getInstance().getUserlogin().getValue().getId()));

            List<DetailTicket> temp = new ArrayList<>();

            ShowtimeController.getInstance(cinema1).getLiveData(bundle.getString("showtime")).observe(this, showtime1 -> {
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
                });

                String str = "";
                if(bundle.get(SeatType.STANDARD.toString()) != null){
                    List<String> normalSeat = bundle.getStringArrayList(SeatType.STANDARD.toString());
                    for(String x : normalSeat){
                        str += " " + x;
                        DetailTicket t = new Seat(DetailTicket.DetailType.SEAT, x, SeatType.STANDARD);
                        temp.add(t);
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
                        DetailTicket t = new Seat(DetailTicket.DetailType.SEAT, x, SeatType.VIP);
                        temp.add(t);
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
                        DetailTicket t = new Seat(DetailTicket.DetailType.SEAT, x, SeatType.COUPLE);
                        temp.add(t);
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
                                map.put(x,a);
                                totalInt += x.getUnitPrice() * a;
                                DetailTicket t = new ProductInTicket(DetailTicket.DetailType.PRODUCT, ProductController.getInstance().TryGet(x.getId()), a);
                                temp.add(t);
                            }
                        });

                        adapter = new ConfirmedProductAdapter(map);
                        prods.setAdapter(adapter);

                        ViewGroup.LayoutParams params = prods.getLayoutParams();
                        params.height = map.size() * 520;
                        prods.setLayoutParams(params);

                        total.setText(Constant.currencyFormatter.format(totalInt));
                        ticket.setTotal((int) totalInt);
                        ticket.setDetails(temp);
                    });
                }
                total.setText(Constant.currencyFormatter.format(totalInt));
                ticket.setTotal((int) totalInt);
                ticket.setDetails(temp);
            });

        });
    }
    private void staticBinding(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ticket== null || ticket.getShowtime() == null) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmBooking.this);
                builder.setTitle("Xác nhận vé xem phim!")
                                .setMessage("Bạn đang muốn gửi yêu cầu xác nhận vé cảu bạn?")
                                .setIcon(R.drawable.ic_movie)
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TicketController.getInstance().add(ticket);
                                        confirm.setVisibility(View.GONE);
                                        pay.setVisibility(View.VISIBLE);
                                        back.setVisibility(View.GONE);
                                        home.setVisibility(View.VISIBLE);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
            }
        });
    }
}