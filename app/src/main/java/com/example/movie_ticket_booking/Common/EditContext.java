package com.example.movie_ticket_booking.Common;

import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.Models.User;

public class EditContext {
    public static MutableLiveData<User> user = new MutableLiveData<>();
    public static MutableLiveData<Product> product = new MutableLiveData<>();
    public static MutableLiveData<Movie> movie = new MutableLiveData<>();
    public static MutableLiveData<Cinema> cinema = new MutableLiveData<>();
}
