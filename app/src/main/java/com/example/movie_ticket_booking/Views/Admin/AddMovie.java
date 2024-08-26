package com.example.movie_ticket_booking.Views.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.example.movie_ticket_booking.Common.CloudinaryUploadCallback;
import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Components.HorizontalStringAdapter;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.MovieType;
import com.example.movie_ticket_booking.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AddMovie extends Fragment {
    private static AddMovie _instance = null;
    private MovieController _controller;

    private Button btnAddActor, btnAddDirector, btnCreate;
    private EditText inpActor, inpDirector, inpIntro, inpMinute, inpPremiere, inpTitle, inpTrailer;
    private ImageView btnBack, imgLandscape, imgPoster;
    private RecyclerView listActors, listDirectors;
    private Spinner spnMovieType;

    private ArrayAdapter<MovieType> movieTypeAdapter;
    private HorizontalStringAdapter listActorsAdapter, listDirectorsAdapter;
    private LinearLayoutManager layoutManagerActors, layoutManagerDirectors;
    private List<String> actors, directors;

    private Uri imgLandscapeUri, imgPosterUri;

    private AddMovie() {
        super(R.layout.frag_admin_add_movie);
    }

    public static AddMovie getInstance() {
        if (_instance == null)
            _instance = new AddMovie();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        bindingViews(view);
        setupViews();
        return view;
    }

    private void initData() {
        _controller = MovieController.getInstance();

        actors = new ArrayList<>();
        directors = new ArrayList<>();

        layoutManagerActors = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerDirectors = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        listActorsAdapter = new HorizontalStringAdapter(actors);
        listDirectorsAdapter = new HorizontalStringAdapter(directors);
        movieTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, MovieType.values());
    }

    private void bindingViews(View view) {
        btnAddActor = view.findViewById(R.id.btn_add_actor);
        btnAddDirector = view.findViewById(R.id.btn_add_director);
        btnBack = view.findViewById(R.id.btn_back);
        btnCreate = view.findViewById(R.id.btn_create);

        imgLandscape = view.findViewById(R.id.img_landscape);
        imgPoster = view.findViewById(R.id.img_poster);

        inpActor = view.findViewById(R.id.inp_actor);
        inpDirector = view.findViewById(R.id.inp_director);
        inpIntro = view.findViewById(R.id.inp_intro);
        inpMinute = view.findViewById(R.id.inp_minute);
        inpPremiere = view.findViewById(R.id.inp_premiere);
        inpTitle = view.findViewById(R.id.inp_title);
        inpTrailer = view.findViewById(R.id.inp_trailer);

        listActors = view.findViewById(R.id.list_actors);
        listDirectors = view.findViewById(R.id.list_directors);

        spnMovieType = view.findViewById(R.id.spn_movie_type);
    }

    private void setupViews() {
        btnAddActor.setOnClickListener(_v -> {
            String _actor = inpActor.getText().toString().trim();
            if (_actor.isEmpty()) {
                Toast.makeText(getContext(), "Actor's name is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            actors.add(_actor);
            inpActor.setText("");
            listActorsAdapter.notifyItemInserted(actors.size() - 1);
        });
        btnAddDirector.setOnClickListener(_v -> {
            String _director = inpDirector.getText().toString().trim();
            if (_director.isEmpty()) {
                Toast.makeText(getContext(), "Director's name is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            directors.add(_director);
            inpDirector.setText("");
            listDirectorsAdapter.notifyItemInserted(directors.size() - 1);
        });
        btnBack.setOnClickListener(_v -> getParentFragmentManager().popBackStack());
        btnCreate.setOnClickListener(_v -> {
            Movie _m =  new Movie();
            _m.setActors(actors);
            _m.setDirectors(directors);
            _m.setType(MovieType.valueOf(spnMovieType.getSelectedItem().toString()));

            try {
                _m.setIntro(inpIntro.getText().toString());
                _m.setMinute(Integer.parseInt(inpMinute.getText().toString()));
                _m.setTitle(inpTitle.getText().toString());
                _m.setTrailer(inpTrailer.getText().toString());
                _m.setPremiere(Constant.DATE_FORMATTER.parse(inpPremiere.getText().toString()));

                if (imgLandscapeUri != null) {
                    MediaManager.get()
                            .upload(imgLandscapeUri)
                            .callback(new CloudinaryUploadCallback() {
                                @Override
                                public void onSuccess(String requestId, Map resultData) {
                                    String imgCloudinaryUrl = resultData.get("secure_url").toString();
                                    _m.setLandscapeImage(imgCloudinaryUrl);
                                }
                            })
                            .dispatch();
                } else {
                    _m.setLandscapeImage("");
                }
                if (imgPosterUri != null) {
                    MediaManager.get()
                            .upload(imgPosterUri)
                            .callback(new CloudinaryUploadCallback() {
                                @Override
                                public void onSuccess(String requestId, Map resultData) {
                                    String imgCloudinaryUrl = resultData.get("secure_url").toString();
                                    _m.setPoster(imgCloudinaryUrl);
                                }
                            })
                            .dispatch();
                } else {
                    _m.setPoster("");
                }

                _controller.add(_m);
                Toast.makeText(getContext(), "Tạo thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        imgLandscape.setOnClickListener(_v -> {
            ImagePicker.with(getActivity())
                    .galleryOnly()
                    .compress(1024)
                    .start(102);
        });
        imgPoster.setOnClickListener(_v -> {
            ImagePicker.with(getActivity())
                    .galleryOnly()
                    .compress(1024)
                    .start(103);
        });

        inpPremiere.setInputType(InputType.TYPE_NULL);
        inpPremiere.setOnClickListener(v -> {
            Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            DatePickerDialog picker = new DatePickerDialog(getContext(),
                    (view1, year1, monthOfYear, dayOfMonth) -> inpPremiere.setText(String.format("%s/%s/%s", dayOfMonth, monthOfYear + 1, year1)), year, month, day);
            picker.show();
        });

        listActors.setAdapter(listActorsAdapter);
        listActors.setLayoutManager(layoutManagerActors);
        listDirectors.setAdapter(listDirectorsAdapter);
        listDirectors.setLayoutManager(layoutManagerDirectors);

        spnMovieType.setAdapter(movieTypeAdapter);
    }

    public void handlePickImageResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == getActivity().RESULT_OK)
            switch (requestCode) {
                case 102:
                    imgLandscapeUri = data.getData();
                    imgLandscape.setImageURI(imgLandscapeUri);
                    break;
                case 103:
                    imgPosterUri = data.getData();
                    imgPoster.setImageURI(imgPosterUri);
                    break;
            }
        else if (resultCode == ImagePicker.RESULT_ERROR)
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
    }
}
