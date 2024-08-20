package com.example.movie_ticket_booking.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.movie_ticket_booking.Common;
import com.example.movie_ticket_booking.Models.BaseModel;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeGridAdapter extends BaseAdapter {

    private Context context;
    private List<Showtime> showtimes;

    @Override
    public int getCount() {
        return showtimes.size();
    }

    @Override
    public Object getItem(int position) {
        return showtimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_showtime_item, parent, false);
        Showtime s = showtimes.get(position);
        TextView t = view.findViewById(R.id.griditem_showtime);
        t.setText(Common.timeFormatter.format(s.getDate()));
        return view;
    }
}
