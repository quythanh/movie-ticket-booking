package com.example.movie_ticket_booking.Components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movie_ticket_booking.Common.GenericAdapter;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.R;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends GenericAdapter<Product> {
    public ProductAdapter(List<Product> list) {
        super(list, R.layout.item_product);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = super.getView(i, view, viewGroup);
        Product p = this.getItem(i);
        TextView mTxtName, mTxtPrice, mTxtDescription;

        mTxtName = view.findViewById(R.id.txt_name);
        mTxtPrice = view.findViewById(R.id.txt_price);
        mTxtDescription = view.findViewById(R.id.txt_description);

        mTxtName.setText(p.getName());
        mTxtPrice.setText(String.format(Locale.getDefault(),"%d VNĐ", p.getUnitPrice()));
        mTxtDescription.setText(p.getDescription());

        return view;
    }
}
