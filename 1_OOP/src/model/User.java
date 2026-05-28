package model;

public class User {
    private static int idCounter = 1;
    private String name;
    private int age;
    private int id;
    private String username;
    private String password;

    public User(){
        id = idCounter++;
        name = "";
        age = 0;
    }

    public User(String name, int age){
        id = idCounter++;
        this.name = name;
        this.age = age;
    }

    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}