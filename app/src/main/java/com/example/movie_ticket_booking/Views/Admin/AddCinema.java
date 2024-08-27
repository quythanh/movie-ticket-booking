package com.example.movie_ticket_booking.Views.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Models.Address;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;

public class AddCinema extends Fragment {

    private static AddCinema _instance = null;

    private Button mBtnCreateRoom, mBtnSave;
    private EditText mInpName, mInpStreet, mInpDistrict, mInpWard, mInpLong, mInpLat;
    private GridView mGridRooms;
    private ImageView mBtnBack;
    private Spinner mSpnProvinces;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<Room> roomAdapter;

    private AddCinema() {
        super(R.layout.frag_admin_info_cinema);
    }

    public static AddCinema getInstance() {
        if (_instance == null)
            _instance = new AddCinema();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        bindingViews(view);
        setupViews();
        return view;
    }

    private void initData() {
        provinceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.provinces));
        roomAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
    }

    private void bindingViews(View view) {
        mBtnBack = view.findViewById(R.id.btn_back);
        mBtnCreateRoom = view.findViewById(R.id.btn_create_room);
        mBtnSave = view.findViewById(R.id.btn_save);

        mGridRooms = view.findViewById(R.id.grid_rooms);

        mInpDistrict = view.findViewById(R.id.inp_district);
        mInpLat = view.findViewById(R.id.inp_lat);
        mInpLong = view.findViewById(R.id.inp_long);
        mInpName = view.findViewById(R.id.inp_name);
        mInpStreet = view.findViewById(R.id.inp_street);
        mInpWard = view.findViewById(R.id.inp_ward);

        mSpnProvinces = view.findViewById(R.id.spn_provinces);
    }

    private void setupViews() {
        mBtnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());
        mBtnCreateRoom.setOnClickListener(_v -> Toast.makeText(getContext(), "Bạn cần tạo rạp trước khi tạp phòng!", Toast.LENGTH_SHORT).show());
        mBtnSave.setOnClickListener(_v -> {
            Address addr = new Address(
                    mInpStreet.getText().toString(),
                    mInpWard.getText().toString(),
                    mInpDistrict.getText().toString(),
                    mSpnProvinces.getSelectedItem().toString(),
                    Double.parseDouble(mInpLat.getText().toString()),
                    Double.parseDouble(mInpLong.getText().toString())
            );
            Cinema cinema = new Cinema(mInpName.getText().toString(), addr);

            CinemaController.getInstance().add(cinema);
            Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
        });

        mGridRooms.setAdapter(roomAdapter);
        mSpnProvinces.setAdapter(provinceAdapter);
    }
}
