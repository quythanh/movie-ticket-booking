package com.example.movie_ticket_booking.Components;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common.GenericAdapter;
import com.example.movie_ticket_booking.Controllers.UserController;
import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.R;

import java.util.List;

public class UserAdapter extends GenericAdapter<User> {

    public UserAdapter(List<User> list) {
        super(list, R.layout.item_user);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = super.getView(i, view, viewGroup);

        User u = this.getItem(i);
        String avatarUrl = u.getAvatarPath();

        // Binding Views
        TextView txtName = view.findViewById(R.id.txt_name);
        ImageView btnDelete = view.findViewById(R.id.btn_delete);
        ImageView imgAvatar = view.findViewById(R.id.img_avatar);

        // Load Views
        txtName.setText(u.getFullName());
        if (avatarUrl != null)
            Glide.with(view).load(avatarUrl).into(imgAvatar);

        // Setup Views
        btnDelete.setOnClickListener(_v -> {
            UserController.getInstance().delete(u.getId());
            Log.d("admin_manage_user",
                    String.format("Delete user %s - id: %s", u.getFullName(), u.getId()));
        });

        return view;
    }
}
