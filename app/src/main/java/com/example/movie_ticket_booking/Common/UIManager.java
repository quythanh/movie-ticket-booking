package com.example.movie_ticket_booking.Common;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.movie_ticket_booking.Configuration;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Models.SeatType;
import com.example.movie_ticket_booking.R;
import com.example.movie_ticket_booking.Views.LoginFragment;

public class UIManager {
    public static void addFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void changeFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    public static void AuthChangeFragment(FragmentManager fragmentManager, Fragment fragment) {
        if(AuthUserController.getInstance().getUserlogin().getValue().getId() == null){
            changeFragment(fragmentManager, LoginFragment.getInstance());
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }


    public static String getVideoFrame(String url) {
        if (url == null || !url.contains("youtube.com"))
            return "<h1>404 not found</h1>";
        return "<iframe width='100%' height='100%' src='" + url + "' title='YouTube video player' frameborder='0' allow='accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share' referrerpolicy='strict-origin-when-cross-origin' allowfullscreen></iframe>";
    }

    public static SeatType getSeatType(char x, int totalLine){
        if ((int) x - 65 >= totalLine - Configuration.LastDoubleRow && (int) x -65 <= totalLine - 1)
            return SeatType.COUPLE;
        if ((int) x - 65 >= 0 && (int) x - 65 <= Configuration.FirstNormalRow - 1)
            return SeatType.STANDARD;
        return SeatType.VIP;
    }

}
