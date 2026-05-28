package com.rrdm.library_api.model;

import java.util.UUID;

public class Book {
    private UUID id;
    private String title;
    private String author;
    private int pages;

    public Book() {
        id = UUID.randomUUID();
        title = "";
        author = "";
        pages = 0;
    }

    public Book(String title, String author, int pages) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
