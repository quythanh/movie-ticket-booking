package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.Models.Showtime;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ShowtimeController extends GenericController<Showtime> {
    private static ShowtimeController _instance = null;

    private ShowtimeController() {
        super("showtimes", Showtime.class);
    }

    public static synchronized ShowtimeController getInstance() {
        if (_instance == null)
            _instance = new ShowtimeController();
        return _instance;
    }

    public void add(Showtime o, Cinema c) {
        this.db.collection(this.collectionPath)
                .add(o)
                .addOnSuccessListener(documentReference -> {
                    Log.d(collectionPath, "Add successfully ID: " + documentReference.getId());
                    c.getShowtimes().add(documentReference.getId());
                    try {
                        CinemaController.getInstance().update(c.getId(), c);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .addOnFailureListener(e -> Log.e(collectionPath, "Error adding document", e));
    }
    public MutableLiveData<List<Showtime>> getShowtime(String movieId, Cinema cinema, Date date) throws ParseException {
        MutableLiveData<List<Showtime>> liveData = new MutableLiveData<>();
        Date startDate = Common.dateTimeFormatter.parse(Common.dateFormatter.format(date) + " 00:00");
        Date endDate = (Date) startDate.clone();
        endDate.setTime(startDate.getTime() + 1000*60*60*24);

        Timestamp start = new Timestamp(startDate);
        Timestamp end = new Timestamp(endDate);

        this.db.collection(this.collectionPath)
                .whereEqualTo("movie",movieId)
                .whereIn("id", cinema.getShowtimes())

                .whereGreaterThanOrEqualTo("date", start)
                .whereLessThan("date", end)
//                .whereGreaterThanOrEqualTo("date", new Timestamp(new Date()))
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Showtime> l = new ArrayList<>();
                    queryDocumentSnapshots.forEach(doc -> {
                        Showtime m = doc.toObject(Showtime.class);
                        m.setId(doc.getId());
                        l.add(m);
                    });
                   liveData.setValue(l);
                })
                .addOnFailureListener(e -> Log.d("qqz", "getShowtime: failed"));
        return  liveData;
    }
}
