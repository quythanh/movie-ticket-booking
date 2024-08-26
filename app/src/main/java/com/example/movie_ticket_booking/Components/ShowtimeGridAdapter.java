package com.example.movie_ticket_booking.Components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.GenericAdapter;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowtimeGridAdapter extends GenericAdapter<Showtime> {

    public ShowtimeGridAdapter(List<Showtime> list) {
        super(list, R.layout.grid_showtime_item);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = super.getView(position, view, parent);
        Showtime s = this.getItem(position);

        TextView t = view.findViewById(R.id.griditem_showtime);
        t.setText(Constant.TIME_FORMATTER.format(s.getDate()));
        return view;
    }
}
