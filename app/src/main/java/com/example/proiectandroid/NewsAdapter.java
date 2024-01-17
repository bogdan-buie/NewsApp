package com.example.proiectandroid;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public List<Article> articleList;


    public NewsAdapter(List<Article> articleListList) {
        this.articleList = articleListList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sursa, titlu, data, descriere, autor, categorie;
        public ViewHolder(View itemView) {
            super(itemView);
            sursa = (TextView) itemView.findViewById(R.id.sursa);
            titlu = (TextView) itemView.findViewById(R.id.titlu);
            data = (TextView) itemView.findViewById(R.id.data);
            descriere = (TextView) itemView.findViewById(R.id.descriere);
            autor = (TextView) itemView.findViewById(R.id.autor);
            categorie = (TextView) itemView.findViewById(R.id.categorie);
        }
    }

    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row,parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Article selectedArticle = articleList.get(position);
        holder.sursa.setText(selectedArticle.getSource());
        holder.titlu.setText(selectedArticle.getTitle());
        holder.data.setText(this.formatDate(selectedArticle.getPublished_at()));
        holder.descriere.setText(selectedArticle.getDescription());
        holder.autor.setText("Author:" + selectedArticle.getAuthor());
        holder.categorie.setText("Category: " + selectedArticle.getCategory());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("article", (Serializable) selectedArticle);

                // Porniți activitatea ProductDetailActivity
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
    private static String formatDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        try {
            Date date = inputFormat.parse(inputDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH);
            int day = Integer.parseInt(outputFormat.format(date).split(" ")[1]);
            String suffix = getDaySuffix(day);

            return outputFormat.format(date).replaceFirst("\\b\\d+\\b", day + suffix);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";  // Sau altă gestionare a excepțiilor, în funcție de necesități
        }
    }

    private static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        } else {
            switch (day % 10) {
                case 1:
                    return "st";
                case 2:
                    return "nd";
                case 3:
                    return "rd";
                default:
                    return "th";
            }
        }
    }

}
