package com.example.movie_ticket_booking;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.movie_ticket_booking.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binder;
    FilmFragment film;
    CinemasFragment cinemas;
    NotificationFragment notification;
    OtherFragment other;

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

        binder.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
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