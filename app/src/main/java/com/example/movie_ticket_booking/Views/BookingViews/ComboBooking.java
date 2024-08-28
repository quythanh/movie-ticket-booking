package com.example.movie_ticket_booking.Views.BookingViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.movie_ticket_booking.Components.BookingProductAdapter;
import com.example.movie_ticket_booking.Components.ProductAdapter;
import com.example.movie_ticket_booking.Controllers.ProductController;
import com.example.movie_ticket_booking.Models.SeatType;
import com.example.movie_ticket_booking.R;

import java.util.List;

public class ComboBooking extends AppCompatActivity {
    private Bundle bundle;
    private ListView lv;
    private BookingProductAdapter adapter;
    private Button back, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_booking);
        initial();
        getData();
        staticBinding();
    }

    private void initial(){
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("ticket");
        lv = findViewById(R.id.products);
        back = findViewById(R.id.backToSeat);
        next = findViewById(R.id.nextToConfirm);
    }

    private void getData() {
        ProductController.getInstance().getAll().observe(this, products -> {
            if(products == null) return;
            adapter = new BookingProductAdapter(products, bundle);
            lv.setAdapter(adapter);
        });
    }
    private void staticBinding() {
        back.setOnClickListener(v -> finish());

        next.setOnClickListener(v -> {
            Intent intent  = new Intent(getApplicationContext(), ConfirmBooking.class);
            intent.putExtra("ticket", bundle);
            startActivity(intent);
        });
    }
}