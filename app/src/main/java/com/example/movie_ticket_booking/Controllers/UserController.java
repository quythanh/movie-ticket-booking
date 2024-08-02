package com.example.movie_ticket_booking.Controllers;

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
}
