package com.example.movie_ticket_booking.Controllers;

import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Models.DetailTicket;
import com.example.movie_ticket_booking.Models.ProductInTicket;
import com.example.movie_ticket_booking.Models.Seat;
import com.example.movie_ticket_booking.Models.SeatType;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.Models.User;
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

    public MutableLiveData<List<Ticket>> getAll(User u) {
        MutableLiveData<List<Ticket>> result = new MutableLiveData<>();
        DocumentReference _userRef = UserController.getInstance().getRef(u.getId());

        this.db.collection(this.collectionPath)
                .whereEqualTo("user", _userRef)
                .orderBy("createdDate")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Ticket> tickets = new ArrayList<>();

                    queryDocumentSnapshots.forEach(x -> {
                        Ticket t = Ticket.parse(x);
                        tickets.add(t);
                    });

                    result.setValue(tickets);
                })
                .addOnFailureListener(command -> {
                    System.out.println(command);
                });

        return result;
    }

    public MutableLiveData<List<Ticket>> getMyTickets() {
        User u = AuthUserController.getInstance().getUserlogin().getValue();
        return this.getAll(u);
    }

    public  MutableLiveData<List<String>> getSoldSeat(DocumentReference showtime){
        MutableLiveData<List<String>> result = new MutableLiveData<>();
        List<String> res = new ArrayList<>();
        this.db.collection(this.collectionPath)
                .whereEqualTo("showtime", showtime)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        queryDocumentSnapshots.forEach(q -> {
                            List<Map<String, Object>> dp = (List<Map<String, Object>>) q.get("details");
                            dp.forEach(x -> {
                                if (x.get("detailType").equals("SEAT")) {
                                    res.add((String) x.get("seatNumber"));
                                }
                            });
                        });
                        result.setValue(res);
                });
        return result;
    }
}
