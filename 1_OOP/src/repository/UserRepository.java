package repository;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users;
    public UserRepository(){
        users = new ArrayList<>();
    }

    public UserRepository(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
