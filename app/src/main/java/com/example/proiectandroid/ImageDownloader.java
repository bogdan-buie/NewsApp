package com.example.proiectandroid;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

    HttpURLConnection httpURLConnection;

    private ImageView imageView;

    public ImageDownloader(ImageView imageView) {
        this.imageView = imageView;

    }
    @Override
    public Bitmap doInBackground(String... strings){
        try {
            URL url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            Bitmap temp = BitmapFactory.decodeStream(inputStream);
            return temp;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(Bitmap bitmap){
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }
}
