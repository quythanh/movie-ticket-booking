package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Common.SelectContext;
import com.example.movie_ticket_booking.Components.TicketListAdapter;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Controllers.ShowtimeController;
import com.example.movie_ticket_booking.Controllers.TicketController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.Showtime;
import com.example.movie_ticket_booking.Models.Ticket;
import com.example.movie_ticket_booking.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageTickets extends Fragment implements IReloadOnDestroy {

    private static ManageTickets _instance = null;

    private ImageView mBtnBack;
    private ListView mListTickets;
    private Spinner mSpnCinemas, mSpnMovies;

    private ArrayAdapter<Cinema> cinemaAdapter;
    private ArrayAdapter<Movie> movieAdapter;
    private TicketListAdapter ticketAdapter;
    private MutableLiveData<Map<String, Object>> filters;

    private ManageTickets() {
        super(R.layout.frag_admin_manage_tickets);
    }

    public static ManageTickets getInstance() {
        if (_instance == null)
            _instance = new ManageTickets();
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
        getTicketsData();
        return view;
    }

    private void initData() {
        cinemaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        movieAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        ticketAdapter = new TicketListAdapter(getViewLifecycleOwner(), new ArrayList<>());
        filters = new MutableLiveData<>(new HashMap<>());
    }

    private void bindingViews(View view) {
        mBtnBack = view.findViewById(R.id.btn_back);

        mListTickets = view.findViewById(R.id.list_tickets);

        mSpnCinemas = view.findViewById(R.id.spn_cinemas);
        mSpnMovies = view.findViewById(R.id.spn_movies);
    }

    private void setupViews() {
        mBtnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());

        mListTickets.setAdapter(ticketAdapter);
        mSpnCinemas.setAdapter(cinemaAdapter);
        mSpnMovies.setAdapter(movieAdapter);

        mListTickets.setOnItemClickListener((adapterView, view, i, l) -> {
            SelectContext.ticket = ticketAdapter.getItem(i);
            EditTicketDialog dialog = new EditTicketDialog();
            dialog.show(getChildFragmentManager(), "Dialog");
        });
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

    private void getTicketsData() {
        filters.observe(getViewLifecycleOwner(), _map -> {
            Cinema _c = (Cinema) _map.get("cinema");
            String _m = (String) _map.get("movieId");

            if (_c == null || _m == null)
                return;

            TicketController
                    .getInstance()
                    .getAll()
                    .observe(getViewLifecycleOwner(), _tickets -> {
                        DocumentReference cinemaRef = CinemaController.getInstance().getRef(_c.getId());
                        _tickets
                                .stream()
                                .filter(_ticket -> {
                                    // Filter all ticket in selected Cinema
                                    DocumentReference cinemaRefOfTicket = _ticket.getShowtime().getParent().getParent();
                                    return cinemaRef.equals(cinemaRefOfTicket);
                                })
                                .forEach(_ticket -> {
                                    ticketAdapter.clear();
                                    _ticket.getShowtime().get().addOnSuccessListener(documentSnapshot -> {
                                        Showtime _s = documentSnapshot.toObject(Showtime.class);
                                        if (_s.getMovie().equals(_m))
                                            ticketAdapter.add(_ticket);
                                    });
                                });
                    });
        });
    }

    @Override
    public void reload() {
        getTicketsData();
    }
}
