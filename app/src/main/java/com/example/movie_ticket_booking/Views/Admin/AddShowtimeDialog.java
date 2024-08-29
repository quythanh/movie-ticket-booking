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
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.R;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

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
            // TODO: check time conflict
            Movie _m = SelectContext.movie;
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
            Showtime _showtime = new Showtime(_m, _roomRef, _datetime);
            ShowtimeController.getInstance(_c).add(_showtime);
            Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
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
        RoomController
                .getInstance(SelectContext.cinema)
                .getAll()
                .observe(getParentFragment().getViewLifecycleOwner(), _rooms -> {
                    roomAdapter.clear();
                    roomAdapter.addAll(_rooms);
                    roomAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IReloadOnDestroy parent = (ManageShowtimes) getParentFragment();
        parent.reload();
    }
}
