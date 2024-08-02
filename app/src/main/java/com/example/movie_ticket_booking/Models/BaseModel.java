package com.example.movie_ticket_booking.Models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseModel {
    protected String id;
    protected Date createdDate;
    protected boolean isActive;
}
