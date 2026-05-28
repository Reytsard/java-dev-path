package view;

public class CLIDisplay {
    public CLIDisplay() {}

    public void showMenu() {
        System.out.println("""
                =======================================================================
                                            Menu
                1.) Login
                2.) Sign Up
                3.) Exit
                =======================================================================""");
    }

    public void showLogin() {
        System.out.println("""
                =======================================================================
                                            Login
                =======================================================================""");
        System.out.print("Username: ");
    }

    public void showPasswordField() {
        System.out.print("Password: ");
    }

    public void showLibraryMenu() {
        System.out.print("""
                ============================================================================
                Welcome to the Library
                1. List all books
                2. Add a book
                3. Edit a book
                4. Remove a book
                5. Logout
                ============================================================================
                Choice:\s""");
    }

    public void showSignUp() {
        System.out.println("""
                =======================================================================
                                          Sign Up
                =======================================================================""");
    }

    public void showAddBook() {
        System.out.println("""
                ============================================================================
                Add a Book
                ============================================================================""");
    }

    public void showEditBook() {
        System.out.println("""
                ============================================================================
                Edit a Book
                ============================================================================""");
    }

    public void showRemoveBook() {
        System.out.println("""
                ============================================================================
                Remove a Book
                ============================================================================""");
    }

    public void prompt(String label) {
        System.out.print(label + ": ");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
