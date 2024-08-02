package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Tag;

public class TagController extends GenericController<Tag> {
    private static TagController _instance = null;

    private TagController() {
        super("tags", Tag.class);
    }

    public static synchronized TagController getInstance() {
        if (_instance == null)
            _instance = new TagController();
        return _instance;
    }
}
