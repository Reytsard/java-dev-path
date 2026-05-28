package repository;

import model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private List<Book> books;

    public BookRepository() {
        books = new ArrayList<>();
    }

    public BookRepository(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void editBook(Book book) {
        int index = getIndexByBookId(book.getId());
        books.set(index, book);
    }

    public int getIndexByBookId(int id) {
        Book book = books.stream().filter(b -> b.getId() == id).findFirst().orElseThrow();
        return books.indexOf(book);
    }

    public Book getBookById(int id) {
        return books.stream().filter(b -> b.getId() == id).findFirst().orElseThrow();
    }
}
