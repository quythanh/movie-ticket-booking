package com.example.movie_ticket_booking.Common;

import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;

public class CloudinaryUploadCallback implements UploadCallback {
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
