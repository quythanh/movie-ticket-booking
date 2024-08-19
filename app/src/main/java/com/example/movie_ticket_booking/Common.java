package com.example.movie_ticket_booking;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;

public class Common {
    public static SimpleDateFormat dateFormatter;
    public static SimpleDateFormat dateTimeFormatter;

    static {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    }

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
}
