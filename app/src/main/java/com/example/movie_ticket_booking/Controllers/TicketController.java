package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.GenericController;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.Models.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.AggregateField;
import com.google.firebase.firestore.AggregateSource;
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

    @Override
    public LiveData<List<Ticket>> getAll() {
        MutableLiveData<List<Ticket>> result = new MutableLiveData<>();

        this.db.collection(this.collectionPath)
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

    public LiveData<List<Ticket>> getAll(User u) {
        MutableLiveData<List<Ticket>> result = new MutableLiveData<>();
        DocumentReference _userRef = UserController.getInstance().getRef(u.getId());

        this.db.collection(this.collectionPath)
                .whereEqualTo("user", _userRef)
                .orderBy("createdDate", Query.Direction.DESCENDING)
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

    public LiveData<List<Ticket>> getMyTickets() {
        User u = AuthUserController.getInstance().getUserlogin().getValue();
        return this.getAll(u);
    }

    public LiveData<List<String>> getSoldSeat(DocumentReference showtime) {
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

    public LiveData<Integer> getRevenue(Timestamp start, Timestamp end) {
        MutableLiveData liveData = new MutableLiveData();

        this.db.collection(this.collectionPath)
                .whereGreaterThanOrEqualTo("createdDate", start)
                .whereLessThan("createdDate", end)
                .aggregate(AggregateField.sum("total"))
                .get(AggregateSource.SERVER)
                .addOnSuccessListener(aggregateQuerySnapshot -> {
                    int sum = Math.toIntExact((Long) aggregateQuerySnapshot.get(AggregateField.sum("total")));
                    Log.d(this.getClass().getName(), Integer.toString(sum));
                    liveData.setValue(sum);
                })
                .addOnFailureListener(e -> {
                    Log.e(this.getClass().getName(), e.toString());
                });

        return liveData;
    }

    public LiveData<Integer> getRevenue(Date start, Date end) {
        Timestamp s = new Timestamp(start),
                e = new Timestamp(end);

        return this.getRevenue(s, e);
    }

    public LiveData<Integer> getRevenue(long start, long end) {
        Date s = new Date(start),
                e = new Date(end);

        return this.getRevenue(s, e);
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
