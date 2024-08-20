package com.example.movie_ticket_booking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.cloudinary.android.MediaManager;
import com.example.movie_ticket_booking.Components.LocationService;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.RoomController;
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

import java.util.Objects;

import lombok.Getter;

public class MainActivity extends AppCompatActivity {
    private static MainActivity Instance;
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

    @Getter
    MutableLiveData<FragmentEnum> FragmentChanger;
    public MainActivity(){
        film = new FilmFragment();
        cinemas = new CinemasFragment();
        notification = new NotificationFragment();
        other = new OtherFragment();
        login = new LoginFragment();
        FragmentChanger = new MutableLiveData<>();
        register = new RegisterFragment();
        movieInfo = new MovieInfoFragment();
    }

    public void ChangeFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fg = fm.beginTransaction();
        fg.replace(R.id.frame, fragment);
        fg.commit();
    }

    public static MainActivity getInstance(){
        if(Instance == null){
            Instance = new MainActivity();
        }
        return Instance;
    }

    public void startService(){
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startService();
                }
                else {
                    Toast.makeText(this, "Chưa được phân quyền mở GPS",Toast.LENGTH_SHORT ).show();
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaManager.init(this);
        //check permission
        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                startService();
            }
        }
        else {
            startService();
        }

        binder = ActivityMainBinding.inflate(getLayoutInflater());
        film = new FilmFragment();
        cinemas = new CinemasFragment();
        notification = new NotificationFragment();
        other = new OtherFragment();
        login = new LoginFragment();
        Instance = this;
        authController = AuthUserController.getInstance();

        setContentView(binder.getRoot());
        ChangeFragment(film);

        navbar = findViewById(R.id.bottomNavigationView);

        if (Objects.requireNonNull(authController.getUserlogin().getValue()).getRole() == UserRole.ADMIN)
            navbar.inflateMenu(R.menu.bottom_admin_navmenu);
        else
            navbar.inflateMenu(R.menu.bottom_navmenu);
        FragmentChanger.observe(this, name -> {
            switch (name){
                case FILM:
                    ChangeFragment(film);
                    break;
                case CINEMAS:
                    ChangeFragment(cinemas);
                    break;
                case NOTIFICATION:
                    ChangeFragment(notification);
                    break;
                case OTHER:
                    ChangeFragment(other);
                    break;
                case LOGIN:
                    ChangeFragment(login);
                    break;
                case REGISTER:
                    ChangeFragment(register);
                    break;
                case MOVIE_INFO:
                    ChangeFragment(movieInfo);
                    break;
            }
        });
        binder.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.film:
                    FragmentChanger.setValue(FragmentEnum.FILM);
                    break;
                case R.id.cinemas:
                    FragmentChanger.setValue(FragmentEnum.CINEMAS);
                    break;
                case R.id.notification:
                    FragmentChanger.setValue(FragmentEnum.NOTIFICATION);
                    break;
                case R.id.other:
                    FragmentChanger.setValue(FragmentEnum.OTHER);
                    break;
            }
            return true;
        });
    }
}