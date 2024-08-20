package com.example.movie_ticket_booking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cloudinary.android.MediaManager;
import com.example.movie_ticket_booking.Views.Admin.EditUser;
import com.example.movie_ticket_booking.Views.Admin.Home;
import com.example.movie_ticket_booking.Views.Admin.Statistic;
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

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaManager.init(this);
        fragmentManager = getSupportFragmentManager();

        binder = ActivityMainBinding.inflate(getLayoutInflater());
        film = new FilmFragment();
        cinemas = new CinemasFragment();
        notification = new NotificationFragment();
        other = new OtherFragment();

        Home fragAdminHome = Home.getInstance();
        Statistic fragAdminStat = Statistic.getInstance();

        setContentView(binder.getRoot());
        Common.changeFragment(fragmentManager, film);

        navbar = findViewById(R.id.bottomNavigationView);

       if (AuthUserController.getUserlogin().getRole() == UserRole.ADMIN)
           navbar.inflateMenu(R.menu.bottom_admin_navmenu);
       else
           navbar.inflateMenu(R.menu.bottom_navmenu);

        binder.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                // User
                case R.id.film:
                    Common.changeFragment(fragmentManager, film);
                    break;
                case R.id.cinemas:
                    Common.changeFragment(fragmentManager, cinemas);
                    break;
                case R.id.notification:
                    Common.changeFragment(fragmentManager, notification);
                    break;
                case R.id.other:
                    Common.changeFragment(fragmentManager, other);
                    break;

                // Admin
                case R.id.page_admin_home:
                    Common.changeFragment(fragmentManager, fragAdminHome);
                    break;
                case R.id.page_admin_statistic:
                    Common.changeFragment(fragmentManager, fragAdminStat);
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EditUser.getInstance().handlePickImageResult(requestCode, resultCode, data);
    }
}