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

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.example.movie_ticket_booking.Common.CloudinaryUploadCallback;
import com.example.movie_ticket_booking.Common.Constant;
import com.example.movie_ticket_booking.Common.EditContext;
import com.example.movie_ticket_booking.Components.HorizontalStringAdapter;
import com.example.movie_ticket_booking.Controllers.MovieController;
import com.example.movie_ticket_booking.Models.Movie;
import com.example.movie_ticket_booking.Models.MovieType;
import com.example.movie_ticket_booking.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class EditMovie extends Fragment {
    // === REGION: FIELDS ===
    private static EditMovie _instance = null;

    private Button btnAddActor, btnAddDirector, btnUpdate;
    private EditText inpActor, inpDirector, inpIntro, inpMinute, inpPremiere, inpTitle, inpTrailer;
    private ImageView btnBack, imgLandscape, imgPoster;
    private RecyclerView listActors, listDirectors;
    private Spinner spnMovieType;

    private ArrayAdapter<MovieType> movieTypeAdapter;
    private HorizontalStringAdapter listActorsAdapter, listDirectorsAdapter;
    private LinearLayoutManager layoutManagerActors, layoutManagerDirectors;
    private List<String> actors, directors;
    private Movie movie;
    private MovieController _controller;
    private Uri imgLandscapeUri, imgPosterUri;
    // === END REGION ===


    // === REGION: METHODS ===
    private EditMovie() {
        super(R.layout.frag_admin_edit_movie);
    }

    public static EditMovie getInstance() {
        if (_instance == null)
            _instance = new EditMovie();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        bindingViews(view);
        setupViews(view);
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
        btnUpdate = view.findViewById(R.id.btn_update);

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

    private void setupViews(View view) {
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
        btnUpdate.setOnClickListener(_v -> {
            movie.setActors(actors);
            movie.setDirectors(directors);
            movie.setIntro(inpIntro.getText().toString());
            movie.setMinute(Integer.parseInt(inpMinute.getText().toString()));
            movie.setTitle(inpTitle.getText().toString());
            movie.setTrailer(inpTrailer.getText().toString());
            movie.setType(MovieType.valueOf(spnMovieType.getSelectedItem().toString()));
            try {
                movie.setPremiere(Constant.DATE_FORMATTER.parse(inpPremiere.getText().toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (imgLandscapeUri != null) {
                MediaManager.get()
                        .upload(imgLandscapeUri)
                        .callback(new CloudinaryUploadCallback() {
                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                String imgCloudinaryUrl = resultData.get("secure_url").toString();
                                movie.setLandscapeImage(imgCloudinaryUrl);
                                try {
                                    _controller.update(movie.getId(), movie);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        })
                        .dispatch();
            }
            if (imgPosterUri != null) {
                MediaManager.get()
                        .upload(imgPosterUri)
                        .callback(new CloudinaryUploadCallback() {
                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                String imgCloudinaryUrl = resultData.get("secure_url").toString();
                                movie.setPoster(imgCloudinaryUrl);
                                try {
                                    _controller.update(movie.getId(), movie);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        })
                        .dispatch();
            }

            try {
                _controller.update(movie.getId(), movie);
                Toast.makeText(getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        imgLandscape.setOnClickListener(_v -> {
            ImagePicker.with(getActivity())
                    .galleryOnly()
                    .compress(1024)
                    .start(104);
        });
        imgPoster.setOnClickListener(_v -> {
            ImagePicker.with(getActivity())
                    .galleryOnly()
                    .compress(1024)
                    .start(105);
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

        EditContext.movie.observe(getViewLifecycleOwner(), _selectedMovie -> {
            imgLandscapeUri = null;
            imgPosterUri = null;
            movie = _selectedMovie;
            actors.addAll(movie.getActors());
            directors.addAll(movie.getDirectors());
            loadViews(view);
        });
    }

    private void loadViews(View view) {
        String lanscapeUrl = movie.getLandscapeImage(),
            posterUrl = movie.getPoster();
        if (lanscapeUrl != null && !lanscapeUrl.isEmpty())
            Glide.with(view).load(lanscapeUrl).into(imgLandscape);
        if (posterUrl != null && !posterUrl.isEmpty())
            Glide.with(view).load(posterUrl).into(imgPoster);

        listActorsAdapter.notifyDataSetChanged();
        listDirectorsAdapter.notifyDataSetChanged();
        inpIntro.setText(movie.getIntro());
        inpMinute.setText(String.format("%d", movie.getMinute()));
        inpPremiere.setText(Constant.DATE_FORMATTER.format(movie.getPremiere()));
        inpTitle.setText(movie.getTitle());
        inpTrailer.setText(movie.getTrailer());

        spnMovieType.setSelection(movieTypeAdapter.getPosition(movie.getType()));
    }

    public void handlePickImageResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == getActivity().RESULT_OK)
            switch (requestCode) {
                case 104:
                    imgLandscapeUri = data.getData();
                    imgLandscape.setImageURI(imgLandscapeUri);
                    break;
                case 105:
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
