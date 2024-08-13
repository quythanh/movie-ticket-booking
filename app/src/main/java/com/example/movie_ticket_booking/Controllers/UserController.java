package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import com.example.movie_ticket_booking.FragmentEnum;
import com.example.movie_ticket_booking.MainActivity;
import com.example.movie_ticket_booking.Models.User;

import java.net.PasswordAuthentication;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public void add(User o) {
        this.db.collection(this.collectionPath).document(o.getId())
                .set(o)
                .addOnSuccessListener(documentReference -> Log.d(collectionPath, "Add successfully ID: " + documentReference))
                .addOnFailureListener(e -> Log.e(collectionPath, "Error adding document", e));
    }

    public void add(User o, FragmentEnum redirect){
        this.db.collection(this.collectionPath).document(o.getId())
                .set(o)
                .addOnSuccessListener(documentReference -> {
                    Log.d(collectionPath, "Add successfully ID: " + documentReference);
                    MainActivity.getInstance().getFragmentChanger().setValue(redirect);
                })
                .addOnFailureListener(e -> Log.e(collectionPath, "Error adding document", e));
    }
}
