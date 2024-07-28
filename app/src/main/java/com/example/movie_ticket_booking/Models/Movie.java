package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movie extends BaseModel{
    private String title;
    private List<String> directors;
    private List<String> actors;
    private int minute;
    private Date premiere;
    private String intro;
    private MovieType type;
    //picture
    //trailer

    //Foreign key
    private List<Cinema> cinemas;
    private List<Rating> ratings;
    private List<Tag> tags;

    public void setActive(boolean active) {
        this.isActive = active;
        if(!active)
            this.type = MovieType.INACCESSIBLE;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
}
