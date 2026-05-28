package service;

import model.Book;
import repository.BookRepository;

public class BookService {
    BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book){
        bookRepository.addBook(book);
    }

    public void removeBook(Book book) {
        bookRepository.removeBook(book);
    }

    public void editBook(Book book) {
        bookRepository.editBook(book);
    }

    public Book getBookById(int id) {
        return bookRepository.getBookById(id);
    }
}
