package com.example.movie_ticket_booking.Components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_booking.Common.ItemClickListener;
import com.example.movie_ticket_booking.R;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HorizontalStringAdapter extends RecyclerView.Adapter<HorizontalStringAdapter.ViewHolder> {
    private List<String> items;

    @NonNull
    @Override
    public HorizontalStringAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent,false);
        return new HorizontalStringAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalStringAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
        holder.setItemClickListener((_v, _pos, isLongClick) -> {
            items.remove(_pos);
            this.notifyItemRemoved(_pos);
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Setter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);

            itemView.setOnLongClickListener(_v -> {
                itemClickListener.OnClick(_v, getBindingAdapterPosition(), true);
                return false;
            });
        }

        public void bind(String string) {
            this.text.setText(string);
        }
    }
}
