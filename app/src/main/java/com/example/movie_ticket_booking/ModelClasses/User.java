package com.example.movie_ticket_booking.ModelClasses;

public class User {

    UserRole role;
    public User(){
        this.role = UserRole.CLIENT;
    }

    public void setUserRole(UserRole A){
        this.role = A;
    }

    public UserRole getUserRole(){
        return this.role;
    }


}
