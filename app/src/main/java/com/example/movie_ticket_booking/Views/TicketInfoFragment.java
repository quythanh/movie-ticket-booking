package com.example.movie_ticket_booking.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movie_ticket_booking.Api.CreateOrder;
import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Components.ConfirmedProductAdapter;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Controllers.TicketController;
import com.example.movie_ticket_booking.MainActivity;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.DetailTicket;
import com.example.movie_ticket_booking.Models.Payment;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.Models.ProductInTicket;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.Models.Seat;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.R;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPaySDK;

public class TicketInfoFragment extends Fragment {
    private static TicketInfoFragment _instance;


    @Getter
    private static MutableLiveData<Ticket> ticket = new MutableLiveData<>();
    private TextView movie, date, cinema, room, vip, normal, couple, total, status, cancel;
    private LinearLayout vips, normals, couples;
    private ListView products;
    private ConfirmedProductAdapter adapter;
    private Map<Product, Integer> productsMap;
    private Button pay, canceled;

    public static TicketInfoFragment getInstance(){
        if(_instance == null)
            _instance = new TicketInfoFragment();
        return _instance;
    }

    private TicketInfoFragment() {
        super(R.layout.fragment_ticket_info);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initial(view);
        Binding();
        return view;
    }

    private void initial(View view){
        movie = view.findViewById(R.id.ticketMovieTitle);
        date = view.findViewById(R.id.ticketShowtimeDate);
        cinema = view.findViewById(R.id.ticketCinemaTitle);
        room = view.findViewById(R.id.ticketRoomNumber);
        total = view.findViewById(R.id.ticketTotalPrice);
        status = view.findViewById(R.id.ticketStatusInfo);
        cancel = view.findViewById(R.id.ticketCanceledTime);
        pay = view.findViewById(R.id.payBtn);
        canceled = view.findViewById(R.id.CancelBtn);

        vip = view.findViewById(R.id.ticketVipSeat);
        normal = view.findViewById(R.id.ticketNormalSeat);
        couple = view.findViewById(R.id.ticketCoupleSeat);

        vips = view.findViewById(R.id.vipSession);
        normals = view.findViewById(R.id.standardSession);
        couples = view.findViewById(R.id.coupleSession);

        products = view.findViewById(R.id.ticketProducts);

        productsMap = new HashMap<>();
        adapter = new ConfirmedProductAdapter(productsMap);
        products.setAdapter(adapter);

        //zalo pay
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
    }
    private void Binding(){

        if(ticket == null)
        {
                UIManager.changeFragment(getParentFragmentManager(), NotificationFragment.getInstance());
        }
        else {
        ticket.observe(getViewLifecycleOwner(), ticket1 -> {
            if(ticket1 == null) return;
            ticket1.getShowtime().get().addOnSuccessListener(documentSnapshot -> {
                Showtime s = documentSnapshot.toObject(Showtime.class);
                s.setId(documentSnapshot.getId());

                date.setText(Constant.DATETIME_FORMATTER.format(s.getDate()));
                MovieController.getInstance().get(s.getMovie()).observe(getViewLifecycleOwner(), movie1 -> {
                    movie.setText(movie1.getTitle());
                });
                s.getRoom().get().addOnSuccessListener(command -> {
                    Room r = command.toObject(Room.class);
                    room.setText(String.format("%d", r.getRoomNumber()));
                });

                s.getRoom().getParent().get().addOnSuccessListener(command -> {
                    Cinema c = command.toObjects(Cinema.class).get(0);
                    cinema.setText(c.getName());
                });
            });
            total.setText(Constant.currencyFormatter.format(ticket1.getTotal()));
            String vipt = "", normalt= "", couplet="";
            for(DetailTicket dt : ticket1.getDetails()){
                if(dt.getDetailType() == DetailTicket.DetailType.SEAT){
                    Seat temp = (Seat) dt;
                    switch (temp.getType()){
                        case VIP:{
                            vipt += String.format(" %s", temp.getSeatNumber());
                            break;
                        }
                        case STANDARD:{
                            normalt += String.format(" %s", temp.getSeatNumber());
                            break;
                        }
                        case COUPLE:{
                            couplet += String.format(" %s", temp.getSeatNumber());
                            break;
                        }
                    }
                }
                else {
                    ProductInTicket pit = (ProductInTicket) dt;
                    pit.getProduct().get().addOnSuccessListener(command -> {
                        Product p = command.toObject(Product.class);
                        p.setId(command.getId());

                        adapter.getProds().put(p,pit.getAmount());
                        products.invalidateViews();

                        ViewGroup.LayoutParams params = products.getLayoutParams();
                        params.height = adapter.getCount() * 500;
                        products.setLayoutParams(params);
                    });
                }
            }

            if(!vipt.isBlank())
            {
                vips.setVisibility(View.VISIBLE);
                vip.setText(vipt);
            }
            if(!normalt.isBlank())
            {
                normals.setVisibility(View.VISIBLE);
                normal.setText(normalt);
            }
            if(!couplet.isBlank())
            {
                couples.setVisibility(View.VISIBLE);
                couple.setText(couplet);
            }

            cancel.setText(Constant.DATETIME_FORMATTER.format(ticket1.getCancelableTime()));
            if(ticket1.isActive()){
                if(ticket1.isPaid()) {
                    status.setText("ĐÃ THANH TOÁN");
                    status.setTextColor(Color.parseColor("#5EFF00"));
                    pay.setVisibility(View.GONE);
                    canceled.setVisibility(View.GONE);
                }
                else {
                    status.setText("ĐÃ ĐẶT");
                    status.setTextColor(Color.parseColor("#F6AA30"));
                }
            }else {
                status.setText("ĐÃ HỦY");
                status.setTextColor(Color.parseColor("#FF0015"));
                canceled.setVisibility(View.GONE);
                pay.setVisibility(View.GONE);
            }

            if(ticket1.getCancelableTime().before(new Date())) canceled.setVisibility(View.GONE);
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateOrder orderApi = new CreateOrder();
                    try {
                        JSONObject data = orderApi.createOrder(String.format("%d", ticket1.getTotal()));
                        Log.d("Amount", String.format("%d", ticket1.getTotal()));
                        String code = data.getString("return_code");

                        if (code.equals("1")) {
                            String token = data.getString("zp_trans_token");
                            Payment p = new Payment(new Date(), token);
                            ticket1.setPayment(p);
                            ticket1.setPaid(true);
                            try {
                                TicketController.getInstance().update(ticket1.getId(), ticket1);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(v.getContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            UIManager.changeFragment(getParentFragmentManager(), NotificationFragment.getInstance());

//                        ZaloPaySDK.getInstance().payOrder(ConfirmBooking.this, token, "demozpdk://app", new PayOrderListener() {
//                            @Override
//                            public void onPaymentSucceeded(String s, String s1, String s2) {
//                                Toast.makeText(v.getContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
//                                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
//                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(intent1);
//                            }
//
//                            @Override
//                            public void onPaymentCanceled(String s, String s1) {
//                                Toast.makeText(v.getContext(), "Thanh toán đã được hủy", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
//                                Toast.makeText(v.getContext(), "Thanh toán không thành công", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            canceled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date now = new Date();
                    if(now.before(ticket1.getCancelableTime())){
                        ticket1.setActive(false);
                        try {
                            TicketController.getInstance().update(ticket1.getId(), ticket1);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(MainActivity.getInstance().getApplicationContext(), "Hủy thành công", Toast.LENGTH_SHORT).show();
                        UIManager.changeFragment(getParentFragmentManager(), NotificationFragment.getInstance());
                    }
                }
            });
        });

        }
    }
}