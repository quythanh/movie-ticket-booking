package com.example.movie_ticket_booking.Views.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.movie_ticket_booking.Common;
import com.example.movie_ticket_booking.Controllers.UserController;
import com.example.movie_ticket_booking.Models.User;
import com.example.movie_ticket_booking.Models.UserRole;
import com.example.movie_ticket_booking.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class EditUser extends Fragment {
    private static EditUser _instance = null;
    private User user;
    private final UserController _controller = UserController.getInstance();

    private Button btnSave;
    private EditText txtFirstName, txtLastName, txtEmail, txtUsername, txtPhone, txtBirthday;
    private ImageView btnBack, btnDelete, imgAvatar;
    private RadioButton rdGenderFemale, rdGenderMale;
    private Spinner spnRole;
    private MaterialSwitch swActive;
    private Uri imgUri;

    private EditUser() {
        super(R.layout.frag_admin_edit_user);
    }

    public static EditUser getInstance() {
        if (_instance == null)
            _instance = new EditUser();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        bindingViews(view);
        setupViews(view);
        return view;
    }

    private void bindingViews(@Nullable View view) {
        btnBack = view.findViewById(R.id.btn_back);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnSave = view.findViewById(R.id.btn_save);

        imgAvatar = view.findViewById(R.id.img_avatar);

        rdGenderFemale = view.findViewById(R.id.rd_gender_female);
        rdGenderMale = view.findViewById(R.id.rd_gender_male);

        spnRole = view.findViewById(R.id.spn_role);
        swActive = view.findViewById(R.id.sw_active);

        txtBirthday = view.findViewById(R.id.inp_birthday);
        txtEmail = view.findViewById(R.id.inp_email);
        txtFirstName = view.findViewById(R.id.inp_first_name);
        txtLastName = view.findViewById(R.id.inp_last_name);
        txtUsername = view.findViewById(R.id.inp_username);
        txtPhone = view.findViewById(R.id.inp_phone);
    }

    private void loadViews(View view) {
        String avatarUrl = user.getAvatarPath();
        ArrayAdapter roleAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, UserRole.values());

        if (avatarUrl != null)
            Glide.with(view).load(avatarUrl).into(imgAvatar);

        spnRole.setAdapter(roleAdapter);

        rdGenderFemale.setChecked(!user.isGender());
        rdGenderMale.setChecked(user.isGender());

        spnRole.setSelection(roleAdapter.getPosition(user.getRole()));
        swActive.setChecked(user.isActive());

        txtEmail.setText(user.getEmail());
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtUsername.setText(user.getUsername());
        txtPhone.setText(user.getPhone());

        if (user.getBirthdate() != null)
            txtBirthday.setText(Common.dateFormatter.format(user.getBirthdate()));

        txtBirthday.setInputType(InputType.TYPE_NULL);
        txtBirthday.setOnClickListener(v -> {
            Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            DatePickerDialog picker = new DatePickerDialog(getContext(),
                    (view1, year1, monthOfYear, dayOfMonth) -> txtBirthday.setText(String.format("%s/%s/%s", dayOfMonth, monthOfYear + 1, year1)), year, month, day);
            picker.show();
        });
    }

    private void setupViews(View view) {
        btnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());

        ArrayAdapter roleAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, UserRole.values());
        spnRole.setAdapter(roleAdapter);

        imgAvatar.setOnClickListener(_v -> {
            ImagePicker.with(getActivity())
                    .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        ManageUsers.getSelectedUser().observe(getViewLifecycleOwner(), selectedUser -> {
            imgUri = null;
            user = selectedUser;

            loadViews(view);

            btnDelete.setOnClickListener(_v -> this._controller.delete(user.getId()));
            btnSave.setOnClickListener(_v -> {
                user.setUsername(txtUsername.getText().toString());
                user.setFirstName(txtFirstName.getText().toString());
                user.setLastName(txtLastName.getText().toString());
                user.setEmail(txtEmail.getText().toString());
                user.setGender(rdGenderMale.isChecked());
                user.setRole(UserRole.valueOf(spnRole.getSelectedItem().toString()));
                user.setActive(swActive.isChecked());
                user.setPhone(txtPhone.getText().toString());
                try {
                    user.setBirthdate(Common.dateFormatter.parse(txtBirthday.getText().toString()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (imgUri != null) {
                    MediaManager.get().upload(imgUri).callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            // your code here
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            // example code starts here
                            Double progress = (double) bytes/totalBytes;
                            // post progress to app UI (e.g. progress bar, notification)
                            // example code ends here
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            String imgCloudinaryUrl = resultData.get("secure_url").toString();
                            user.setAvatarPath(imgCloudinaryUrl);
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            // your code here
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            // your code here
                        }
                    })
                    .dispatch();
                }

                try {
                    this._controller.update(user.getId(), user);
                    Toast.makeText(getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public void handlePickImageResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            imgUri = data.getData();
            imgAvatar.setImageURI(imgUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR)
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
    }
}
