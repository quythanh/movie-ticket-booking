package com.example.movie_ticket_booking.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Common.ItemClickListener;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
public class WeekdateApdater extends  RecyclerView.Adapter<WeekdateApdater.WeekdateViewHolder>{

    private Context context;
    private List<Date> weekdates;
    @Getter
    private MutableLiveData<Date> date_data;

    public WeekdateApdater(Context context){
        this.context = context;
        date_data = new MutableLiveData<>();
        Date date = new Date();
        long day_time = 1000 * 60 * 60 * 24;
        weekdates = new ArrayList<>();
        weekdates.add(date);
        for(int i = 1; i<=6; i++){
            Date temp = new Date();
            temp.setTime(date.getTime() + i * day_time);
            weekdates.add(temp);
        }
    }

    @NonNull
    @Override
    public WeekdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeekdateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_weekdate_item, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull WeekdateViewHolder holder, int position) {
        Date d = this.weekdates.get(position);
        holder.date.setText(String.format("%d", d.getDate()));
        holder.month.setText(String.format("Tháng %d", d.getMonth() + 1));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                date_data.setValue(weekdates.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.weekdates.size();
    }

    public static class WeekdateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private TextView date, month;
        @Setter
        private ItemClickListener itemClickListener;

        public WeekdateViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.weekdate_date);
            month = itemView.findViewById(R.id.weekdate_month);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.OnClick(v, getBindingAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.OnClick(v, getBindingAdapterPosition(), true);
            return true;
        }
    }
}
