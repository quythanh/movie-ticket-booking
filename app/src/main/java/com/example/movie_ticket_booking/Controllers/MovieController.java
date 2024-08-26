package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.MovieType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieController extends GenericController<Movie> {
    private static MovieController _instance = null;

    private MovieController() {
        super("movies", Movie.class);
    }

    public static MovieController getInstance() {
        if (_instance == null)
            _instance = new MovieController();
        return _instance;
    }

    public LiveData<List<Movie>> getByType(MovieType type) {
        MutableLiveData<List<Movie>> liveData = new MutableLiveData<>();

        this.db.collection(this.collectionPath)
                .whereEqualTo("type", type.toString())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Movie> l = new ArrayList<>();
                    queryDocumentSnapshots.forEach(doc -> {
                        Movie m = doc.toObject(Movie.class);
                        m.setId(doc.getId());
                        l.add(m);
                    });
                    liveData.setValue(l);
                })
                .addOnFailureListener(e -> {
                    Log.e(collectionPath, "Error fetching document", e);
                    liveData.setValue(Collections.emptyList());
                });

        return liveData;
    }
}
