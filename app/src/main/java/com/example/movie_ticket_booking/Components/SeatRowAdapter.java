package com.example.movie_ticket_booking.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class SeatRowAdapter extends RecyclerView.Adapter<SeatRowAdapter.SeatRowViewHolder>{

    private List<Integer> seatMap;
    private Context c;


    public SeatRowAdapter(Context c, List<Integer> seatMap){
        this.c = c;
        this.seatMap = seatMap;
        Map<Character, List<Integer>> temp = new HashMap<>();
        for(int i = 0; i<seatMap.size(); i++)
        {
            char t = (char) (i+65);
            temp.put(t, new ArrayList<>());
        }
        AuthUserController.getInstance().getBookingSeat().setValue(temp);

    }
    @NonNull
    @Override
    public SeatRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertial_seat_row_item, parent, false);
        return new SeatRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatRowViewHolder holder, int position) {
        holder.seatRow.setAdapter(new SeatAdapter(c, (char)(position+65),seatMap.get(position), seatMap.size()));
    }

    @Override
    public int getItemCount() {
        return seatMap.size();
    }

    public static class SeatRowViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView seatRow;
        public SeatRowViewHolder(@NonNull View itemView) {
            super(itemView);
            seatRow = itemView.findViewById(R.id.seatRow);
            seatRow.setHasFixedSize(true);
            seatRow.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        }

    }
}
