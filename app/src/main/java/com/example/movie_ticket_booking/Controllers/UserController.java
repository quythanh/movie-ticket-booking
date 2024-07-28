package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.User;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class UserController {
    private static List<User> users;

    static {
        users = new ArrayList<>();
    }

    private UserController(){}

    public static void addUser(User A){
        users.add(A);
    }
}
