package com.example.movie_ticket_booking.Views.Admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.EditContext;
import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Components.ShowtimeGridAdapter;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Controllers.RoomController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.R;
import com.google.firebase.firestore.FieldPath;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ManageShowtimes extends Fragment {

    private static ManageShowtimes _instance = null;

    private EditText mInpDate;
    private GridView mGridShowtimes;
    private ImageView mBtnAdd, mBtnBack;
    private Spinner mSpnCinemas, mSpnMovies;

    private ArrayAdapter<Cinema> cinemaAdapter;
    private ArrayAdapter<Movie> movieAdapter;
    private ShowtimeGridAdapter showtimeAdapter;
    private MutableLiveData<Map<String, Object>> filters;

    private ManageShowtimes() {
        super(R.layout.frag_admin_manage_showtimes);
    }

    public static ManageShowtimes getInstance() {
        if (_instance == null)
            _instance = new ManageShowtimes();
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
        getShowtimesData();
        return view;
    }

    private void initData() {
        cinemaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        movieAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        showtimeAdapter = new ShowtimeGridAdapter(new ArrayList<>());
        filters = new MutableLiveData<>(new HashMap<>());
    }

    private void bindingViews(View view) {
        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnBack = view.findViewById(R.id.btn_back);

        mGridShowtimes = view.findViewById(R.id.grid_showtimes);

        mInpDate = view.findViewById(R.id.inp_date);

        mSpnCinemas = view.findViewById(R.id.spn_cinemas);
        mSpnMovies = view.findViewById(R.id.spn_movies);
    }

    private void setupViews() {
        mBtnAdd.setOnClickListener(_v -> {
            Cinema _c = (Cinema) mSpnCinemas.getSelectedItem();
            if (_c == null) return;

            Movie _m = (Movie) mSpnMovies.getSelectedItem();
            if (_m == null) return;

            EditContext.cinema.setValue(_c);
            EditContext.movie.setValue(_m);
            AddShowtimeDialog dialog = new AddShowtimeDialog();
            dialog.show(getChildFragmentManager(), "Dialog");
        });
        mBtnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());

        mGridShowtimes.setAdapter(showtimeAdapter);

        mInpDate.setText(Constant.DATE_FORMATTER.format(new Date()));
        mInpDate.setOnClickListener(_v -> {
            Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            DatePickerDialog picker = new DatePickerDialog(
                    getContext(),
                    (_view, _year, _month, _day) -> {
                        Date date = new GregorianCalendar(_year, _month, _day).getTime();
                        String strDate = Constant.DATE_FORMATTER.format(date);
                        mInpDate.setText(strDate);
                    },
                    year, month, day
            );
            picker.show();
        });
        mInpDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Map<String, Object> m = filters.getValue();
                String strDate = mInpDate.getText().toString();
                if (strDate.isEmpty()) return;

                try {
                    Date _d = Constant.DATE_FORMATTER.parse(strDate);
                    m.put("date", _d);
                    filters.setValue(m);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSpnCinemas.setAdapter(cinemaAdapter);
        mSpnMovies.setAdapter(movieAdapter);

        mSpnCinemas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> m = filters.getValue();
                Cinema _c = (Cinema) mSpnCinemas.getSelectedItem();
                m.put("cinema", _c);
                filters.setValue(m);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpnMovies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> m = filters.getValue();
                Movie _m = (Movie) mSpnMovies.getSelectedItem();
                m.put("movieId", _m.getId());
                filters.setValue(m);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadViewsData() {
        CinemaController.getInstance().getAll().observe(getViewLifecycleOwner(), _cinemas -> {
            cinemaAdapter.clear();
            cinemaAdapter.addAll(_cinemas);
            cinemaAdapter.notifyDataSetChanged();
        });
        MovieController.getInstance().getAll().observe(getViewLifecycleOwner(), _movies -> {
            movieAdapter.clear();
            movieAdapter.addAll(_movies);
            movieAdapter.notifyDataSetChanged();
        });
    }

    private void getShowtimesData() {
        filters.observe(getViewLifecycleOwner(), _map -> {
            Date _d = (Date) _map.get("date");
            Cinema _c = (Cinema) _map.get("cinema");
            String _m = (String) _map.get("movieId");

            if (_d == null || _c == null || _m == null)
                return;

            try {
                ShowtimeController
                        .getInstance()
                        .getShowtime(_m, _c, _d)
                        .observe(getViewLifecycleOwner(), _showtimes -> showtimeAdapter.setList(_showtimes));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
