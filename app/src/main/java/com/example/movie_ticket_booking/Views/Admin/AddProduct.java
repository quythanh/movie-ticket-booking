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

import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.ProductController;
import com.example.movie_ticket_booking.Models.Address;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.R;

public class AddProduct extends DialogFragment {

    private EditText mInpName, mInpPrice, mInpDescription;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Thêm sản phẩm");
        builder.setPositiveButton("Thoát", null);
        builder.setNegativeButton("Thêm", (dialogInterface, i) -> {
            Product product = new Product(
                    mInpName.getText().toString(),
                    Integer.parseInt(mInpPrice.getText().toString()),
                    mInpDescription.getText().toString()
            );
            ProductController.getInstance().add(product);
            Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
        });

        View view = getLayoutInflater().inflate(R.layout.frag_admin_info_product, null);
        builder.setView(view);
        bindingViews(view);

        return builder.create();
    }

    private void bindingViews(View view) {
        mInpName = view.findViewById(R.id.inp_name);
        mInpPrice = view.findViewById(R.id.inp_price);
        mInpDescription = view.findViewById(R.id.inp_description);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IReloadOnDestroy f = (ManageProducts) getParentFragment();
        f.reload();
    }
}
