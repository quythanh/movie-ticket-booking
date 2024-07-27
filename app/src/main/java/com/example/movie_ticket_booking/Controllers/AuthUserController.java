package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.ModelClasses.User;
import com.example.movie_ticket_booking.ModelClasses.UserRole;

public class AuthUserController {
    private static User userlogin;
    static {
        userlogin = new User();
    }
    public static User getUserlogin() {
        return userlogin;
    }

    public static void setUserlogin(User userlogin) {
        AuthUserController.userlogin = userlogin;
    }

    public static void toggleRole(){
        userlogin.setUserRole(userlogin.getUserRole()== UserRole.CLIENT ? UserRole.ADMIN : UserRole.CLIENT);
    }
}
