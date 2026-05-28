package controller;

import model.Book;
import model.Category;
import model.User;
import repository.BookRepository;
import repository.UserRepository;
import service.BookService;
import service.UserService;
import view.CLIDisplay;

import java.util.List;
import java.util.Scanner;

public class CLIController {
    private CLIDisplay cliDisplay;
    private BookService bookService;
    private UserService userService;

    private static final Scanner kbd = new Scanner(System.in);

    public CLIController() {
        cliDisplay = new CLIDisplay();
        userService = new UserService(new UserRepository());
        bookService = new BookService(new BookRepository());
        seedData();
    }

    private void seedData() {
        User admin = new User("Admin", 30);
        admin.setUsername("admin");
        admin.setPassword("admin123");
        userService.addUser(admin);

        Book b1 = new Book("Clean Code", "Robert Martin", 55, 431, true);
        b1.setCategory(new Category("Programming"));
        bookService.addBook(b1);

        Book b2 = new Book("The Pragmatic Programmer", "David Thomas", 60, 352, true);
        b2.setCategory(new Category("Programming"));
        bookService.addBook(b2);

        Book b3 = new Book("Design Patterns", "Erich Gamma", 50, 395, false);
        b3.setCategory(new Category("Software Engineering"));
        bookService.addBook(b3);
    }

    public CLIDisplay getCliDisplay() {
        return cliDisplay;
    }

    public String getInput() {
        return kbd.nextLine();
    }

    public boolean validateUsernameAndPassword(String username, String password) {
        try {
            return userService.validateUser(username.trim(), password);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasAnyUsers() {
        return !userService.getUserRepository().getUsers().isEmpty();
    }

    public List<Book> getAllBooks() {
        return bookService.getBookRepository().getBooks();
    }

    public void addBook(String title, String authorName, int authorAge, int pages, String categoryName, boolean isAvailable) {
        Book book = new Book(title, authorName, authorAge, pages, isAvailable);
        book.setCategory(new Category(categoryName));
        bookService.addBook(book);
    }

    public Book getBookById(int id) {
        return bookService.getBookById(id);
    }

    public void editBook(Book book) {
        bookService.editBook(book);
    }

    public void removeBook(Book book) {
        bookService.removeBook(book);
    }

    public boolean usernameExists(String username) {
        try {
            userService.getUserByUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void createUser(String name, int age, String username, String password) {
        User user = new User(name, age);
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }
}
