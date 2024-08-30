package com.example.movie_ticket_booking.Components;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.GenericAdapter;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketListAdapter extends GenericAdapter<Ticket> {
    private LifecycleOwner owner;

    public TicketListAdapter(LifecycleOwner owner, List<Ticket> tickets) {
        super(tickets, R.layout.list_ticket_item);
        this.owner = owner;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        v = super.getView(position, v, parent);

        TextView title, time, date, createdDate, status;
        ImageView img;
        title = v.findViewById(R.id.ticketMovie);
        time = v.findViewById(R.id.ticketShowTime);
        date = v.findViewById(R.id.ticketShowDate);
        createdDate = v.findViewById(R.id.ticketCreadtedDate);
        status = v.findViewById(R.id.ticketStatus);
        img = v.findViewById(R.id.ticketMovieImage);

        Ticket t = this.getItem(position);
        t.getShowtime().get().addOnSuccessListener(documentSnapshot -> {
            Showtime s = documentSnapshot.toObject(Showtime.class);
            time.setText(Constant.TIME_FORMATTER.format(s.getDate()));
            date.setText(Constant.DATE_FORMATTER.format(s.getDate()));

            MovieController.getInstance().get(s.getMovie()).observe(owner, movie -> {
                if(movie==null) return;
                title.setText(movie.getTitle());
                Glide.with(parent.getContext()).load(movie.getPoster()).into(img);
            });
        });

        createdDate.setText(Constant.DATETIME_FORMATTER.format(t.getCreatedDate()));
        if (t.isActive()){
            status.setText("ĐÃ ĐẶT");
            status.setTextColor(Color.parseColor("#70BB44"));
        }
        else{
            status.setText("ĐÃ HỦY");
            status.setTextColor(Color.parseColor("#FF0010"));
        }
        return v;
    }
}
