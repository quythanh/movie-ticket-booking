package com.example.movie_ticket_booking.Components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Common.ItemClickListener;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Models.SeatType;
import com.example.movie_ticket_booking.R;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder>{
    private Context context;
    private char row;
    private int maxSeat;
    private int totalLine;
    private List<String> solds;
    private SeatType type;

    public SeatAdapter(Context c, char r, int m, int t, List<String> s){
        this.context = c;
        this.row = r;
        this.totalLine = t;
        this.maxSeat = m;
        this.solds = s;

        type = UIManager.getSeatType(row, totalLine);
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SeatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_seat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        boolean isSold = false;
        holder.seatNumber.setText(String.format("%c%d",row, position+1));
        if(solds.indexOf(String.format("%s%d", row, position+1)) != -1){
            isSold = true;
            switch (type){
                case VIP:
                    holder.seatBackground.setBackgroundResource(R.drawable.vip_sold_seat);
                    break;
                case STANDARD:
                    holder.seatBackground.setBackgroundResource(R.drawable.normal_sold_seat);
                    break;
                case COUPLE:
                    if(position%2==0)
                        holder.seatBackground.setBackgroundResource(R.drawable.couple_sold_left_seat);
                    else
                        holder.seatBackground.setBackgroundResource(R.drawable.couple_sold_right_seat);

                    break;
            }
        }
        else if(!CheckExist(position)){
            switch (type){
                case VIP:
                    holder.seatBackground.setBackgroundResource(R.drawable.vip_seat);
                    break;
                case STANDARD:
                    holder.seatBackground.setBackgroundResource(R.drawable.normal_seat);
                    break;
                case COUPLE:
                    if(position%2==0)
                        holder.seatBackground.setBackgroundResource(R.drawable.couple_left_seat);
                    else
                        holder.seatBackground.setBackgroundResource(R.drawable.couple_right_seat);

                    break;
            }

        }else
        {
            switch (type) {
                case VIP:
                    holder.seatBackground.setBackgroundResource(R.drawable.vip_selected_seat);
                    break;
                case STANDARD:
                    holder.seatBackground.setBackgroundResource(R.drawable.normal_selected_seat);
                    break;
                case COUPLE:
                    if(position%2==0)
                        holder.seatBackground.setBackgroundResource(R.drawable.couple_selected_left_seat);
                    else
                        holder.seatBackground.setBackgroundResource(R.drawable.couple_selected_right_seat);
                    break;
            }
        }

        boolean finalIsSold = isSold;
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                if(finalIsSold) return;
                Map<Character, List<Integer>> temp = AuthUserController.getInstance().getBookingSeat().getValue();
                if(!CheckExist(position))
                {
                    temp.get(row).add(position);
                    if(type == SeatType.COUPLE){
                        if(position%2 == 0)
                            temp.get(row).add(position + 1);
                        else
                            temp.get(row).add(position -1);
                    }

                }else
                {

                    temp.get(row).remove(temp.get(row).indexOf(position));
                    if(type == SeatType.COUPLE){
                        if(position%2 == 0){
                            temp.get(row).remove(temp.get(row).indexOf(position + 1));
                        }
                        else
                            temp.get(row).remove(temp.get(row).indexOf(position - 1));
                    }
                }
                Log.d("seat", temp.get(row).toString());
                AuthUserController.getInstance().getBookingSeat().setValue(temp);
                notifyDataSetChanged();
            }
        });
    }

    private boolean CheckExist(int position) {
        AuthUserController controller = AuthUserController.getInstance();
        int pos = -1;
        Map<Character, List<Integer>> temp = controller.getBookingSeat().getValue();
        for(int i = 0; i<temp.get(row).size(); i++){
            if(temp.get(row).get(i) == position)
                pos = temp.get(row).get(i);
        }
        return pos != -1;
    }

    @Override
    public int getItemCount() {
        return maxSeat;
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Setter
        private ItemClickListener itemClickListener;
        private LinearLayout seatBackground;
        private TextView seatNumber;
        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatBackground = itemView.findViewById(R.id.seatBackground);
            seatNumber = itemView.findViewById(R.id.seatNumber);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.OnClick(v,getBindingAdapterPosition(), false);
        }
    }
}
