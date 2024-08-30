package com.example.movie_ticket_booking.Views;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    private static RegisterFragment _instance = null;

    private RegisterFragment() {
        super(R.layout.fragment_register);
    }

    public static RegisterFragment getInstance() {
        if (_instance == null)
            _instance = new RegisterFragment();
        return _instance;
    }

    private String username, lastname, firstname, password, confirmPass, phone, email, birthday;
    private boolean gender;
    private RadioGroup radGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Map<String, String> info = new HashMap<>();
        gender = true;

        EditText mInpBirthday = view.findViewById(R.id.birthday);
        mInpBirthday.setOnClickListener(_v -> {
            Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            DatePickerDialog picker = new DatePickerDialog(
                    getContext(),
                    (_view, _year, _month, _day) -> {
                        Date date = new GregorianCalendar(_year, _month, _day).getTime();
                        String strDate = Constant.DATE_FORMATTER.format(date);
                        mInpBirthday.setText(strDate);
                    },
                    year, month, day
            );
            picker.show();
        });

        Button confirm = view.findViewById(R.id.RegisterBtn);
        confirm.setOnClickListener(v -> {

            EditText editText;
            editText = view.findViewById(R.id.username);
            username = editText.getText().toString();

            editText = view.findViewById(R.id.Lastname);
            lastname = editText.getText().toString();

            editText = view.findViewById(R.id.Firstname);
            firstname = editText.getText().toString();

            editText = view.findViewById(R.id.password);
            password = editText.getText().toString();

            editText = view.findViewById(R.id.confirmpass);
            confirmPass = editText.getText().toString();

            editText = view.findViewById(R.id.email);
            email = editText.getText().toString();

            editText = view.findViewById(R.id.Phone);
            phone = editText.getText().toString();

            birthday = mInpBirthday.getText().toString();

            radGroup = view.findViewById(R.id.genderRad);
            gender = radGroup.getCheckedRadioButtonId() == R.id.male;

            if (username.isBlank() || lastname.isBlank() || firstname.isBlank() || email.isBlank() || password.isBlank() || confirmPass.isBlank() || phone.isBlank()) {
                Toast.makeText(view.getContext(), "Thiếu thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPass)) {
                Toast.makeText(view.getContext(), "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
            } else {
                info.put("lastname", lastname);
                info.put("firstname", firstname);
                info.put("email", email);
                info.put("username", username);
                info.put("password", password);
                info.put("phone", phone);
                if (birthday != null)
                    info.put("birthdate", birthday);
                info.put("gender", gender ? "Nam" : "Nữ");

                AuthUserController.getInstance().Register(view.getContext(), info, getParentFragmentManager() ,FilmFragment.getInstance());
            }
        });
        return view;
    }
}