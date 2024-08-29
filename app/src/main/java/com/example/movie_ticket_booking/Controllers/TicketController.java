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
    public MutableLiveData<List<Ticket>> getMyTickets(){
        User u = AuthUserController.getInstance().getUserlogin().getValue();
        MutableLiveData<List<Ticket>> result = new MutableLiveData<>();
        List<Ticket> tickets =new ArrayList<>();
        DocumentReference ur = UserController.getInstance().getRef(u.getId());

        this.db.collection(this.collectionPath)
                .whereEqualTo("user", ur)
                .orderBy("createdDate")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    queryDocumentSnapshots.forEach(x -> {
                        Ticket t = new Ticket();
                        t.setId(x.getId());
                        t.setUser(x.getDocumentReference("user"));
                        t.setShowtime(x.getDocumentReference("showtime"));
                        t.setTotal(Math.toIntExact(x.getLong("total")));
                        t.setActive( x.getBoolean("active"));
                        t.setCancelableTime(x.getTimestamp("cancelableTime").toDate());
                        t.setCreatedDate(x.getTimestamp("createdDate").toDate());
                        t.setDetails(new ArrayList<>());
                        List<Map<String, Object>> GeneralDetails = (List<Map<String, Object>>) x.get("details");

                        for(Map<String, Object> y : GeneralDetails)
                        {
                            if(y.get("detailType").equals("SEAT")){
                                DetailTicket dt = new Seat(DetailTicket.DetailType.SEAT, (String) y.get("seatNumber"), SeatType.valueOf((String) y.get("type")));
                                t.getDetails().add(dt);
                            }
                            else if (y.get("detailType").equals("PRODUCT")){
                                DetailTicket dt = new ProductInTicket(DetailTicket.DetailType.PRODUCT, (DocumentReference) y.get("product") , Math.toIntExact((Long) y.get("amount")));
                                t.getDetails().add(dt);
                            }
                        }
                        tickets.add(t);
                    });
                    result.setValue(tickets);
                })
                .addOnFailureListener(command -> {
                    System.out.println(command);
                });
        return  result;
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
