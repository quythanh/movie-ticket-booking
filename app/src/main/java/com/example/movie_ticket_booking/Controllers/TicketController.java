package com.example.movie_ticket_booking.Controllers;

import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Models.DetailTicket;
import com.example.movie_ticket_booking.Models.ProductInTicket;
import com.example.movie_ticket_booking.Models.Seat;
import com.example.movie_ticket_booking.Models.SeatType;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.Models.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
                .whereEqualTo("user", ur)
                // .orderBy("createdDate", Query.Direction.DESCENDING)
                // .get()
                // .addOnSuccessListener(queryDocumentSnapshots -> {
                //     queryDocumentSnapshots.forEach(x -> {
                //         Ticket t = new Ticket();
                //         t.setId(x.getId());
                //         t.setUser(x.getDocumentReference("user"));
                //         t.setShowtime(x.getDocumentReference("showtime"));
                //         t.setTotal(Math.toIntExact(x.getLong("total")));
                //         t.setActive( x.getBoolean("active"));
                //         t.setCancelableTime(x.getTimestamp("cancelableTime").toDate());
                //         t.setCreatedDate(x.getTimestamp("createdDate").toDate());
                //         t.setDetails(new ArrayList<>());
                //         t.setPaid(x.getBoolean("paid"));
                //         if(!t.isPaid() && t.getCancelableTime().before(new Date())){
                //             t.setActive(false);
                //             try {
                //                 update(t.getId(), t);
                //             } catch (IllegalAccessException e) {
                //                 throw new RuntimeException(e);
                //             }
                //         }

                //         List<Map<String, Object>> GeneralDetails = (List<Map<String, Object>>) x.get("details");
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
                .whereEqualTo("active", true)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        queryDocumentSnapshots.forEach(q -> {
                            boolean isPaid = q.get("paid")!=null ? q.getBoolean("paid") : false;
                            Date cancel = q.getTimestamp("cancelableTime").toDate();
                            if(isPaid || !isPaid && cancel.after(new Date())){
                                List<Map<String, Object>> dp = (List<Map<String, Object>>) q.get("details");
                                dp.forEach(x -> {
                                    if (x.get("detailType").equals("SEAT")) {
                                        res.add((String) x.get("seatNumber"));
                                    }
                                });
                            }
                        });
                        result.setValue(res);
                });
        return result;
    }

    public MutableLiveData<Map<Integer, Integer>> statisticMyTicketByMonth() throws ParseException {
        User u = AuthUserController.getInstance().getUserlogin().getValue();
        DocumentReference ur = UserController.getInstance().getRef(u.getId());

        MutableLiveData<Map<Integer, Integer>> result = new MutableLiveData<>();
        Map<Integer, Integer> stat = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Timestamp start = new Timestamp(Constant.DATETIME_FORMATTER.parse(String.format("01/01/%s 00:00",calendar.get(Calendar.YEAR))));
        Timestamp end = new Timestamp(Constant.DATETIME_FORMATTER.parse(String.format("31/12/%s 23:59",calendar.get(Calendar.YEAR))));

        this.db.collection(this.collectionPath)
                .whereEqualTo("user", ur)
                .whereGreaterThanOrEqualTo("createdDate", start)
                .whereLessThanOrEqualTo("createdDate", end)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    queryDocumentSnapshots.forEach(x -> {
                        Date d = x.getTimestamp("createdDate").toDate();
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        int month = c.get(Calendar.MONTH);
                        if(stat.get(month) == null)
                            stat.put(month, 1);
                        else
                            stat.put(month, stat.get(month) + 1);
                    });
                    result.setValue(stat);
                })
                .addOnFailureListener(command -> {
                    System.out.println(command);
                });
        return result;
    }
}
