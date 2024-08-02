package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Product;

public class ProductController extends GenericController<Product> {
    private static ProductController _instance = null;

    private ProductController() {
        super("products", Product.class);
    }

    public static synchronized ProductController getInstance() {
        if (_instance == null)
            _instance = new ProductController();
        return _instance;
    }
}
