package com.example.movie_ticket_booking.Views.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movie_ticket_booking.Common.EditContext;
import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Common.SelectContext;
import com.example.movie_ticket_booking.Controllers.RoomController;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.R;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EditRoom extends DialogFragment {
    private EditText mInpNum, mInpSeats;
    private Room room;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("CHỈNH SỬA PHÒNG");

        View view = getLayoutInflater().inflate(R.layout.frag_admin_info_room, null);
        builder.setView(view);

        mInpNum = view.findViewById(R.id.inp_num);
        mInpSeats = view.findViewById(R.id.inp_seats);

        room = SelectContext.room;
        mInpNum.setText(Integer.toString(room.getRoomNumber()));
        mInpSeats.setText(String.join(",", room.getSeats().stream().map(Object::toString).collect(Collectors.toList())));

        builder.setPositiveButton("Sửa", (dialogInterface, i) -> {
            room.setRoomNumber(Integer.parseInt(mInpNum.getText().toString()));
            room.setSeats(Arrays
                            .stream(mInpSeats
                                        .getText()
                                        .toString()
                                        .split("\\,"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));

            try {
                RoomController.getInstance(SelectContext.cinema).update(room.getId(), room);
                Toast.makeText(getContext(), "Sửa phòng thành công!", Toast.LENGTH_SHORT).show();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        builder.setNeutralButton("Xóa", (dialogInterface, i) -> {
            RoomController.getInstance(SelectContext.cinema).delete(room.getId());
            Toast.makeText(getContext(), "Xóa phòng thành công!", Toast.LENGTH_SHORT).show();
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
