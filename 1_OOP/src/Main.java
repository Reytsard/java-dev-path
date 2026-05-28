import controller.CLIController;
import model.Book;
import model.Category;

import java.util.List;

public class Main {
    private CLIController controller;
    private boolean isLoggedIn = false;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        controller = new CLIController();
        boolean running = true;
        while (running) {
            controller.getCliDisplay().showMenu();
            String inp = controller.getInput().trim();
            switch (inp) {
                case "1" -> {
                    loginPath();
                    if (isLoggedIn) {
                        libraryLoop();
                        isLoggedIn = false;
                    }
                }
                case "2" -> signUpPath();
                case "3" -> running = false;
                default -> controller.getCliDisplay().showMessage("Invalid choice, please try again.");
            }
        }
    }

    private void loginPath() {
        if (!controller.hasAnyUsers()) {
            controller.getCliDisplay().showMessage("No users registered.");
            return;
        }
        controller.getCliDisplay().showLogin();
        String username = controller.getInput();
        controller.getCliDisplay().showPasswordField();
        String password = controller.getInput();
        isLoggedIn = controller.validateUsernameAndPassword(username, password);
        if (!isLoggedIn) {
            controller.getCliDisplay().showMessage("Invalid username or password.");
        }
    }

    private void signUpPath() {
        controller.getCliDisplay().showSignUp();

        controller.getCliDisplay().prompt("Name");
        String name = controller.getInput().trim();
        if (name.isBlank()) {
            controller.getCliDisplay().showMessage("Name cannot be empty. Sign up cancelled.");
            return;
        }

        controller.getCliDisplay().prompt("Age");
        int age;
        try {
            age = Integer.parseInt(controller.getInput().trim());
        } catch (NumberFormatException e) {
            controller.getCliDisplay().showMessage("Invalid age. Sign up cancelled.");
            return;
        }

        controller.getCliDisplay().prompt("Username");
        String username = controller.getInput().trim();
        if (username.isBlank()) {
            controller.getCliDisplay().showMessage("Username cannot be empty. Sign up cancelled.");
            return;
        }
        if (controller.usernameExists(username)) {
            controller.getCliDisplay().showMessage("Username already taken. Sign up cancelled.");
            return;
        }

        controller.getCliDisplay().prompt("Password");
        String password = controller.getInput();
        if (password.isBlank()) {
            controller.getCliDisplay().showMessage("Password cannot be empty. Sign up cancelled.");
            return;
        }

        controller.getCliDisplay().prompt("Confirm Password");
        String confirmPassword = controller.getInput();
        if (!password.equals(confirmPassword)) {
            controller.getCliDisplay().showMessage("Passwords do not match. Sign up cancelled.");
            return;
        }

        controller.createUser(name, age, username, password);
        controller.getCliDisplay().showMessage("Account created successfully. You can now log in.");
    }

    private void libraryLoop() {
        boolean inLibrary = true;
        while (inLibrary) {
            controller.getCliDisplay().showLibraryMenu();
            String choice = controller.getInput().trim();
            switch (choice) {
                case "1" -> showAllBooks();
                case "2" -> addBook();
                case "3" -> editBook();
                case "4" -> removeBook();
                case "5" -> {
                    controller.getCliDisplay().showMessage("Logged out successfully.");
                    inLibrary = false;
                }
                default -> controller.getCliDisplay().showMessage("Invalid choice, please try again.");
            }
        }
    }

    private void showAllBooks() {
        List<Book> books = controller.getAllBooks();
        if (books.isEmpty()) {
            controller.getCliDisplay().showMessage("No books in the library.");
            return;
        }
        controller.getCliDisplay().showMessage("============================================================================");
        controller.getCliDisplay().showMessage(String.format("%-4s %-30s %-20s %-6s %-20s %-9s",
                "ID", "Title", "Author", "Pages", "Category", "Available"));
        controller.getCliDisplay().showMessage("============================================================================");
        for (Book book : books) {
            controller.getCliDisplay().showMessage(String.format("%-4d %-30s %-20s %-6d %-20s %-9s",
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor().getName(),
                    book.getPages(),
                    book.getCategory() != null ? book.getCategory().getName() : "-",
                    book.isAvailable() ? "Yes" : "No"));
        }
        controller.getCliDisplay().showMessage("============================================================================");
    }

    private void addBook() {
        controller.getCliDisplay().showAddBook();

        controller.getCliDisplay().prompt("Title");
        String title = controller.getInput().trim();

        controller.getCliDisplay().prompt("Author Name");
        String authorName = controller.getInput().trim();

        controller.getCliDisplay().prompt("Author Age");
        int authorAge;
        try {
            authorAge = Integer.parseInt(controller.getInput().trim());
        } catch (NumberFormatException e) {
            controller.getCliDisplay().showMessage("Invalid age. Book not added.");
            return;
        }

        controller.getCliDisplay().prompt("Pages");
        int pages;
        try {
            pages = Integer.parseInt(controller.getInput().trim());
        } catch (NumberFormatException e) {
            controller.getCliDisplay().showMessage("Invalid page count. Book not added.");
            return;
        }

        controller.getCliDisplay().prompt("Category");
        String category = controller.getInput().trim();

        controller.getCliDisplay().prompt("Is Available? (y/n)");
        String availStr = controller.getInput().trim().toLowerCase();
        boolean isAvailable = availStr.equals("y") || availStr.equals("yes");

        controller.addBook(title, authorName, authorAge, pages, category, isAvailable);
        controller.getCliDisplay().showMessage("Book added successfully.");
    }

    private void editBook() {
        showAllBooks();
        controller.getCliDisplay().showEditBook();
        controller.getCliDisplay().prompt("Enter Book ID to edit");
        try {
            int id = Integer.parseInt(controller.getInput().trim());
            Book book = controller.getBookById(id);

            controller.getCliDisplay().prompt("New Title (current: " + book.getTitle() + ", Enter to keep)");
            String title = controller.getInput().trim();
            if (!title.isBlank()) book.setTitle(title);

            controller.getCliDisplay().prompt("New Author Name (current: " + book.getAuthor().getName() + ", Enter to keep)");
            String authorName = controller.getInput().trim();
            if (!authorName.isBlank()) book.getAuthor().setName(authorName);

            controller.getCliDisplay().prompt("New Pages (current: " + book.getPages() + ", Enter to keep)");
            String pagesStr = controller.getInput().trim();
            if (!pagesStr.isBlank()) {
                try {
                    book.setPages(Integer.parseInt(pagesStr));
                } catch (NumberFormatException e) {
                    controller.getCliDisplay().showMessage("Invalid pages, keeping current value.");
                }
            }

            String currentCat = book.getCategory() != null ? book.getCategory().getName() : "none";
            controller.getCliDisplay().prompt("New Category (current: " + currentCat + ", Enter to keep)");
            String catName = controller.getInput().trim();
            if (!catName.isBlank()) book.setCategory(new Category(catName));

            controller.getCliDisplay().prompt("Is Available? (current: " + (book.isAvailable() ? "Yes" : "No") + ", y/n, Enter to keep)");
            String availStr = controller.getInput().trim().toLowerCase();
            if (!availStr.isBlank()) {
                book.setAvailable(availStr.equals("y") || availStr.equals("yes"));
            }

            controller.editBook(book);
            controller.getCliDisplay().showMessage("Book updated successfully.");
        } catch (NumberFormatException e) {
            controller.getCliDisplay().showMessage("Invalid ID.");
        } catch (Exception e) {
            controller.getCliDisplay().showMessage("Book not found.");
        }
    }

    private void removeBook() {
        showAllBooks();
        controller.getCliDisplay().showRemoveBook();
        controller.getCliDisplay().prompt("Enter Book ID to remove");
        try {
            int id = Integer.parseInt(controller.getInput().trim());
            Book book = controller.getBookById(id);

            controller.getCliDisplay().prompt("Confirm remove \"" + book.getTitle() + "\"? (y/n)");
            String confirm = controller.getInput().trim().toLowerCase();
            if (confirm.equals("y") || confirm.equals("yes")) {
                controller.removeBook(book);
                controller.getCliDisplay().showMessage("Book removed successfully.");
            } else {
                controller.getCliDisplay().showMessage("Remove cancelled.");
            }
        } catch (NumberFormatException e) {
            controller.getCliDisplay().showMessage("Invalid ID.");
        } catch (Exception e) {
            controller.getCliDisplay().showMessage("Book not found.");
        }
    }
}
