package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common;
import com.example.movie_ticket_booking.Components.UserAdapter;
import com.example.movie_ticket_booking.Controllers.UserController;
import com.example.movie_ticket_booking.Models.FilterType;
import com.example.movie_ticket_booking.Models.GenericFilter;
import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.Models.UserRole;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class ManageUsers extends Fragment {
    private static ManageUsers _instance = null;
    private final UserController _controller = UserController.getInstance();

    private EditText inpName;
    private ImageView btnBack;
    private ListView listViewUsers;
    private RadioButton rdStatusActive;
    private Spinner spnRole;

    @Getter
    private static MutableLiveData<User> selectedUser = new MutableLiveData<>();
    private GenericFilter<User> filters = new GenericFilter<>(User.class);

    private ManageUsers() {
        super(R.layout.frag_admin_manage_users);
    }

    public static ManageUsers getInstance() {
        if (_instance == null)
            _instance = new ManageUsers();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        bindingViews(view);
        setupViews();
        return view;
    }

    private void bindingViews(@Nullable View view) {
        btnBack = view.findViewById(R.id.btn_back);
        inpName = view.findViewById(R.id.inp_name);
        listViewUsers = view.findViewById(R.id.list_users);
        rdStatusActive = view.findViewById(R.id.rd_status_active);
        spnRole = view.findViewById(R.id.spn_role);
    }

    private void loadViews() {
        _controller
                .filter(filters.get())
                .observe(
                        getViewLifecycleOwner(),
                        _users -> {
                            ListAdapter adapter = new UserAdapter(getActivity(), _users);
                            listViewUsers.setAdapter(adapter);
                            listViewUsers.setOnItemClickListener((_adapterView, _v, pos, _l) -> {
                                selectedUser.setValue(_users.get(pos));
                                Common.addFragment(getParentFragmentManager(), EditUser.getInstance());
                            });
                        }
                );
    }

    private void setupViews() {
        ArrayAdapter roleAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, UserRole.values());

        filters.set(FilterType.EQUAL, "active", true);

        btnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());

        inpName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = inpName.getText().toString().trim().toLowerCase();
                filters.set(FilterType.STRING_CONTAINS, "username", searchText.length() >= 3 ? searchText.trim() : null);
                loadViews();
            }
        });

        spnRole.setAdapter(roleAdapter);
        spnRole.setSelection(roleAdapter.getPosition(UserRole.ALL));
        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals(UserRole.ALL))
                    filters.set(FilterType.EQUAL, "role", null);
                else
                    filters.set(FilterType.EQUAL, "role", adapterView.getItemAtPosition(i));
                loadViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        rdStatusActive.setOnCheckedChangeListener((compoundButton, b) -> {
            filters.set(FilterType.EQUAL, "active", b);
            loadViews();
        });
    }
}
