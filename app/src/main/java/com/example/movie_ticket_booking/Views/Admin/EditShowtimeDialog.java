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

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Common.SelectContext;
import com.example.movie_ticket_booking.Controllers.RoomController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.R;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EditShowtimeDialog extends DialogFragment {
    private EditText mInpTime;
    private Spinner mSpnRooms;

    private Showtime showtime;
    private ArrayAdapter<Room> roomAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Sửa Suất chiếu");
        builder.setPositiveButton("Thoát", null);
        builder.setNeutralButton("Xóa", (dialogInterface, i) -> {
            Cinema _c = SelectContext.cinema;
            ShowtimeController.getInstance(_c).delete(showtime.getId());
            Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Sửa", (dialogInterface, i) -> {
            // TODO: check time conflict
            Cinema _c = SelectContext.cinema;
            String _date = SelectContext.date;
            String _time = mInpTime.getText().toString();
            Room _room = (Room) mSpnRooms.getSelectedItem();

            Date _datetime;
            try {
                _datetime = Constant.DATETIME_FORMATTER.parse(_date + " " + _time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (_datetime == null) return;

            DocumentReference _roomRef = RoomController.getInstance(_c).getRef(_room.getId());
            showtime.setDate(_datetime);
            showtime.setRoom(_roomRef);

            try {
                ShowtimeController.getInstance(_c).update(showtime.getId(), showtime);
                Toast.makeText(getContext(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
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
        showtime = SelectContext.showtime;
        roomAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
    }

    private void bindingViews(View view) {
        mInpTime = view.findViewById(R.id.inp_time);
        mSpnRooms = view.findViewById(R.id.spn_rooms);
    }

    private void setupViews() {
        mSpnRooms.setAdapter(roomAdapter);

        mInpTime.setText(Constant.TIME_FORMATTER.format(showtime.getDate()));
    }

    private void loadViewsData() {
        Cinema _cinema = SelectContext.cinema;
        RoomController _controller = RoomController.getInstance(_cinema);

        _controller.getAll().observe(getParentFragment().getViewLifecycleOwner(), _rooms -> {
            roomAdapter.clear();
            roomAdapter.addAll(_rooms);
            roomAdapter.notifyDataSetChanged();

            List<Room> __rooms = _rooms.stream()
                    .filter(_r -> _controller.getRef(_r.getId()).equals(SelectContext.showtime.getRoom()))
                    .collect(Collectors.toList());
            Room _room = __rooms.get(0);
            int _pos = roomAdapter.getPosition(_room);
            mSpnRooms.setSelection(_pos);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IReloadOnDestroy parent = (ManageShowtimes) getParentFragment();
        parent.reload();
    }
}
