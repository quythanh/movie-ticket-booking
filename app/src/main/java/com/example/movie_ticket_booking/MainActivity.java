package com.example.movie_ticket_booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.ModelClasses.User;
import com.example.movie_ticket_booking.ModelClasses.UserRole;
import com.example.movie_ticket_booking.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binder;
    FilmFragment film;
    CinemasFragment cinemas;
    NotificationFragment notification;
    OtherFragment other;
    BottomNavigationView navbar;

    private void ChangeFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fg = fm.beginTransaction();
        fg.replace(R.id.frame, fragment);
        fg.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binder = ActivityMainBinding.inflate(getLayoutInflater());
        film = new FilmFragment();
        cinemas = new CinemasFragment();
        notification = new NotificationFragment();
        other = new OtherFragment();


        setContentView(binder.getRoot());
        ChangeFragment(film);

        navbar = findViewById(R.id.bottomNavigationView);


        if(AuthUserController.getUserlogin().getUserRole() == UserRole.ADMIN)
            navbar.inflateMenu(R.menu.bottom_admin_navmenu);
        else
            navbar.inflateMenu(R.menu.bottom_navmenu);

        binder.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.film:
                    ChangeFragment(film);
                    break;
                case R.id.cinemas:
                    ChangeFragment(cinemas);
                    break;
                case R.id.notification:
                    ChangeFragment(notification);
                    break;
                case R.id.other:
                    ChangeFragment(other);
                    break;
            }
            return true;
        });

    }


}