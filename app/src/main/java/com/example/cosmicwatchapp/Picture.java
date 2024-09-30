package com.example.cosmicwatchapp;

public class Picture {
    private String title;
    private String url;
    private String copyright;

    public Picture(String title, String url, String copyright) {
        this.title = title;
        this.url = url;
        this.copyright = copyright;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getCopyright() {
        return copyright;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", copyright='" + copyright + '\'' +
                '}';
    }
}