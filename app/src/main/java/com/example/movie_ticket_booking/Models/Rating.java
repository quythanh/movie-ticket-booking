package com.example.movie_ticket_booking.Models;

import com.google.firebase.firestore.DocumentReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating extends BaseModel{
    private DocumentReference user;
    private DocumentReference movie;
    private int stars;
    private String comment;

    public Rating(){
        super();
    }
    @Override
    public String toString() {
        return String.format("Bình luận của %s cho phim %s", user, movie);
    }
}
