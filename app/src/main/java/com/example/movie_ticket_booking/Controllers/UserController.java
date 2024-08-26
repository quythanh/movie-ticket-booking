package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Models.User;

public class UserController extends GenericController<User> {
    private static UserController _instance = null;


    private UserController() {
        super("users", User.class);
    }

    public static synchronized UserController getInstance() {
        if (_instance == null)
            _instance = new UserController();
        return _instance;
    }

    public void add(User o, FragmentManager fragmentManager, Fragment redirect) {
        this.db.collection(this.collectionPath).document(o.getId())
                .set(o)
                .addOnSuccessListener(documentReference -> {
                    Log.d(collectionPath, "Add successfully ID: " + documentReference);
                    UIManager.changeFragment(fragmentManager, redirect);
                })
                .addOnFailureListener(e -> Log.e(collectionPath, "Error adding document", e));
    }
}
