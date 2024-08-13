package com.example.movie_ticket_booking.Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.FragmentEnum;
import com.example.movie_ticket_booking.MainActivity;
import com.example.movie_ticket_booking.Models.User;
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

    private  AuthUserController() {
        userlogin = new MutableLiveData<>(new User());
        Log.d("user_authen", "AuthUserController: " + userlogin.getValue().toString());
        mAuth = FirebaseAuth.getInstance();
        userController = UserController.getInstance();
    }

    public static AuthUserController getInstance(){
        if(Instance==null)
            Instance = new AuthUserController();
        return Instance;
    }

    public void Register(Context context, Map<String, String> info, FragmentEnum redirect){

        String email = info.get("email");
        String password = info.get("password");
        if(email==null || password==null) return;
        this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(authResult -> {
            if(authResult.isSuccessful()){
                FirebaseUser u = mAuth.getCurrentUser();

                this.userlogin.getValue().setEmail(email);
                this.userlogin.getValue().setId(u.getUid());

                try {
                    this.userlogin.getValue().setInfo(info);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                UserController.getInstance().add(this.userlogin.getValue(), redirect);
                Toast.makeText(context, "Đăng kí thành công", Toast.LENGTH_SHORT).show();

            }
            else
                Toast.makeText(context, "Trung email", Toast.LENGTH_SHORT).show();

        });
    }

    public void Login(Context context, String email, String password, FragmentEnum redirect){
        if(email==null || password == null) return;
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                FirebaseUser u = mAuth.getCurrentUser();
                userController.TryGet(u.getUid()).get().addOnSuccessListener(document -> {
                    userlogin.setValue(document.toObject(User.class));
                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    main = MainActivity.getInstance();
                    main.getFragmentChanger().setValue(redirect);
                });
            }
            else
                Toast.makeText(context, "Không đúng email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        });
    }
//    public void toggleRole(){
//        userlogin.setRole(userlogin.getRole()== UserRole.CLIENT ? UserRole.ADMIN : UserRole.CLIENT);
//    }
}
