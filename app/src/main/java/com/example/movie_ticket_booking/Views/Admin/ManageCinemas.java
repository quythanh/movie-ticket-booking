package com.example.movie_ticket_booking.Views.Admin;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.GenericFilter;
import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Components.GridCinemaAdapter;
import com.example.movie_ticket_booking.Controllers.CinemaController;
import com.example.movie_ticket_booking.Models.Cinema;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

public class ManageCinemas extends Fragment implements IReloadOnDestroy {
    private static ManageCinemas _instance = null;
    @Getter private static MutableLiveData<Cinema> selectedCinema = new MutableLiveData<>();

    private EditText mInpAddress;
    private ImageView mBtnAdd, mBtnBack;
    private ListView mListCinemas;
    private Spinner mSpnProvince;

    private CinemaController _controller;
    private GenericFilter<Cinema> filters;
    private GridCinemaAdapter adapter;
    private ArrayAdapter<String> provinceAdapter;

    public ManageCinemas() {
        super(R.layout.frag_admin_manage_cinemas);
    }

    public static ManageCinemas getInstance() {
        if (_instance == null)
            _instance = new ManageCinemas();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        loadViewsData();
        bindingViews(view);
        setupViews();
        return view;
    }

    private void initData() {
        _controller = CinemaController.getInstance();
        filters = new GenericFilter<>(Cinema.class);
        adapter = new GridCinemaAdapter(new ArrayList<>());
        provinceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
    }

    private void loadViewsData() {
        // Get all provinces
        try {
            _controller.getAll()
                    .observe(getViewLifecycleOwner(), cinemas -> {
                        Set<String> provinces = new HashSet<>();
                        cinemas.forEach(cinema -> provinces.add(cinema.getAddress().getProvince()));
                        provinceAdapter.clear();
                        provinceAdapter.addAll(provinces.stream().sorted().collect(Collectors.toList()));
                        provinceAdapter.notifyDataSetChanged();
                    });
        } catch (Exception e) {
            Log.e("Branches", e.toString());
        }

        _controller.filter(filters.get()).observe(getViewLifecycleOwner(), cinemas -> {
            adapter.setList(cinemas);
            adapter.notifyDataSetChanged();
        });
    }

    private void bindingViews(View view) {
        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnBack = view.findViewById(R.id.btn_back);
        mInpAddress = view.findViewById(R.id.inp_address);
        mListCinemas = view.findViewById(R.id.list_cinemas);
        mSpnProvince = view.findViewById(R.id.spn_province);
    }

    private void setupViews() {
        mBtnAdd.setOnClickListener(_v -> {
            AddCinemaDialog dialog = new AddCinemaDialog();
            dialog.show(getChildFragmentManager(), "Dialog");
        });
        mBtnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());
        mListCinemas.setAdapter(adapter);
        mSpnProvince.setAdapter(provinceAdapter);

        mInpAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = mInpAddress.getText().toString().trim().toLowerCase();
                filters.set(FilterType.STRING_CONTAINS, "address.street", searchText.length() >= 3 ? searchText : null);
                loadViewsData();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mListCinemas.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedCinema.setValue(adapter.getItem(i));
            EditCinemaDialog dialog = new EditCinemaDialog();
            dialog.show(getChildFragmentManager(), "Dialog");
        });

        mSpnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filters.set(FilterType.EQUAL, "address.province", provinceAdapter.getItem(i));
                loadViewsData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void reload() {
        loadViewsData();
    }
}
