package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private static List<Product> products;

    static {
        products = new ArrayList<>();
    }

    private ProductController() {}
}
