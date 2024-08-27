package com.example.movie_ticket_booking.Views.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movie_ticket_booking.Common.EditContext;
import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Controllers.RoomController;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddRoom extends DialogFragment {
    private EditText mInpNum, mInpSeats;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("TẠO PHÒNG");

        View view = getLayoutInflater().inflate(R.layout.frag_admin_info_room, null);
        builder.setView(view);

        mInpNum = view.findViewById(R.id.inp_num);
        mInpSeats = view.findViewById(R.id.inp_seats);

        builder.setPositiveButton("Tạo", (dialogInterface, i) -> {
            int roomNum = Integer.parseInt(mInpNum.getText().toString());
            List<Integer> seats = Arrays.stream(mInpSeats.getText().toString().split("\\,")).map(Integer::parseInt).collect(Collectors.toList());

            EditContext.cinema.observe(getParentFragment().getViewLifecycleOwner(), _cinema -> {
                Room room = new Room(roomNum, _cinema.getId(), seats);
                RoomController.getInstance().add(room);
                Toast.makeText(getContext(), "Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
            });
        });
        builder.setNegativeButton("Thoát", null);

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IReloadOnDestroy parent = (EditCinema) getParentFragment();
        parent.reload();
    }
}
