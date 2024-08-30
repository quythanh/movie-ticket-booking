package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket extends BaseModel{
    private DocumentReference user;
    private DocumentReference showtime;
    private List<DetailTicket> details;
    private Integer total;
    private Date cancelableTime;
    private boolean paid;
    private Payment payment;

    public Ticket(){
        super();
        cancelableTime = new Date();
        cancelableTime.setTime(this.createdDate.getTime() + 1000*60*60);
        paid = false;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Vé xem phim: %s", this.showtime);
    }
}
