package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private String landscapeImage;
    private String poster;
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

    public Movie() {};
    public Movie(Map<String,Object> params){
        for(Map.Entry<String, Object> entry : params.entrySet()){
            switch (entry.getKey()){
                case "id":
                    this.id = (String) entry.getValue();
                    break;
                case "title":
                    this.title = (String) entry.getValue();
                    break;
                case "image":
                    this.landscapeImage = (String) entry.getValue();
                    break;
            }
        }
    }
    public Movie(String id, String title, String LandscapeImage, String poster){
        this.id = id;
        this.title = title;
        this.landscapeImage = LandscapeImage;
        this.poster = poster;
    }
}
