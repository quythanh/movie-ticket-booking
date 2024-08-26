package com.example.movie_ticket_booking.Components;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movie_ticket_booking.Common.GenericAdapter;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.R;

import java.util.List;

public class GridCinemaAdapter extends GenericAdapter<Cinema> {

    public GridCinemaAdapter(List<Cinema> list) {
        super(list, R.layout.horizontal_cinema_item);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = super.getView(i, view, viewGroup);
        Cinema c = this.getItem(i);

        TextView name, address;
        LinearLayout wrapper;
        LinearLayout.LayoutParams params;

        wrapper = view.findViewById(R.id.wrapper);
        name = view.findViewById(R.id.CinemaItemName);
        address = view.findViewById(R.id.CinemaItemAdress);

        ViewGroup.LayoutParams defaultParams = wrapper.getLayoutParams();
        params = new LinearLayout.LayoutParams(defaultParams.width, defaultParams.height);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        wrapper.setLayoutParams(params);
        name.setText(String.format("TP Cine %s", c.getName()));
        address.setText(c.getAddress().toString());

        return view;
    }
}
