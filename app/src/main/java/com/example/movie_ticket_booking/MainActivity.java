package com.example.movie_ticket_booking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cloudinary.android.MediaManager;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Models.UserRole;
import com.example.movie_ticket_booking.Views.CinemasFragment;
import com.example.movie_ticket_booking.Views.FilmFragment;
import com.example.movie_ticket_booking.Views.NotificationFragment;
import com.example.movie_ticket_booking.Views.OtherFragment;
import com.example.movie_ticket_booking.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        MediaManager.init(this);

        binder = ActivityMainBinding.inflate(getLayoutInflater());
        film = new FilmFragment();
        cinemas = new CinemasFragment();
        notification = new NotificationFragment();
        other = new OtherFragment();

        setContentView(binder.getRoot());
        ChangeFragment(film);

        navbar = findViewById(R.id.bottomNavigationView);

        if (AuthUserController.getUserlogin().getRole() == UserRole.ADMIN)
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