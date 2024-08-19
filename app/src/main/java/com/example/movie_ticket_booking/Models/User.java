package com.example.movie_ticket_booking.Models;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends BaseModel {
    private String lastName;
    private String firstName;
    private Date birthdate;
    private boolean gender;
    private String avatarPath;
    private String email;

    //authentication
    private String username;
    private String password;
    private UserRole role;

    //Foreign key
    private List<Ticket> tickets;

    //constructor
    public User(){
        this.role = UserRole.CLIENT;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.role.prefix, this.lastName, this.firstName);
    }
}
