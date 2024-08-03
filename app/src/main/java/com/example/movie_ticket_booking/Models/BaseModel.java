package com.example.movie_ticket_booking.Models;

import com.example.movie_ticket_booking.Identifiable;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseModel implements Identifiable {
    @Exclude
    protected String id;
    protected Date createdDate;
    protected boolean isActive;
}
