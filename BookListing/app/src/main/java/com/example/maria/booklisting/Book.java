package com.example.maria.booklisting;

import android.graphics.drawable.Drawable;

public class Book {

    private String title;

    private String publisher;

    private String authors;

    private String publishedDate;

    private String infoLink;

    private Drawable bookImage;

    public Book(String title, String publisher, String authors, String publishedDate, String infoLink, Drawable bookImage) {
        this.title = title;
        this.publisher = publisher;
        this.authors=authors;
        this.publishedDate = publishedDate;
        this.infoLink = infoLink;
        this.bookImage=bookImage;
    }

    public String getBoockTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthors() {
        return authors;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public Drawable getBookImage() {
        return bookImage;
    }
}