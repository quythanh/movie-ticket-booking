package com.example.movie_ticket_booking.Components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.R;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedProductAdapter extends BaseAdapter {

    @Getter
    private Map<Product, Integer> prods;

    @Override
    public int getCount() {
        return prods.size();
    }

    @Override
    public Object getItem(int position) {
        int i =0;
        for(Map.Entry<Product, Integer> x : prods.entrySet()){
            if(i == position)
                return  x;
            i++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_confirmed_product_item, parent, false);
        TextView name, amount, desc, price;
        name = view.findViewById(R.id.productName);
        amount = view.findViewById(R.id.amount);
        desc = view.findViewById(R.id.productDescription);
        price = view.findViewById(R.id.pricePD);
        Map.Entry<Product, Integer> p = (Map.Entry<Product, Integer>) getItem(position);

        name.setText(p.getKey().getName());
        amount.setText(String.format("%d", p.getValue()));
        desc.setText(p.getKey().getDescription());
        price.setText(Constant.currencyFormatter.format(p.getKey().getUnitPrice()));

        return view;
    }
}
