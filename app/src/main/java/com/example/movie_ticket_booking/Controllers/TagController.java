package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Tag;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class TagController {
    private static List<Tag> tags;

    static {
        tags = new ArrayList<>();
    }
    private  TagController() {}
}
