package com.example.movie_ticket_booking.Controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common;
import com.example.movie_ticket_booking.FragmentEnum;
import com.example.movie_ticket_booking.MainActivity;
import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.Models.UserRole;
import com.example.movie_ticket_booking.Views.Admin.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.ParseException;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserController {
    public static AuthUserController Instance = null;
    private MutableLiveData<User> userlogin;
    private FirebaseAuth mAuth;
    private UserController userController;
    private MainActivity main;

    private AuthUserController() {
        userlogin = new MutableLiveData<>(new User());
        Log.d("user_authen", "AuthUserController: " + userlogin.getValue().toString());
        mAuth = FirebaseAuth.getInstance();
        userController = UserController.getInstance();
    }

    public static AuthUserController getInstance() {
        if(Instance==null)
            Instance = new AuthUserController();
        return Instance;
    }

    public void Register(Context context, Map<String, String> info, FragmentManager fragmentManager, Fragment redirect) {

        String email = info.get("email");
        String password = info.get("password");
        if(email==null || password==null) return;
        this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(authResult -> {
            if(authResult.isSuccessful()) {
                FirebaseUser u = mAuth.getCurrentUser();

                User _user = this.userlogin.getValue();

                _user.setEmail(email);
                _user.setId(u.getUid());

                try {
                    _user.setInfo(info);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                UserController.getInstance().add(_user, fragmentManager, redirect);
                Toast.makeText(context, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context, "Trung email", Toast.LENGTH_SHORT).show();

        });
    }

    public void Login(Context context, String email, String password, FragmentManager fragmentManager, Fragment redirect) {
        if (email == null || password == null) return;

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser u = mAuth.getCurrentUser();

                userController.TryGet(u.getUid()).get().addOnSuccessListener(document -> {
                    userlogin.setValue(document.toObject(User.class));
                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    if (userlogin.getValue().getRole() == UserRole.CLIENT)
                        Common.changeFragment(fragmentManager, redirect);
                    else {
                        Common.changeFragment(fragmentManager, Home.getInstance());
//                        MainActivity m = MainActivity.getInstance();
//                        m.startActivity(new Intent(context, MainActivity.class));
                    }
                });
            }
            else
                Toast.makeText(context, "Không đúng email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        });
    }
}
