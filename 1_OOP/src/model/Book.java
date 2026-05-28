package model;

public class Book {
    private static int idCounter = 1;
    private int id;
    private int pages;
    private boolean isAvailable;
    private User author;
    private Category category;
    private String title;

    public Book() {
        title = "";
        author = new User();
        pages = 0;
        isAvailable = false;
        id = idCounter++;
    }

    public Book(String title, String name, int age, int pages, boolean isAvailable) {
        User author = new User(name, age);
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.isAvailable = isAvailable;
        id = idCounter++;
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return author;
    }

    public int getPages() {
        return pages;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public void setPage(int pages) {
        this.pages = pages;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", pages=" + pages +
                ", isAvailable=" + isAvailable +
                ", author=" + author.getName() +
                ", category=" + category +
                ", title='" + title + '\''
                ;
    }
}