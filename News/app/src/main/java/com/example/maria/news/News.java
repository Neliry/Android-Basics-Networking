package com.example.maria.news;

import android.graphics.drawable.Drawable;

public class News {
    private String sectionName;

    private String title;

    private String author;

    private String publishedDate;

    private String infoLink;

    private Drawable image;


    public News(String sectionName, String title, String author, String publishedDate, String infoLink, Drawable image) {
        this.sectionName = sectionName;
        this.title = title;
        this.author=author;
        this.publishedDate = publishedDate;
        this.infoLink = infoLink;
        this.image=image;
    }

    public String getNewsTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public Drawable getImage() {
        return image;
    }
}