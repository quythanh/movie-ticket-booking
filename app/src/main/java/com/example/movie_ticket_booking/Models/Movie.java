package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Controllers.RatingController;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
    private String trailer;
    private double ratingPoint;

    //Foreign key
//    private List<Cinema> cinemas;
//    private List<Tag> tags;

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active)
            this.type = MovieType.INACCESSIBLE;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }

    public Movie() {};
    public String getDirectorsToString(){
        final String[] res = {""};
        directors.forEach(x -> {
            if(directors.indexOf(x) + 1 < directors.size()){
                res[0] += x + ", ";
            }
            else
                res[0] += x;
        });
        return res[0];
    }
    public String getActorsToString(){
        final String[] res = {""};
        actors.forEach(x -> {
            if(actors.indexOf(x) + 1 < actors.size()){
                res[0] += x + ", ";
            }
            else
                res[0] += x;
        });
        return res[0];
    }

    public MutableLiveData<Double> getAvgPoint(LifecycleOwner owner){
        MutableLiveData<Double> res = new MutableLiveData<>();
        RatingController.getInstance(this).getAll().observe(owner, ratings -> {
            this.ratingPoint = 0;
            if(ratings == null) return;
            for(Rating r : ratings){
                this.ratingPoint += r.getStars();
            }
            this.ratingPoint /= ratings.size();
            res.setValue(this.ratingPoint);
        });
        return res;
    }
}
