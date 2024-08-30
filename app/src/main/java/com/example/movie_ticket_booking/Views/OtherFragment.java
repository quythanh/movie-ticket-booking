package com.example.movie_ticket_booking.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.TicketController;
import com.example.movie_ticket_booking.MainActivity;
import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.util.ArrayList;

public class OtherFragment extends Fragment {
    private static OtherFragment _instance = null;

    private OtherFragment() {
        // Required empty public constructor
    }

    public static OtherFragment getInstance() {
        if (_instance == null)
            _instance = new OtherFragment();
        return _instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_other_fragment, container, false);
        BarChart chart;
        TextView textView = v.findViewById(R.id.userName);
        ImageView img = v.findViewById(R.id.userAvatar);
        Button btn = v.findViewById(R.id.logoutBtn);
        chart = v.findViewById(R.id.barChart);

        User u = AuthUserController.getInstance().getUserlogin().getValue();
        textView.setText(u.getFullName());
        Glide.with(v.getContext()).load(u.getAvatarPath()).into(img);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUserController.getInstance().getUserlogin().setValue(new User());
                UIManager.changeFragment(getParentFragmentManager(), FilmFragment.getInstance());
            }
        });

        try {
            TicketController.getInstance().statisticMyTicketByMonth().observe(getViewLifecycleOwner(), map -> {
                if (map == null) return;

                ArrayList<BarEntry> statEntry = new ArrayList<>();
                for(int i = 0; i<12; i++){
                    statEntry.add(new BarEntry(i+1, map.get(i) == null ? 0 : map.get(i)));
                }
//                statEntry.add(new BarEntry(1, 10));
//                statEntry.add(new BarEntry(2, 12));
//                statEntry.add(new BarEntry(3, 1));
//                statEntry.add(new BarEntry(4, 5));
//                statEntry.add(new BarEntry(5, 8));
//                statEntry.add(new BarEntry(6, 0));
//                statEntry.add(new BarEntry(7, 20));
//                statEntry.add(new BarEntry(8, 1));
//                statEntry.add(new BarEntry(9, 6));
//                statEntry.add(new BarEntry(10, 7));
//                statEntry.add(new BarEntry(11, 9));
//                statEntry.add(new BarEntry(12, 10));

                BarDataSet barDataSet = new BarDataSet(statEntry, "Số lượng vé");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(5f);

                BarData barData = new BarData(barDataSet);
                chart.setFitBars(true);
                chart.setData(barData);
                chart.getDescription().setText("Thống kê số vé đặt trong năm qua");
                chart.animateY(2000);
            });
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return v;
    }
}