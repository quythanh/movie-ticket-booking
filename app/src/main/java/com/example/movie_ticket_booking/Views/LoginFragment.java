package com.example.movie_ticket_booking.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_booking.Common.UIManager;
import com.example.movie_ticket_booking.Controllers.AuthUserController;
import com.example.movie_ticket_booking.R;

public class LoginFragment extends Fragment {
    private static LoginFragment _instance = null;

    private EditText email, password;
    private Button loginBtn, registerBtn;
    private AuthUserController authController;
    private ImageView img;

    private LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment getInstance() {
        if (_instance == null)
            _instance = new LoginFragment();
        return _instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        bindingViews(view);
        setupViews(view);
        return view;
    }

    private void bindingViews(View view) {
        email = view.findViewById(R.id.emailTxt);
        password = view.findViewById(R.id.passwordTxt);
        loginBtn = view.findViewById(R.id.loginBtn);
        authController = AuthUserController.getInstance();
        registerBtn = view.findViewById(R.id.RegisterBtn);
        img = view.findViewById(R.id.imageViewLogo);
    }

    private void setupViews(View view) {
        Glide.with(view.getContext()).load(R.drawable.logo).into(img);

        loginBtn.setOnClickListener(_v -> {
            String mail = email.getText().toString();
            String pass = password.getText().toString();
            email.setText("");
            password.setText("");
            if(!mail.isBlank() && !pass.isBlank())
                authController.Login(view.getContext(), mail, pass, getParentFragmentManager(), FilmFragment.getInstance());
            else
                Toast.makeText(view.getContext(), "EMPTY", Toast.LENGTH_SHORT).show();
        });
        registerBtn.setOnClickListener(_v -> UIManager.addFragment(getParentFragmentManager(), RegisterFragment.getInstance()));
    }
}