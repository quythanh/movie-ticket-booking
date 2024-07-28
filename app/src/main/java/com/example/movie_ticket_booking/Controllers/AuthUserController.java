package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.Models.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserController {
    @Getter
    @Setter
    private static User userlogin;
    static {
        userlogin = new User();
    }

    public static void toggleRole(){
        userlogin.setRole(userlogin.getRole()== UserRole.CLIENT ? UserRole.ADMIN : UserRole.CLIENT);
    }
}
