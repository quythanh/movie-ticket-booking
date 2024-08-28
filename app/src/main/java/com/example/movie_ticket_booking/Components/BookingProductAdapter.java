package com.example.movie_ticket_booking.Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingProductAdapter extends BaseAdapter {

    private List<Product> products;
    private Bundle bundle;

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product_item, parent, false);
        TextView name, description, amount, price;
        Button minus, add;
        name = view.findViewById(R.id.productName);
        description = view.findViewById(R.id.productDescription);
        amount = view.findViewById(R.id.amount);
        price = view.findViewById(R.id.pricePD);
        minus = view.findViewById(R.id.minusBtn);
        add = view.findViewById(R.id.addBtn);
        Product p = products.get(position);

        name.setText(p.getName());
        description.setText(p.getDescription());
        price.setText(Constant.currencyFormatter.format(p.getUnitPrice()));


        final int[] a = {Integer.parseInt(amount.getText().toString())};
        final String key = p.getId();

        if(bundle !=null && bundle.getBundle("products") != null && bundle.getBundle("products").get(p.getId()) != null){
            a[0] = bundle.getBundle("products").getInt(key);
            amount.setText(String.format("%d",a[0]));
        }

        add.setOnClickListener(v -> {
            a[0] += 1;

            Bundle b = new Bundle();
            if(bundle.getBundle("products") != null){
                b = bundle.getBundle("products");
            }
            b.putInt(key, a[0]);
            bundle.putBundle("products", b);
            amount.setText(String.format("%d", a[0]));
        });

        minus.setOnClickListener(v -> {
            if (a[0] == 0) return;

            a[0] -= 1;
            Bundle b = bundle.getBundle("products");
            if (a[0] == 0)
                b.remove(key);
            else
                b.putInt(key, a[0]);
            if (bundle.getBundle("products").isEmpty())
                bundle.remove("products");
            else
                bundle.putBundle("products", b);
            amount.setText(String.format("%d", a[0]));
        });
        return view;
    }
}
