package com.example.movie_ticket_booking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.cloudinary.android.MediaManager;
import com.example.movie_ticket_booking.Views.Admin.EditUser;
import com.example.movie_ticket_booking.Views.Admin.Home;
import com.example.movie_ticket_booking.Views.Admin.Statistic;
import com.example.movie_ticket_booking.Controllers.AuthUserController;

import com.example.movie_ticket_booking.Components.LocationService;
import com.example.movie_ticket_booking.Models.UserRole;
import com.example.movie_ticket_booking.Views.CinemasFragment;
import com.example.movie_ticket_booking.Views.FilmFragment;
import com.example.movie_ticket_booking.Views.LoginFragment;
import com.example.movie_ticket_booking.Views.MovieInfoFragment;
import com.example.movie_ticket_booking.Views.NotificationFragment;
import com.example.movie_ticket_booking.Views.OtherFragment;
import com.example.movie_ticket_booking.Views.RegisterFragment;
import com.example.movie_ticket_booking.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static MainActivity _instance;

    ActivityMainBinding binder;
    FilmFragment film;
    CinemasFragment cinemas;
    NotificationFragment notification;
    OtherFragment other;
    LoginFragment login;
    RegisterFragment register;
    MovieInfoFragment movieInfo;
    BottomNavigationView navbar;
    AuthUserController authController;
    Home fragAdminHome;
    Statistic fragAdminStat;

    FragmentManager fragmentManager;

    public MainActivity() {
        film = FilmFragment.getInstance();
        cinemas = CinemasFragment.getInstance();
        notification = NotificationFragment.getInstance();
        other = OtherFragment.getInstance();
        login = LoginFragment.getInstance();
        register = RegisterFragment.getInstance();
        movieInfo = MovieInfoFragment.getInstance();
        authController = AuthUserController.getInstance();

        fragmentManager = getSupportFragmentManager();
        fragAdminHome = Home.getInstance();
        fragAdminStat = Statistic.getInstance();
    }

    public static MainActivity getInstance() {
        return _instance;
    }

    public void startService() {
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startService();
                else
                    Toast.makeText(this, "Chưa được phân quyền mở GPS",Toast.LENGTH_SHORT ).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaManager.init(this);
        _instance = this;

        // check permission
        if (Build.VERSION.SDK_INT >= 23)
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            else
                startService();
        else
            startService();

        binder = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binder.getRoot());
        Common.changeFragment(fragmentManager, film);

        navbar = findViewById(R.id.bottomNavigationView);
        authController.getUserlogin().observe(this, _u -> {
            navbar.getMenu().clear();

            if (_u.getRole() == UserRole.ADMIN)
                navbar.inflateMenu(R.menu.bottom_admin_navmenu);
            else
                navbar.inflateMenu(R.menu.bottom_navmenu);
        });

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