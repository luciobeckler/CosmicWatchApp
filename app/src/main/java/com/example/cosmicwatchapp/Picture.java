package com.example.cosmicwatchapp;

public class Picture {
    private String title;
    private String url;

    public Picture(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}