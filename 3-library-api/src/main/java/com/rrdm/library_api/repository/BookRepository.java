package com.rrdm.library_api.repository;

import com.rrdm.library_api.model.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BookRepository {
    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
    }

    public List<Book> findAll() {
        return books;
    }

    public Book save(Book book) {
        books.add(book);
        return book;
    }

    public Optional<Book> findById(UUID id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public boolean deleteById(UUID id) {
        return books.removeIf(b -> b.getId().equals(id));
    }

    public Book update(UUID id, Book book){
        Optional<Book> bookToFind = findById(id);
        if(bookToFind.isPresent()){
            Book existing = bookToFind.get();
            int index = books.indexOf(bookToFind.get());

            if(book.getAuthor() != null) existing.setAuthor(book.getAuthor());
            if(book.getPages() != 0) existing.setPages(book.getPages());
            if(book.getTitle() != null) existing.setTitle(book.getTitle());
            return books.set(index, existing);
        }
        return null;
    }

    public List<Book> findByAuthor(String author) {
        return books.stream().filter(b -> b.getAuthor().contains(author)).collect(Collectors.toList());
    }
}
