package com.example.movie_ticket_booking.Views.Admin;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.Controllers.TicketController;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Statistic extends Fragment {
    private static Statistic _instance = null;

    private BarChart mChart;
    private EditText mInpRange;
    private Spinner mSpnRangeUnit;
    private TextView mTxtName;

    private BarDataSet barDataSet;
    private BarData barData;
    private ArrayAdapter<Constant.Time> unitAdapter;
    private long startDate, endDate;

    private Statistic() {
        super(R.layout.frag_admin_statistic);
    }

    public static Statistic getInstance() {
        if (_instance == null)
            _instance = new Statistic();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        bindingViews(view);
        setupViews();
        loadViewsData();
        return view;
    }

    private void initData() {
        barDataSet = new BarDataSet(new ArrayList<>(), "Doanh thu");

        try {
            endDate = Constant.DATE_FORMATTER.parse(Constant.DATE_FORMATTER.format(new Date()) + " 00:00").getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        startDate = endDate - Constant.Time.WEEK.value;
        unitAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Constant.Time.values());
    }

    private void bindingViews(View view) {
        mChart = view.findViewById(R.id.chart_bar);
        mInpRange = view.findViewById(R.id.inp_range);
        mSpnRangeUnit = view.findViewById(R.id.spn_range_unit);
        mTxtName = view.findViewById(R.id.txt_name);
    }

    private void setupViews() {
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        barData = new BarData(barDataSet);

        mChart.setFitBars(true);
        mChart.getDescription().setText("");
        mChart.animateY(500);
        mChart.setData(barData);

        mSpnRangeUnit.setAdapter(unitAdapter);
        mSpnRangeUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadViewsData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mInpRange.setOnClickListener(_v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Select a date range");

            // Building the date picker dialog
            MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                // Retrieving the selected start and end dates
                startDate = selection.first;
                endDate = selection.second;

                // Displaying the selected date range in the TextView
                mInpRange.setText(_convertRangeDateToString(startDate, endDate));
            });

            // Showing the date picker dialog
            datePicker.show(getChildFragmentManager(), "DATE_PICKER");
        });
        mInpRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadViewsData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mInpRange.setText(_convertRangeDateToString(startDate, endDate));

        mTxtName.setText(AuthUserController.getInstance().getUserlogin().getValue().getFullName());
    }

    private void loadViewsData() {
        // Reset chart data
        barDataSet.clear();
        barData = new BarData(barDataSet);
        mChart.setData(barData);
        mChart.animateY(500);

        // Loop
        long unit = ((Constant.Time) mSpnRangeUnit.getSelectedItem()).value;
        for (long date = startDate; date <= endDate; date += unit) {
            int finalX = Math.toIntExact((date - startDate) / unit) + 1;
            long finalDate = date;

            long end = date + unit;
            if (end > endDate)
                end = endDate + Constant.Time.DAY.value;

            TicketController.getInstance().getRevenue(date, end).observe(getViewLifecycleOwner(), _sum -> {
                Log.d("Statistic",
                        String.format(
                            "%s - %s: %d",
                            Constant.DATETIME_FORMATTER.format(new Date(finalDate)),
                            Constant.DATETIME_FORMATTER.format(new Date(finalDate + unit)),
                            _sum
                        ));
                barDataSet.addEntry(new BarEntry(finalX, _sum));
                barData = new BarData(barDataSet);
                mChart.setData(barData);
                mChart.animateY(500);
                mChart.invalidate();
            });
        }
    }

    private String _convertRangeDateToString(long start, long end) {
        String startDateString = Constant.DATE_FORMATTER.format(new Date(start));
        String endDateString = Constant.DATE_FORMATTER.format(new Date(end));

        return startDateString + " - " + endDateString;
    }
}
