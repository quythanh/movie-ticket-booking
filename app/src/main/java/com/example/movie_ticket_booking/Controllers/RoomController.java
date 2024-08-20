package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.Models.Room;

public class RoomController extends GenericController<Room> {
    private static RoomController _instance = null;

    private RoomController() {
        super("rooms", Room.class);
    }

    public static synchronized RoomController getInstance() {
        if (_instance == null)
            _instance = new RoomController();
        return _instance;
    }

    public void initial(Cinema c){
        Room r1 = new Room(3,c);
        add(r1);
    }
}
