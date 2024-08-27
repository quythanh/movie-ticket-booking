package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseModel {
    private int roomNumber;
    private String cinema;
    private List<Integer> seats;

    @NonNull
    @Override
    public String toString() {
        return String.format("Phòng %d", this.roomNumber);
    }
}
