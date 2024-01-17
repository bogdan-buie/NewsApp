package com.example.proiectandroid;

import java.util.List;

public class NewsResponse {
    private Pagination pagination;
    private List<Article> data;

    public NewsResponse() {
    }

    public NewsResponse(Pagination pagination, List<Article> data) {
        this.pagination = pagination;
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
        this.data = data;
    }
}
