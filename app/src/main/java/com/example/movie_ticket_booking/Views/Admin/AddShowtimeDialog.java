package com.example.movie_ticket_booking.Views.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movie_ticket_booking.Common.EditContext;
import com.example.movie_ticket_booking.Controllers.RoomController;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;

public class AddShowtimeDialog extends DialogFragment {

    private EditText mInpTime;
    private Spinner mSpnRooms;

    private ArrayAdapter<Room> roomAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Thêm Suất chiếu");
        builder.setPositiveButton("Thoát", null);
        builder.setNegativeButton("Thêm", (dialogInterface, i) -> {
            // TODO: add code here

            Toast.makeText(getContext(), EditContext.movie.getValue().getTitle(), Toast.LENGTH_SHORT).show();
        });

        View view = getLayoutInflater().inflate(R.layout.frag_admin_info_showtime, null);
        builder.setView(view);
        initData();
        bindingViews(view);
        setupViews();
        loadViewsData();

        return builder.create();
    }

    private void initData() {
        roomAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
    }

    private void bindingViews(View view) {
        mInpTime = view.findViewById(R.id.inp_time);
        mSpnRooms = view.findViewById(R.id.spn_rooms);
    }

    private void setupViews() {
        mSpnRooms.setAdapter(roomAdapter);
    }

    private void loadViewsData() {
        EditContext.cinema.observe(getParentFragment().getViewLifecycleOwner(), _cinema -> {
            RoomController.getInstance(_cinema).getAll().observe(getParentFragment().getViewLifecycleOwner(), _rooms -> {
                roomAdapter.clear();
                roomAdapter.addAll(_rooms);
                roomAdapter.notifyDataSetChanged();
            });
        });
    }
}
