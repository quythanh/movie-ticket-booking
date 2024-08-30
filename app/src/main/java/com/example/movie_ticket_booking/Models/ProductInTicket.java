package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInTicket extends DetailTicket {
    private DocumentReference product;
    private int amount;

    public ProductInTicket(DetailTicket.DetailType T, DocumentReference p, int a){
        super(T);
        this.product = p;
        this.amount = a;
    }
    @NonNull
    @Override
    public String toString() {
        return this.product.toString();
    }

    public static ProductInTicket parse(Map<String, Object> y) {
        DetailTicket.DetailType _type = DetailTicket.DetailType.PRODUCT;
        DocumentReference _prodRef = (DocumentReference) y.get("product");
        int _amount = Math.toIntExact((Long) y.get("amount"));
        return new ProductInTicket(_type, _prodRef, _amount);
    }
}
