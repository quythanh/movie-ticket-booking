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

import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Models.Address;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.R;

public class AddCinemaDialog extends DialogFragment {

    private EditText mInpName, mInpStreet, mInpDistrict, mInpWard, mInpLong, mInpLat;
    private Spinner mSpnProvinces;

    private ArrayAdapter<String> provinceAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Thêm Rạp");
        builder.setPositiveButton("Thoát", null);
        builder.setNegativeButton("Thêm", (dialogInterface, i) -> {
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

        View view = getLayoutInflater().inflate(R.layout.frag_admin_add_cinema, null);
        builder.setView(view);
        initData();
        bindingViews(view);
        setupViews();

        return builder.create();
    }

    private void initData() {
        provinceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.provinces));
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
