package com.example.movie_ticket_booking.Controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Models.Ticket;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketController extends GenericController<Ticket> {
    private static TicketController _instance = null;

    private TicketController() {
        super("tickets", Ticket.class);
    }


    public static synchronized TicketController getInstance() {
        if (_instance == null)
            _instance = new TicketController();
        return _instance;
    }

    public  MutableLiveData<List<String>> getSoldSeat(DocumentReference showtime){
        MutableLiveData<List<String>> result = new MutableLiveData<>();
        List<String> res = new ArrayList<>();
        this.db.collection(this.collectionPath)
                .whereEqualTo("showtime", showtime)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.getDocumentChanges().isEmpty()) {
                        result.setValue(res);
                    }else {
                        queryDocumentSnapshots.forEach(q -> {
                            List<Map<String, Object>> dp = (List<Map<String, Object>>) q.get("details");
                            dp.forEach(x -> {
                                Object t = x.get("detailType");
                                if (x.get("detailType").equals("SEAT")) {
                                    res.add((String) x.get("seatNumber"));
                                }
                            });
                            result.setValue(res);
                        });
                    }
                });
        return result;
    }
}
