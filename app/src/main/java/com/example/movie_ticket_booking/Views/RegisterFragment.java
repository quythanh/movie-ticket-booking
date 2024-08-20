package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.FragmentEnum;
import com.example.movie_ticket_booking.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }
    private String username, lastname, firstname, password, confirmPass, phone, email, birthday;
    private boolean gender;
    private RadioGroup radGroup;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Map<String, String> info = new HashMap<>();
        gender = true;

        Button confirm = view.findViewById(R.id.RegisterBtn);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText;
                editText = view.findViewById(R.id.username);
                username = editText!=null ? editText.getText().toString() : "";
                editText = view.findViewById(R.id.Lastname);
                lastname = editText!=null ? editText.getText().toString() : "";
                editText = view.findViewById(R.id.Firstname);
                firstname = editText!=null ? editText.getText().toString() : "";
                editText = view.findViewById(R.id.password);
                password = editText!=null ? editText.getText().toString() : "";
                editText = view.findViewById(R.id.confirmpass);
                confirmPass = editText!=null ? editText.getText().toString() : "";
                editText = view.findViewById(R.id.email);
                email = editText!=null ? editText.getText().toString() : "";
                editText = view.findViewById(R.id.Phone);
                phone = editText!=null ? editText.getText().toString() : "";
                editText = view.findViewById(R.id.birthday);
                birthday = editText!=null ? editText.getText().toString() : "";
                radGroup = view.findViewById(R.id.genderRad);
                gender = radGroup.getCheckedRadioButtonId() == R.id.male;

                if(!username.isBlank() && !lastname.isBlank() && !firstname.isBlank() && !email.isBlank()
                        && !password.isBlank() && !confirmPass.isBlank() && !phone.isBlank() && password.equals(confirmPass) )
                {
                    info.put("lastname", lastname);
                    info.put("firstname", firstname);
                    info.put("email", email);
                    info.put("username", username);
                    info.put("password", password);
                    info.put("phone", phone);
                    if(birthday != null)
                        info.put("birthdate", birthday);
                    info.put("gender", gender ? "nam" : "nữ");

                    AuthUserController.getInstance().Register(view.getContext(),info, FragmentEnum.FILM);

                }
                else{
                    if(!password.equals(confirmPass))
                        Toast.makeText(view.getContext(), "Thiếu thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(view.getContext(), "Thiếu thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}