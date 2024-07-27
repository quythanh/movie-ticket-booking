package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.ModelClasses.User;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private static List<User> users;

    static {
        users = new ArrayList<>();
    }

    private UserController(){}

    public static List<User> getUserList(){
        return users;
    }

    public static void addUser(User A){
        users.add(A);
    }

}
