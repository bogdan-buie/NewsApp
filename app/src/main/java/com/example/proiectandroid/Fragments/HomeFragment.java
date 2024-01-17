package com.example.proiectandroid.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.proiectandroid.Article;
import com.example.proiectandroid.GetTask;
import com.example.proiectandroid.NewsAdapter;
import com.example.proiectandroid.NewsResponse;
import com.example.proiectandroid.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView rvArticles;
    List<Article> articleList = new ArrayList<>();
    EditText editTextCautare;
    Button btnCautare;

    NewsAdapter newsAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rvArticles = rootView.findViewById(R.id.rvArticles);
        editTextCautare = rootView.findViewById(R.id.etCautare);
        btnCautare = rootView.findViewById(R.id.btnCautare);

        initialNewsGet();



        btnCautare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cautare(view);
            }
        });
        return rootView;
    }

    public void cautare(View view){
        String keywords = editTextCautare.getText().toString();
        if(!keywords.equals("")){
            new GetTask(new GetTask.Callback() {
                @Override
                public void onTaskComplete(NewsResponse result) {

                    if (result != null) {
                        Log.d("GET", "Received NewsResponse: " + result.toString());
                        setArticleToList(result);
                    } else {
                        Log.e("MainActivity", "Error in GET request");
                    }
                    Log.d("GET","http://api.mediastack.com/v1/news?access_key=b04bb16ada4a535a8e54f02a856016e6&keywords="+keywords);
                }

            }).execute("http://api.mediastack.com/v1/news?access_key=b04bb16ada4a535a8e54f02a856016e6&keywords="+keywords);
        }
    }

    public void initialNewsGet(){
        new GetTask(new GetTask.Callback() {
            @Override
            public void onTaskComplete(NewsResponse result) {

                if (result != null) {
                    Log.d("MainActivity", "Received NewsResponse: " + result.toString());
                    setArticleToList(result);
                } else {
                    Log.e("MainActivity", "Error in GET request");
                }
            }

        }).execute("http://api.mediastack.com/v1/news?access_key=b04bb16ada4a535a8e54f02a856016e6&keywords=breaking-news");
    }
    public void setArticleToList(NewsResponse newsResponse){
        articleList.clear();
        Log.d("GET", "setArticle");
        for(Article myarticle : newsResponse.getData()){
            articleList.add(myarticle);
        }

        newsAdapter = new NewsAdapter(articleList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(requireContext());
        rvArticles.setLayoutManager(manager);
        rvArticles.setAdapter(newsAdapter);
    }
}
