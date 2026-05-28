package com.rrdm.library_api.service;

import com.rrdm.library_api.model.Book;
import com.rrdm.library_api.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    BookRepository bookRepository;
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Optional<Book> findById(UUID id){
        return bookRepository.findById(id);
    }

    public boolean deleteById(UUID id){
        return bookRepository.deleteById(id);
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public Book update(UUID id, Book book){
        return bookRepository.update(id, book);
    }

    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
}

