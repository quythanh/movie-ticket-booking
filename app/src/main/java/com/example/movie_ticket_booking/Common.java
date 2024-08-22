package com.example.movie_ticket_booking;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.text.SimpleDateFormat;
import java.util.Map;

public class Common {
    public static SimpleDateFormat dateFormatter;
    public static SimpleDateFormat dateTimeFormatter;
    public static SimpleDateFormat timeFormatter;

    static {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        timeFormatter = new SimpleDateFormat("hh:mm");
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void changeFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    public static class CloudinaryUploadCallback implements UploadCallback {
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
            // your code here
        }

        @Override
        public void onError(String requestId, ErrorInfo error) {
            // your code here
        }

        @Override
        public void onReschedule(String requestId, ErrorInfo error) {
            // your code here
        }
    }
}
