package com.example.movie_ticket_booking.Models;

import com.example.movie_ticket_booking.Interfaces.Identifiable;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseModel implements Identifiable {
    protected String id;
    protected Date createdDate;
    protected boolean isActive;
}
