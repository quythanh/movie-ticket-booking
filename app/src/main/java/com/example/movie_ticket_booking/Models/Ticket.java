package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import com.example.movie_ticket_booking.Controllers.TicketController;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket extends BaseModel {
    private DocumentReference user;
    private DocumentReference showtime;
    private List<DetailTicket> details;
    private Integer total;
    private Date cancelableTime;
    private boolean paid;
    private Payment payment;

    public Ticket() {
        cancelableTime = new Date();
        cancelableTime.setTime(this.createdDate.getTime() + 1000*60*60);
        paid = false;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Vé xem phim: %s", this.showtime);
    }

    public static Ticket parse(DocumentSnapshot x) {
        Ticket t = new Ticket();
        t.setId(x.getId());
        t.setUser(x.getDocumentReference("user"));
        t.setShowtime(x.getDocumentReference("showtime"));
        t.setTotal(Math.toIntExact(x.getLong("total")));
        t.setActive(x.getBoolean("active"));
        t.setCancelableTime(x.getTimestamp("cancelableTime").toDate());
        t.setCreatedDate(x.getTimestamp("createdDate").toDate());
        t.setDetails(new ArrayList<>());
        t.setPaid(x.getBoolean("paid"));
        if(!t.isPaid() && t.getCancelableTime().before(new Date())){
            t.setActive(false);
            try {
                TicketController.getInstance().update(t.getId(), t);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        List<Map<String, Object>> generalDetails = (List<Map<String, Object>>) x.get("details");
        for (Map<String, Object> y : generalDetails) {
            DetailTicket dt;
            if (y.get("detailType").equals("SEAT"))
                dt = Seat.parse(y);
            else
                dt = ProductInTicket.parse(y);
            t.getDetails().add(dt);
        }

        return t;
    }
}
