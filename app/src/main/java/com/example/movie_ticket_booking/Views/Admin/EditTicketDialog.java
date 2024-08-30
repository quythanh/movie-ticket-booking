package com.example.movie_ticket_booking.Views.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Common.SelectContext;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Controllers.TicketController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Seat;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.R;

import java.util.Date;
import java.util.stream.Collectors;

public class EditTicketDialog extends DialogFragment {
    private TextView mTxtDate, mTxtTime, mTxtPrice, mTxtCinema, mTxtSeats, mTxtStatus;

    private Ticket ticket;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Thông tin Vé");
        builder.setPositiveButton("Thoát", null);
        builder.setNeutralButton("Xóa", (dialogInterface, i) -> {
            TicketController.getInstance().delete(ticket.getId());
            Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
        });

        View view = getLayoutInflater().inflate(R.layout.frag_admin_info_ticket, null);
        builder.setView(view);
        initData();
        bindingViews(view);
        setupViews(builder);

        return builder.create();
    }

    private void initData() {
        ticket = SelectContext.ticket;
    }

    private void bindingViews(View view) {
        mTxtCinema = view.findViewById(R.id.txt_cinema);
        mTxtDate = view.findViewById(R.id.txt_date);
        mTxtPrice = view.findViewById(R.id.txt_price);
        mTxtSeats = view.findViewById(R.id.txt_seats);
        mTxtStatus = view.findViewById(R.id.txt_status);
        mTxtTime = view.findViewById(R.id.txt_time);
    }

    private void setupViews(AlertDialog.Builder builder) {
        mTxtPrice.setText(Constant.currencyFormatter.format(ticket.getTotal()));

        mTxtStatus.setText(ticket.isActive() ? "Còn hiệu lực" : "Đã sử dụng");
        mTxtStatus.setTextColor(getResources().getColor(ticket.isActive() ? android.R.color.holo_green_light : android.R.color.holo_red_dark));

        String seats = ticket.getDetails()
                .stream()
                .filter(Seat.class::isInstance)
                .map(Seat.class::cast)
                .map(Seat::getSeatNumber)
                .collect(Collectors.joining(","));
        mTxtSeats.setText(seats);

        ticket
                .getShowtime()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Showtime _showtime = documentSnapshot.toObject(Showtime.class);
                    Date _date = _showtime.getDate();

                    mTxtDate.setText(Constant.DATE_FORMATTER.format(_date));
                    mTxtTime.setText(Constant.TIME_FORMATTER.format(_date));

                    _showtime.getRoom().getParent().getParent().get().addOnSuccessListener(documentSnapshot1 -> {
                        Cinema _cinema = documentSnapshot1.toObject(Cinema.class);
                        mTxtCinema.setText(_cinema.getName());
                    });

                    MovieController.getInstance().get(_showtime.getMovie()).observe(getParentFragment().getViewLifecycleOwner(), _movie -> {
                        builder.setTitle(_movie.getTitle());
                    });
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IReloadOnDestroy parent = (ManageTickets) getParentFragment();
        parent.reload();
    }
}
