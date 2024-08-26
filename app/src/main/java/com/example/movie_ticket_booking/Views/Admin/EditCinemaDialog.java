package com.example.movie_ticket_booking.Views.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Models.Address;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.R;

public class EditCinemaDialog extends DialogFragment {

    private EditText mInpName, mInpStreet, mInpDistrict, mInpWard, mInpLong, mInpLat;
    private Spinner mSpnProvinces;

    private Cinema cinema;
    private ArrayAdapter<String> provinceAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Chỉnh sửa Rạp");
        builder.setPositiveButton("Thoát", null);
        builder.setNeutralButton("Xóa", (dialogInterface, i) -> {
            CinemaController.getInstance().delete(cinema.getId());
        });
        builder.setNegativeButton("Sửa", (dialogInterface, i) -> {
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

        View view = getLayoutInflater().inflate(R.layout.frag_admin_edit_cinema, null);
        builder.setView(view);
        initData();
        bindingViews(view);
        setupViews();
        loadViewsData();

        return builder.create();
    }

    private void initData() {
        provinceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.provinces));
    }

    private void loadViewsData() {
        ManageCinemas.getSelectedCinema().observe(getParentFragment().getViewLifecycleOwner(), _cinema -> {
            cinema = _cinema;

            mInpName.setText(cinema.getName());
            mInpStreet.setText(cinema.getAddress().getStreet());
            mInpDistrict.setText(cinema.getAddress().getDistrict());
            mInpWard.setText(cinema.getAddress().getWard());
            mSpnProvinces.setSelection(provinceAdapter.getPosition(cinema.getAddress().getProvince()));
            mInpLat.setText(Double.toString(cinema.getAddress().getLatitude()));
            mInpLong.setText(Double.toString(cinema.getAddress().getLongitude()));
        });
    }

    private void bindingViews(View view) {
        mInpName = view.findViewById(R.id.inp_name);
        mInpStreet = view.findViewById(R.id.inp_street);
        mInpDistrict = view.findViewById(R.id.inp_district);
        mInpWard = view.findViewById(R.id.inp_ward);
        mSpnProvinces = view.findViewById(R.id.spn_provinces);
        mInpLong = view.findViewById(R.id.inp_long);
        mInpLat = view.findViewById(R.id.inp_lat);
    }

    private void setupViews() {
        mSpnProvinces.setAdapter(provinceAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IReloadOnDestroy f = (ManageCinemas) getParentFragment();
        f.reload();
    }
}
