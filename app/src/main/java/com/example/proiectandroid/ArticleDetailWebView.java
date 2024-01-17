package com.example.proiectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleDetailWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail_web_view);
        WebView myWebView = (WebView) findViewById(R.id.webview);

        Intent intent = getIntent();
        if (intent.hasExtra("article")) {
            Article article = (Article) intent.getSerializableExtra("article");

            if (article != null) {
                String URL = article.getUrl();
                myWebView.loadUrl(URL);
            }
        }
    }
}