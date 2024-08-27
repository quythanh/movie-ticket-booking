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
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.ProductController;
import com.example.movie_ticket_booking.Models.Address;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.R;

public class EditProduct extends DialogFragment {

    private EditText mInpName, mInpPrice, mInpDescription;

    private Product prod;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Chỉnh sửa sản phẩm");
        builder.setPositiveButton("Thoát", null);
        builder.setNeutralButton("Xóa", (dialogInterface, i) -> {
            ProductController.getInstance().delete(prod.getId());
            Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Sửa", (dialogInterface, i) -> {

            prod.setName(mInpName.getText().toString());
            prod.setUnitPrice(Integer.parseInt(mInpPrice.getText().toString()));
            prod.setDescription(mInpDescription.getText().toString());

            try {
                ProductController.getInstance().update(prod.getId(), prod);
                Toast.makeText(getContext(), "Chỉnh sửa thành công!", Toast.LENGTH_SHORT).show();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        View view = getLayoutInflater().inflate(R.layout.frag_admin_info_product, null);
        builder.setView(view);
        bindingViews(view);
        loadViewsData();

        return builder.create();
    }

    private void bindingViews(View view) {
        mInpName = view.findViewById(R.id.inp_name);
        mInpPrice = view.findViewById(R.id.inp_price);
        mInpDescription = view.findViewById(R.id.inp_description);
    }

    private void loadViewsData() {
        EditContext.product.observe(getParentFragment().getViewLifecycleOwner(), _prod -> {
            prod = _prod;

            mInpName.setText(prod.getName());
            mInpPrice.setText(Integer.toString(prod.getUnitPrice()));
            mInpDescription.setText(prod.getDescription());
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IReloadOnDestroy f = (ManageProducts) getParentFragment();
        f.reload();
    }
}
