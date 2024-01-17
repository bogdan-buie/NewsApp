package com.example.proiectandroid;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GetTask extends AsyncTask<String, Void, NewsResponse> {

    private static final String TAG = "GetTask";
    private final Callback callback;

    // Interfața de callback pentru a notifica apelantul cu privire la rezultat
    public interface Callback {
        void onTaskComplete(NewsResponse result);
    }

    public GetTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected NewsResponse doInBackground(String... urls) {
        if (urls.length == 0) {
            return null;
        }

        String url = urls[0];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonResponse, NewsResponse.class);
            } else {
                Log.e(TAG, "Unsuccessful response: " + response.code());
            }
        } catch (IOException e) {
            Log.e(TAG, "Error during HTTP GET request", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(NewsResponse result) {
        super.onPostExecute(result);
        if (callback != null) {
            callback.onTaskComplete(result);
        }
    }
}