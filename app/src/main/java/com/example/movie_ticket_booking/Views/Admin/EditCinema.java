package com.example.movie_ticket_booking.Views.Admin;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.Common.EditContext;
import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Common.SelectContext;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Models.Address;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Room;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;

public class EditCinema extends Fragment implements IReloadOnDestroy {
    private static EditCinema _instance = null;

    private Button mBtnCreateRoom, mBtnSave;
    private EditText mInpName, mInpStreet, mInpDistrict, mInpWard, mInpLong, mInpLat;
    private GridView mGridRooms;
    private ImageView mBtnBack, mBtnDelete;
    private Spinner mSpnProvinces;
    private TextView mTxtTitle;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<Room> roomAdapter;
    private Cinema cinema;

    private EditCinema() {
        super(R.layout.frag_admin_info_cinema);
    }

    public static EditCinema getInstance() {
        if (_instance == null)
            _instance = new EditCinema();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        bindingViews(view);
        setupViews();
        loadViewsData();
        return view;
    }

    private void initData() {
        provinceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.provinces));
        roomAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
    }

    private void loadViewsData() {
        EditContext.cinema.observe(getViewLifecycleOwner(), _cinema -> {
            cinema = _cinema;
            SelectContext.cinema = _cinema;

            mInpName.setText(cinema.getName());
            mInpStreet.setText(cinema.getAddress().getStreet());
            mInpDistrict.setText(cinema.getAddress().getDistrict());
            mInpWard.setText(cinema.getAddress().getWard());
            mSpnProvinces.setSelection(provinceAdapter.getPosition(cinema.getAddress().getProvince()));
            mInpLat.setText(Double.toString(cinema.getAddress().getLatitude()));
            mInpLong.setText(Double.toString(cinema.getAddress().getLongitude()));

            CinemaController
                    .getInstance()
                    .getRef(cinema.getId())
                    .collection("rooms")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        roomAdapter.clear();
                        queryDocumentSnapshots.forEach(doc -> {
                            Room m = doc.toObject(Room.class);
                            m.setId(doc.getId());
                            roomAdapter.add(m);
                            roomAdapter.notifyDataSetChanged();
                        });
                    });
        });
    }

    private void bindingViews(View view) {
        mBtnBack = view.findViewById(R.id.btn_back);
        mBtnCreateRoom = view.findViewById(R.id.btn_create_room);
        mBtnDelete = view.findViewById(R.id.btn_delete);
        mBtnSave = view.findViewById(R.id.btn_save);

        mGridRooms = view.findViewById(R.id.grid_rooms);

        mInpDistrict = view.findViewById(R.id.inp_district);
        mInpLat = view.findViewById(R.id.inp_lat);
        mInpLong = view.findViewById(R.id.inp_long);
        mInpName = view.findViewById(R.id.inp_name);
        mInpStreet = view.findViewById(R.id.inp_street);
        mInpWard = view.findViewById(R.id.inp_ward);

        mSpnProvinces = view.findViewById(R.id.spn_provinces);

        mTxtTitle = view.findViewById(R.id.txt_title);
    }

    private void setupViews() {
        mBtnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());
        mBtnCreateRoom.setOnClickListener(_v -> {
            AddRoom dialog = new AddRoom();
            dialog.show(getChildFragmentManager(), "Dialog");
        });
        mBtnDelete.setImageResource(R.drawable.ic_delete);
        mBtnDelete.setOnClickListener(_v -> {
            CinemaController.getInstance().delete(cinema.getId());
            Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
        });
        mBtnSave.setOnClickListener(_v -> {
            Address addr = cinema.getAddress();
            addr.setStreet(mInpStreet.getText().toString());
            addr.setWard(mInpWard.getText().toString());
            addr.setDistrict(mInpDistrict.getText().toString());
            addr.setProvince(mSpnProvinces.getSelectedItem().toString());
            addr.setLatitude(Double.parseDouble(mInpLat.getText().toString()));
            addr.setLongitude(Double.parseDouble(mInpLong.getText().toString()));

            cinema.setName(mInpName.getText().toString());
            cinema.setAddress(addr);

            try {
                CinemaController.getInstance().update(cinema.getId(), cinema);
                Toast.makeText(getContext(), "Chỉnh sửa thành công!", Toast.LENGTH_SHORT).show();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        mGridRooms.setAdapter(roomAdapter);
        mGridRooms.setOnItemClickListener((adapterView, view, i, l) -> {
            SelectContext.room = roomAdapter.getItem(i);
            EditRoom dialog = new EditRoom();
            dialog.show(getChildFragmentManager(), "Dialog");
        });
        mSpnProvinces.setAdapter(provinceAdapter);

        mTxtTitle.setText("CHỈNH SỬA RẠP");
    }

    @Override
    public void reload() {
        CinemaController
                .getInstance()
                .getRef(cinema.getId())
                .collection("rooms")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    roomAdapter.clear();
                    queryDocumentSnapshots.forEach(doc -> {
                        Room m = doc.toObject(Room.class);
                        m.setId(doc.getId());
                        roomAdapter.add(m);
                        roomAdapter.notifyDataSetChanged();
                    });
                });
    }
}
