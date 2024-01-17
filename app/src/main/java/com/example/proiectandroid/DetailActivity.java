package com.example.proiectandroid;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.wrappers.PackageManagerWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class DetailActivity extends AppCompatActivity {
    TextView titlu, publicare, autor, sursa, descriere, categorie;
    Button btnDownload, btnWebView;
    ImageView image;
    ImageDownloader imageDownloader;
    private static int REQUEST_CODE = 100;

    private SoundService soundService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SoundService.LocalBinder binder = (SoundService.LocalBinder) service;
            soundService = binder.getService();
            isBound = true;
            Log.d("DetailActivity", "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.d("DetailActivity", "Service disconnected");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        titlu = findViewById(R.id.tvTitlu);
        publicare = findViewById(R.id.tvPublished_at);
        autor = findViewById(R.id.tvAutor);
        sursa = findViewById(R.id.tvSursa);
        descriere = findViewById(R.id.tvDescriere);
        categorie = findViewById(R.id.tvCategorie);
        btnDownload = findViewById(R.id.btnDownloadImage);
        btnWebView = findViewById(R.id.btnWebView);
        image =findViewById(R.id.imageView);
        Intent intentService = new Intent(this, SoundService.class);
        startService(intentService);
        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);

        Intent intent = getIntent();
        Article article = new Article();
        if (intent.hasExtra("article")) {
            article = (Article) intent.getSerializableExtra("article");
            if (article != null) {
                titlu.setText(article.getTitle());
                publicare.setText("PUBLISHED: " + article.getPublished_at());
                autor.setText("AUTHOR: " + article.getAuthor());
                sursa.setText("SOURCE: " + article.getSource());
                descriere.setText(article.getDescription());
                categorie.setText("CATEGORY: " + article.getCategory());
                Log.d("ABC", "Linia 45");
                if(article.getImage()!=null){
                    imageDownloader = new ImageDownloader(image);
                    imageDownloader.execute(article.getImage());
                }
                else{
                    image.setVisibility(View.GONE);
                    btnDownload.setVisibility(View.GONE);
                }

            }
        }
        Article finalArticle = article;
        btnWebView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ArticleDetailWebView.class);
                    intent.putExtra("article", (Serializable) finalArticle);
                view.getContext().startActivity(intent);
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DetailActivity.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveImageToInternalStorage();
                    if (isBound && soundService != null) {
                        soundService.playSound(R.raw.supercell);
                        Log.d("DetailActivity", "SoundService playSound called successfully");
                    } else {
                        Log.e("DetailActivity", "Service not bound or soundService is null");
                    }
                } else {
                    // Dacă permisiunea nu este acordată, solicitați-o la runtime
                    ActivityCompat.requestPermissions(DetailActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                }
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImageToInternalStorage();
            } else {
                Log.d("DownloadImage", "Permisiune respinsă pentru scrierea în stocarea externă");
            }
        }
    }



    private void saveImageToInternalStorage() {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        String ms = String.valueOf(System.currentTimeMillis());
        // Salvați imaginea în stocarea internă
        String filename = "image" + ms + ".jpg";
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File filePath = new File(picturesDir, filename);

        try {
            if (!picturesDir.exists()) {
                picturesDir.mkdirs();  // Creează directorul dacă nu există
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Toast toast = Toast.makeText(this , "Imagine descărcată cu succes", Toast.LENGTH_SHORT);
            toast.show();
            Log.d("DownloadImage", "Imagine descărcată și salvată în stocarea internă: " + filePath.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DownloadImage", "Eroare la salvarea imaginii în stocarea internă");
            Toast toast = Toast.makeText(this , "Imaginea nu a putut fi descărcată", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        super.onDestroy();
    }
}