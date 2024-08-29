package com.example.movie_ticket_booking.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInTicket extends DetailTicket{
    private DocumentReference product;
    private int amount;

    public ProductInTicket(DetailTicket.DetailType T, DocumentReference p, int a){
        this.detailType = T;
        this.product = p;
        this.amount = a;
    }
    @NonNull
    @Override
    public String toString() {
        return this.product.toString();
    }
}
